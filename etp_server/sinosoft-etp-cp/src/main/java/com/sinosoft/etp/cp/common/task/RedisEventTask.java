package com.sinosoft.etp.cp.common.task;

import com.sinosoft.etp.common.domain.TEventLog;
import com.sinosoft.etp.common.utils.RedisUtil;
import com.sinosoft.etp.cp.common.mapper.TEventLogMapper;
import com.sinosoft.etp.cp.courseware.mapper.TCoursewareWithCpMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 86151
 */
@Component("redisEventTask")
@EnableScheduling
public class RedisEventTask {

    @Resource
    private TEventLogMapper tEventLogMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TCoursewareWithCpMapper tCoursewareWithCpMapper;

    /**
     * 每天 一点 执行
     */
    @Scheduled(cron = "0 00 01 ? * *" )
    public void createEventLog(){
        Long len = redisUtil.getStreamLen("stream");
        //判断redis中stream消息队列长度，若长度大于10000则截取多余数据。
        int length = 10000;
        if(len.intValue()>length){
            BigDecimal bigDecimal = new BigDecimal(len);
            bigDecimal = bigDecimal.divide(BigDecimal.valueOf(5)).setScale(0,BigDecimal.ROUND_UP);
            redisUtil.trimStream("stream",bigDecimal.longValue());
        }
        //根据redis key 前缀获取对应的key集合
        Set<String> djSet = redisUtil.getListKey("user_act:");
        Set<String> zlSet = redisUtil.getListKey("user_act_ing:");
        Date day = new Date();
        //调用对应事件的归档方法
        createAct(day,djSet);
        createIng(day,zlSet);
    }

    /**
     * 点击事件数据存储
     * @return
     */
    public void createAct(Date day,Set<String>list){
        int i = 0;
        for(String key : list){
            //避免重复存储，执行新增方法时先调用删除方法
            String[] date = key.split(":");
            tEventLogMapper.delEvent(date[1]);
            //获取value列表
            Set<String> set = redisUtil.getCacheSet(key);
            for (String str : set) {
                String[] value = str.split(":");
                TEventLog tEventLog = new TEventLog();
                tEventLog.setSpecificDate(date[1]);
                //根据事件类型，给不同的参数赋值
                if(redisUtil.ACT_KC.equals(value[1])){
                    tEventLog.setKcid(value[2]==null?0:Long.parseLong(value[2]));
                    tEventLog.setUserId(Long.parseLong(value[0]));
                    tEventLog.setCreateTime(day);
                    tEventLog.setEventType(value[1]);
                }else if(redisUtil.ACT_KJ.equals(value[1])){
                    tEventLog.setKjid(Long.parseLong(value[2]));
                    tEventLog.setUserId(Long.parseLong(value[0]));
                    tEventLog.setCreateTime(day);
                    tEventLog.setEventType(value[1]);
                }else if(redisUtil.ACT_KS.equals(value[1])){
                    tEventLog.setUserId(Long.parseLong(value[0]));
                    tEventLog.setCreateTime(day);
                    tEventLog.setEventType(value[1]);
                }else if(redisUtil.ACT_RW.equals(value[1])){
                    tEventLog.setRwid(Long.parseLong(value[2]));
                    tEventLog.setUserId(Long.parseLong(value[0]));
                    tEventLog.setCreateTime(day);
                    tEventLog.setEventType(value[1]);
                }
                i += tEventLogMapper.insertEvent(tEventLog);
            }
            redisUtil.delStream(key);
        }
        System.out.println("---------------------点击事件一共新增"+ i +"条数据-----------------------------");
    }

    /**
     * 驻留事件数据存储
     * @return
     */
    public void createIng(Date day,Set<String> list){
        int i = 0;

        //把获取到的所有驻留事件根据日期排序
        List<Map> sort = timeSort(list);

        for (Map m: sort) {
            //根据冒号截取用户id
            String[] split = m.get("key").toString().split(":");
            //避免重复存储，执行新增方法时先调用删除方法
            tEventLogMapper.delEvent(split[1]);
            TEventLog tEventLog = new TEventLog();
            Map<String,Object> map = redisUtil.getHash(m.get("key").toString());
            //赋值
            tEventLog.setUserId(Long.parseLong(split[2]));
            tEventLog.setEventType(redisUtil.ACT_ING_XX);
            tEventLog.setSpecificDate(split[1]);
            //遍历hash数据，获取key和value
            Set keySet = map.keySet();
            for (Object k : keySet) {
                //根据“:”分割key，判断并将符合的value赋值给TEventLog
                String[] data = k.toString().split(":");
                if("lastTime".equals(data[2])){
                    try {
                        Date lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get(k).toString());
                        tEventLog.setTriggerTime(lastTime);
                        } catch (ParseException e) {
                        e.printStackTrace();
                        }
                    tEventLog.setKjid(Long.parseLong(data[1]));
                    }else if("offset".equals(data[2])){
                        tEventLog.setOffset(map.get(k)==null?null:map.get(k).toString());
                    }else if("online".equals(data[2])){
                        tEventLog.setStudyDuration(map.get(k).toString());
                        Map setMap = new HashMap();
                        setMap.put("historyStudyTime",map.get(k).toString());
                        setMap.put("kjid",data[1]);
                        setMap.put("xyid",split[2]);
                        int a = tCoursewareWithCpMapper.updateTCoursewareByStudent(setMap);
                        System.out.println("传输参数为:"+map.get(k)+","+data[1]+","+split[2]+","+"添加历史学习时长："+a+"条数据");
                    }
                }
            tEventLog.setCreateTime(day);
            i += tEventLogMapper.insertEvent(tEventLog);
            redisUtil.delStream(m.get("key").toString());
        }
        System.out.println("---------------------驻留事件一共新增"+ i +"条数据-----------------------------");
    }

    /**
     * 排序方法，将乱序的redis key 根据其中的日期进行排序
     * @param list
     * @return
     */
    public List<Map> timeSort(Set<String> list){
        List<Map> maps = new ArrayList<>();
        Map map = new HashMap();
        for (String a: list) {
            String[] aa = a.split(":");
            map.put("key",a);
            map.put("value",BigDecimal.valueOf(Long.parseLong(aa[1])));
            maps.add(map);
        }
        List<Map> sorts = maps.stream().sorted(Comparator.comparing(h->((BigDecimal)h.get("value")))).collect(Collectors.toList());

        //int[] sort = new int[list.size()];
        //int num = 0;
        //for (String time: list) {
        //    String[] time1 = time.split(":");
        //    sort[num] = Integer.parseInt(time1[1]);
        //    num++;
        //}
        //Arrays.sort(sort);
        //System.out.println(sort);
        //String[] list1 = new String[sort.length];
        //for (String key: list) {
        //    for (int j = 0; j < sort.length; j++) {
        //        if (key.contains(String.valueOf(sort[j]))){
        //            list1[j] = key;
        //        }
        //    }
        //}
        //
        //System.out.println(list1);
        return sorts;

    };
}
