package com.sinosoft.etp.up.task.service.impl;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.progress.mapper.ProgressMapper;
import com.sinosoft.etp.common.utils.RedisUtil;
import com.sinosoft.etp.up.courseware.domain.TCoursewareStudent;
import com.sinosoft.etp.up.courseware.mapper.TCoursewareStudentMapper;
import com.sinosoft.etp.up.task.domain.TTask;
import com.sinosoft.etp.up.task.domain.TTaskCatalog;
import com.sinosoft.etp.up.task.mapper.TTaskContentMapper;
import com.sinosoft.etp.up.task.mapper.TTaskMapper;
import com.sinosoft.etp.up.task.mapper.TTaskStudentMapper;
import com.sinosoft.etp.up.task.service.ITTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务Service业务层处理
 *
 *
 * @date 2021-12-08
 */
@Service
public class TTaskServiceImpl implements ITTaskService
{
    @Autowired
    private TTaskMapper tTaskMapper;

    @Resource
    private TTaskContentMapper tTaskContentMapper;

    @Resource
    private TCoursewareStudentMapper tCoursewareStudentMapper;

    @Resource
    private ProgressMapper progressMapper;

    @Resource
    private TTaskStudentMapper tTaskStudentMapper;

    @Resource
    private RedisUtil redisUtil;
    /**
     * 查询任务列表
     *
     * @param tTask 任务
     * @return 任务
     */
    @DataScope(deptAlias = "d")
    @Override
    public List<TTask> selectTTaskList(TTask tTask)
    {
        tTask.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        //tTask.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        return tTaskMapper.selectTTaskList(tTask);
    }

    /**
     * 查询任务9
     *
     * @param id 任务ID
     * @return 任务
     */
    @Override
    public TTaskCatalog selectTTaskById(Long id)
    {
        //点击事件存储
        Map<String,Object> message = new HashMap<>();
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        message.put("eventType","CLI_TASK");
        message.put("value",id.toString());
        message.put("userId",userId.toString());
        redisUtil.addStream("stream",message);

//        TTask tTask = tTaskMapper.selectTTaskById(id);
        TTaskCatalog catalog = tTaskContentMapper.selectTTaskContentListByTaskId(id);
//        tTask.setTaskContentList(list);
//         查询任务关联的用户
//        tTask.setSysUsers(tTaskStudentMapper.selectByKcId(id));
        return catalog;
    }

    /**
     * 任务详情
     * @param id
     * @return
     */
    public TTask selectTTaskInfoById(Long id){
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        TTask tTask = tTaskMapper.selectTTaskById(id,xyid);
        return tTask;
    }

    @Override
    public int getTaskNumByXyid(Long xyid) {
        return tTaskMapper.getTaskNumByXyid(xyid);
    }

    @Override
    @Transactional
    public Double updateTaskStatus(Long rwid,Long kjid) {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();

        //1.更新课件进度，
        TCoursewareStudent data = tCoursewareStudentMapper.selectTCoursewareStudentById(kjid,xyid);
        if(null==data){
            TCoursewareStudent tCoursewareStudent = new TCoursewareStudent();
            tCoursewareStudent.setKjid(kjid);
            tCoursewareStudent.setXyid(xyid);
            tCoursewareStudent.setJd("{}");
            tCoursewareStudent.setDelFlag("0");
            tCoursewareStudentMapper.insertTCoursewareStudent(tCoursewareStudent);
        }
        //2-更新当前任务进度,只更新当前看到那个课件
        tTaskStudentMapper.updateTaskJd(xyid,rwid,kjid);
       /* Double rate =0.0;
        ProgressVo jdInfo = JSONObject.parseObject(JSON.toJSONString(progressMapper.getTaskProgress(rwid)),ProgressVo.class);
        if(null != jdInfo){
            rate = jdInfo.getRate();
            BigDecimal bg = new BigDecimal(rate);
            rate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            jdInfo.setRate(rate*100);
            JSONObject json = new JSONObject();
            json.put("current",kjid);
            json.put("source_type","kj");
            jdInfo.setLastStudy(json);
            TTaskStudent tTaskStudent =new TTaskStudent();
            tTaskStudent.setXyid(xyid);
            tTaskStudent.setRwid(rwid);
            tTaskStudent.setJd(JSONObject.toJSONString(jdInfo));
            tTaskStudentMapper.updateTTaskStudent(tTaskStudent);
        }*/
        return 0.0;
    }

    @Override
    public List<Map<String, Object>> getTaskTags() {
        //Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();
        return tTaskMapper.getTaskTags();
    }


}
