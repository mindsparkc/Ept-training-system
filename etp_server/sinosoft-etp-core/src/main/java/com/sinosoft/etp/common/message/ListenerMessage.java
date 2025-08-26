package com.sinosoft.etp.common.message;

import com.alibaba.fastjson.JSON;
import com.sinosoft.etp.common.courseware.mapper.CoursewareConMapper;
import com.sinosoft.etp.common.progress.mapper.ProgressMapper;
import com.sinosoft.etp.common.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 86151
 */
@Component
public class ListenerMessage implements StreamListener<String, MapRecord<String,String,String>> {
    private static final Logger logger = LoggerFactory.getLogger(ListenerMessage.class);

    public static ListenerMessage listenerMessage;

    @Resource
    private CoursewareConMapper coursewareConMapper;
    @Resource
    private ProgressMapper progressMapper;

    @PostConstruct
    private void init(){
        listenerMessage = this;
        listenerMessage.coursewareConMapper = this.coursewareConMapper;
        listenerMessage.progressMapper = this.progressMapper;
    }
    RedisUtil redisUtil;

    public ListenerMessage(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        try {
            Date d = new Date();
            System.out.println("接收到来自redis的消息");
            System.out.println("message id=" + entries.getId());
            System.out.println("stream =" + entries.getStream());
            //获取参数为map类型，需要遍历取值再进行之后的逻辑处理。
            System.out.println("body =" + entries.getValue());
            Map<String,String> map = entries.getValue();
            String eventType = JSON.parse(map.get("eventType").toString()).toString();
            if(eventType.equals(redisUtil.ACT_KC)||eventType.equals(redisUtil.ACT_KJ)||eventType.equals(redisUtil.ACT_KS)||eventType.equals(redisUtil.ACT_RW)){
                addCli(map,d);
            }else if(eventType.equals(redisUtil.ACT_TAG)){
                addJj(map,d);
            }else if(eventType.equals(redisUtil.ACT_ING_XX)){
                addIng(map,d);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /** 点击事件（课件、课程、任务、考试）逻辑处理，存储至redis */
    public void addCli(Map map,Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println("当前时间：" + sdf.format(d));
        String time = "user_act:"+sdf.format(d);
        String value = JSON.parse(map.get("userId").toString())
                +":"+ JSON.parse(map.get("eventType").toString())
                +":"+ JSON.parse(map.get("id").toString());
        redisUtil.addValue(time,value);

    }

    /** 点击事件（交卷）逻辑处理，存储至redis */
    public void addJj(Map map,Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println("当前时间：" + sdf.format(d));
        String time = "user_act_jj:"+sdf.format(d);
        String value = JSON.parse(map.get("userId").toString())
                +":"+ JSON.parse(map.get("eventType").toString())
                +":"+ JSON.parse(map.get("id").toString())
                +":"+ JSON.parse(map.get("hand").toString());
        redisUtil.addValue(time,value);
    }
    /** 驻留时间（学习中）逻辑处理，存储至redis */
    public void addIng(Map map,Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println("当前时间：" + sdf.format(d));
        String userId = JSON.parse(map.get("userId").toString()).toString();
        String key = "user_act_ing:" + sdf.format(d) + ":" + userId;
        logger.info("******************** 来自 redis key: "+key);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String kjid = JSON.parse(map.get("id").toString()).toString();

        //今天的 学习总时长记录
        String tKey = "kj:" + kjid + ":online";
        //上次学习时间
        String mKey = "kj:" + kjid + ":lastTime";
        //历史学习时长
        String hKey = "kj:" + kjid + ":historyonline";
        //学习课件进度记录(偏移量)
        String oKey = "kj:" + kjid + ":offset";
        //学习课件进度 是否已完成
        String kjCompleted = "kj:" + kjid + ":completed";
        //学习课件时长，新增课件时规定的
        String kjDuration = "kj:" + kjid + ":duration";

        //redis 保存map
        Map value = new HashMap();

        //判断key是否存在，若不存在则新增，存在则修改key中的值
        if(!redisUtil.hasKey(key) ||null ==redisUtil.getMapValue(key,kjDuration)){
            //不存在key,初始化
            //数据库中取 ,历史学习时长,课件规定的学习时长
            Map<String,Object> data = listenerMessage.coursewareConMapper.getStudyDuration(kjid,userId);
            if(null != data){
                //课件学习时长 (分钟)
                value.put(kjDuration,Long.parseLong(data.get("learnTime").toString()));
                //学习进度
                value.put(kjCompleted,Long.parseLong(data.get("jd").toString()));
                //历史学习时长
                value.put(hKey,Long.parseLong(data.get("historyStudyTime").toString()));

            }
            //取不到今天上次学习时间,学习时长不累加,第一次请求是看了10s之后请求
            value.put(tKey,10);

        }else{
            //redis 取上次学习时间,历史学习时长
            String lastTime = redisUtil.getMapValue(key,mKey).toString();
            Long historyStudy = Long.parseLong(redisUtil.getMapValue(key,hKey).toString());
            //课件规定的时长
            Long learnTime =Long.parseLong(redisUtil.getMapValue(key,kjDuration).toString());
            //课件学习进度
            Long jd =Long.parseLong(redisUtil.getMapValue(key,kjCompleted).toString());
            Long todayTime = 0L;
            try {
                //当前时间减去上次学习时间( 秒)
                Long time = (d.getTime() - df.parse(lastTime).getTime())/1000;
                System.out.println("上次学习时间："+lastTime+"...本次学习时长："+time);
                //时间间隔超过2分钟，不累计学习时长
                if(time <= 120){
                    todayTime = Long.parseLong(redisUtil.getMapValue(key,tKey).toString()) + time;
                    System.out.println("今天总学习时长："+todayTime);
                    value.put(tKey,todayTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            logger.info("******************** 来自 redis key---更新前学习进度: "+jd);
            //学习总时长
            Long allTime = (historyStudy + todayTime)/60;
            logger.info("******************** 来自 redis key---学习总时长: "+allTime);
            Long time_start = new Date().getTime()/1000;
            if(allTime >= learnTime && jd < 100){
//            if(allTime >= learnTime ){
                logger.info("******************** 来自 redis key---更新进度 开始了 ！！！！！ ");
                value.put(kjCompleted,100);
                //更新课件进度
                listenerMessage.coursewareConMapper.updateRate(kjid,userId);
                updateKcProgress(kjid,userId);
                updateTaskProgress(kjid,userId);
                logger.info("******************** 来自 redis key---更新进度 结束了 ！！！！！ ");
            }
            //如果课件进度==100，更新任务和课程的进度
            if(jd==100){
                updateKcProgress(kjid,userId);
                updateTaskProgress(kjid,userId);
            }
            System.out.println(time_start-new Date().getTime()/1000);
            logger.info("******************** 数据库消耗 "+ (time_start-new Date().getTime()/1000));
        }

        //记录学习时间
        String mValue = df.format(d);
        value.put(mKey,mValue);
        //学习课件进度记录
        String oValue = JSON.parse(map.get("offset").toString()).toString();
        value.put(oKey,oValue);
        logger.info("******************** 更新 redis key------- ！！！！！ ");

        redisUtil.addHash(key,value);
    }

    //更新课程进度
    public void updateKcProgress(String kjid,String xyid){
        logger.info("******************** 更新课程进度 开始了 ！！！！！ ");
        //查询课件归属哪些课程
        List<Map<String,Object>> list = listenerMessage.progressMapper.getKcContainsKj(xyid,kjid);
        if(null != list){
            list.forEach(item->{
                //获取课程进度
               Map<String,Object> map = listenerMessage.progressMapper.getKcProgress(Long.parseLong(item.get("kcid").toString()),xyid);
                Double rate = Double.parseDouble(map.get("rate").toString());
                BigDecimal bg = new BigDecimal(rate);
                rate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                if(Double.parseDouble(item.get("rate").toString()) != rate){
                    int complete = Integer.parseInt(map.get("completed").toString());
                    long time = System.currentTimeMillis();
                    //更新进度字段
                    listenerMessage.progressMapper.updateKcProgress(rate,time,complete,Long.parseLong(item.get("kcid").toString()),xyid);
                }
            });
        }
        logger.info("******************** 更新课程 end了 ！！！！！ ");
    }

    /**
     * 更新任务进度
     * @param kjid
     * @return
     */
    public void updateTaskProgress(String kjid,String xyid){
        logger.info("******************** 更新任务 开始了 ！！！！！ ");
        //查询课件归属哪些任务
        List<Map<String,Object>> list = listenerMessage.progressMapper.getTaskContainsKj(xyid,kjid);
        if(null != list){
            list.forEach(item->{
                Map<String,Object> map = listenerMessage.progressMapper.getTaskProgress(Long.parseLong(item.get("rwid").toString()),xyid);
                Double rate = Double.parseDouble(map.get("rate").toString());
                BigDecimal bg = new BigDecimal(rate);
                rate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                if(Double.parseDouble(item.get("rate").toString()) != rate){
                    int complete = Integer.parseInt(map.get("completed").toString());
                    long time = System.currentTimeMillis();
                    //更新进度字段
                    listenerMessage.progressMapper.updateTaskProgress(rate,time,complete,Long.parseLong(item.get("rwid").toString()),xyid);
                }
            });
        }
    }

}
