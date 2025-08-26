package com.sinosoft.etp.up.home.controller;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.DeviceUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.core.domain.Exam;
import com.sinosoft.etp.common.utils.RedisUtil;
import com.sinosoft.etp.up.curriculum.domain.TCurriculum;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumMapper;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumService;
import com.sinosoft.etp.up.examination.mapper.TExaminationMapper;
import com.sinosoft.etp.up.home.mapper.HomeMapper;
import com.sinosoft.etp.up.questionnaire.service.ITQuestionnaireService;
import com.sinosoft.etp.up.sign.service.ITSignService;
import com.sinosoft.etp.up.task.mapper.TTaskMapper;
import com.sinosoft.user.exam.domain.UserExamReqQuery;
import com.sinosoft.user.exam.domain.UserSimpleExam;
import com.sinosoft.user.exam.service.IUserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/etp/home")
public class HomeController  extends BaseController {

    @Autowired
    private TTaskMapper tTaskMapper;
    @Autowired
    private TExaminationMapper tExaminationMapper;
    @Autowired
    private TCurriculumMapper tCurriculumMapper;
    @Autowired
    private ITSignService tSignService;
    @Autowired
    private ITQuestionnaireService tQuestionnaireService;
    @Autowired
    private ITCurriculumService tCurriculumService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private HomeMapper homeMapper;
    @Resource
    private IUserExamService userExamService;
    /**
     * 获取首页的任务和考试角标数量
     * @return
     */
    @GetMapping("getTaskAndExamNum")
    public AjaxResult getNum(){
        Map<String,Integer> map = new HashMap<>();
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        int taskNum = tTaskMapper.getTaskNumByXyid(xyid);
        int examNum = tExaminationMapper.getExaminationNum(xyid);
        map.put("taskNum",taskNum);
        map.put("examNum",examNum);
        return AjaxResult.success(map);
    }

    /**
     * 获取首页的任务两个
     * @return
     */
    @GetMapping("getTwoTask")
    @PreProcessField(field = "fmt")
    public AjaxResult getTwoTask(){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();

        //app端需要两个，web端需要5个，为了不改接口，判断一下哪个端调用
        String userAgent = ServletUtils.getRequest().getHeader("user-agent");
        // 【0移动、1PC端】
        int num = 2;
        if(DeviceUtil.checkAgentIsMobile(userAgent)){
            System.out.println("请求来自移动端访问>>>>>>>>>>>>>>");
        }else {
            num = 5;
        }

        List<Map<String,Object>> list = tTaskMapper.getTwoTask(xyid,num);
        for (Map<String, Object> l : list) {
            Object o = l.get("fmt");
            if (o instanceof String) {
                l.put ("fmt", ((String)o).replace("{storage.path.url}", getUrl()));
            }
        }
        return AjaxResult.success(list);
    }
    /**
     * 获取首页的两个课程
     * @return
     */
    @GetMapping("getHomeCurriculum")
    @PreProcessField(field = "fmt")
    public AjaxResult getHomeCurriculum(Long num){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        List<Map<String,Object>> list = tCurriculumMapper.getHomeCurriculum(xyid,num);
        for (Map<String, Object> l : list) {
            Object o = l.get("fmt");
            if (o instanceof String) {
                l.put ("fmt", ((String)o).replace("{storage.path.url}", getUrl()));
            }
        }
        return AjaxResult.success(list);
    }

    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

    /**
     * 获取首页活动
     * @return
     */
    @GetMapping(value = "getHomeActivite")
    public AjaxResult getHomeActivite(){
        return AjaxResult.success(tSignService.getHomeActivite());
    }
    /**
     * 获取首页问卷
     * @return
     */
    @GetMapping(value = "getHomeQuestionnaire")
    public AjaxResult getHomeQuestionnaire() {
        return AjaxResult.success(tQuestionnaireService.getHomeQuestionnaire());
    }

    /**
     * 获取热门课程 top9
     * @return
     */
    @GetMapping(value = "getHomeHotKc")
    public AjaxResult getHomeHotKc() {
        return AjaxResult.success(tCurriculumMapper.getHomeHotKc());
    }

    /**
     * 首页最近学习
     * @param tCurriculum
     * @return
     */
    @GetMapping("/studyLately")
    public TableDataInfo studyLately(TCurriculum tCurriculum)
    {
        startPage();
        List<TCurriculum> list = tCurriculumService.selectTCurriculumList(tCurriculum);

        //课件进度计算
        if(null !=list && list.size() != 0){
            Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();

            Long[] kcids =new Long[list.size()];
            for(int i=0;i <list.size();i++){
                kcids[i] = list.get(i).getId();
            }
            //获取首页最近学习课程---包含课件 列表
            List<Map<String,Object>> datalList = tCurriculumMapper.selectKcContent(xyid,kcids);
            //按课程id 分组
            Map<String, List<Map<String, Object>>> mapData = datalList.stream().collect(Collectors.groupingBy(map -> map.get("cid").toString()));

            Map<String,String> rateMap = getRateByKcid(mapData);

            list.forEach(item->{
                item.setJd(rateMap.get(item.getId().toString()));
            });

        }
        return getDataTable(list, TCurriculum.class);
    }

    public Map<String,String> getRateByKcid(Map<String, List<Map<String, Object>>> mapData ){
        Map<String,String> rateMap = new HashMap<>();
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        for(Map.Entry<String, List<Map<String, Object>>> entry : mapData.entrySet()){
            String kcid = entry.getKey();
            List<Map<String, Object>> kjList = entry.getValue();
            Double rate = 0.0;
            if( null != kjList){
                for(Map<String, Object> item: kjList){
                    //课件设置的学习时长(秒)
                    Long setLearn = Long.parseLong(item.get("learnTime").toString()) * 60;
                    //课件历史学习时长
                    Long history = Long.parseLong(item.get("history").toString());

                    String key = "user_act_ing:" + DateUtils.dateTime() + ":" + xyid;
                    //从redis 取今天学习 时长和历史学习时长
                    //今天的 学习总时长记录
                    String tKey = "kj:" + item.get("kjid") + ":online";
                    Long todayTime = 0l;
                    if(null != redisUtil.getMapValue(key,tKey)){
                        todayTime = Long.parseLong(redisUtil.getMapValue(key,tKey).toString()) ;
                    }
                    //历史学习时长
                       /* String hKey = "kj:" +  item.get("kjid") + ":historyonline";
                        Long historyStudy = Long.parseLong(redisUtil.getMapValue(key,hKey).toString());*/
                    //课件总学习时长
                    if ((history + todayTime)>setLearn){
                        rate += 100.0;
                    }else{
                        if(setLearn ==0){
                            rate += 100.0;
                        }else{
                            rate +=Math.floor((history + todayTime)* 100 /setLearn );
                        }
                    }
                }
                BigDecimal bg = new BigDecimal(rate/kjList.size());
                rate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                rateMap.put(kcid,String.valueOf(rate));
            }else{
                rateMap.put(kcid,"0");
            }
        }
        return rateMap;
    }


    /**
     * 新首页 -- 统计未完成 课程个数
     *       -- 已领取课程分数
     */
    @GetMapping("/kctjNum")
    public AjaxResult getKctjNum(){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        int wwc = tCurriculumMapper.getKcNotCompletedNum(xyid);
        int ylq = tCurriculumMapper.getKcYlqNum(xyid);
        Map<String, Integer> map = new HashMap<>() ;
        map.put("wwc",wwc);
        map.put("ylq",ylq);
        return AjaxResult.success(map);
    }
    /**
     * 新首页 -- 统计未完成 任务个数
     *       -- 任务总个数
     */
    @GetMapping("/rwtjNum")
    public AjaxResult getRwtjNum(){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        int wwc = tTaskMapper.getRwNotCompletedNum(xyid);
        int all = tTaskMapper.getRwNum(xyid);
        int jgq = tTaskMapper.getRwJgqNum(xyid);
        Map<String, Integer> map = new HashMap<>() ;
        map.put("wwc",wwc);
        map.put("all",all);
        map.put("jgq",jgq);
        return AjaxResult.success(map);
    }

    /**
     * 新首页 -- 统计 将开始 考试个数   3天内
     *       -- 将结束考试个数        3天内
     */
    @GetMapping("/kstjNum")
    public AjaxResult getKstjNum(){
        int jks = userExamService.getCountNum("jks");
        int jjs = userExamService.getCountNum("jjs");
        Map<String, Integer> map = new HashMap<>() ;
        map.put("jks",jks);
        map.put("jjs",jjs);
        return AjaxResult.success(map);
    }

    /**
     * 新首页 -- 统计获取到的证书
     */
    @GetMapping("/zstjNum")
    public AjaxResult getZstjNum(){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        int num = homeMapper.getZsNum(xyid);
        return AjaxResult.success(num);
    }

    /**
     * 新首页 -- 获取最近学习的课程
     * @return 课程信息
     */
    @GetMapping("/lastStudy")
    public AjaxResult getLastStudy(){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        return AjaxResult.success(tCurriculumMapper.getLastStudy(xyid));
    }

    /**
     * 获取即将结束的考试信息
     */
    @GetMapping("/comingEndExam")
    public AjaxResult getExamByEnd() {
        return AjaxResult.success(userExamService.getComingEnd());
    }
}
