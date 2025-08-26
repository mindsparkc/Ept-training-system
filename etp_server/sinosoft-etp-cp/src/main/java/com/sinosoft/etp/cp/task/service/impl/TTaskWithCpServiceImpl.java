package com.sinosoft.etp.cp.task.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.constant.ETPConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.bean.BeanUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.etp.cp.common.service.CommonServiceWithCp;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import com.sinosoft.etp.cp.task.domain.TTaskContentWithCp;
import com.sinosoft.etp.cp.task.domain.TTaskStudentWithCp;
import com.sinosoft.etp.cp.task.domain.TTaskWithCp;
import com.sinosoft.etp.cp.task.mapper.TTaskCompanyWithCpMapper;
import com.sinosoft.etp.cp.task.mapper.TTaskContentWithCpMapper;
import com.sinosoft.etp.cp.task.mapper.TTaskStudentWithCpMapper;
import com.sinosoft.etp.cp.task.mapper.TTaskWithCpMapper;
import com.sinosoft.etp.cp.task.service.ITTaskWithCpService;
import com.sinosoft.etp.cp.task.service.ITaskStudentWithCpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务Service业务层处理
 *
 *
 * @date 2021-12-08
 */
@Service
public class TTaskWithCpServiceImpl implements ITTaskWithCpService
{
    @Resource
    private TTaskWithCpMapper tTaskMapper;

    @Resource
    private TTaskCompanyWithCpMapper tTaskCompanyMapper;

    @Resource
    private ITTagWithCpService iTTagService;

    @Resource
    private TTaskContentWithCpMapper tTaskContentMapper;

    @Resource
    private ITaskStudentWithCpService iTaskStudentService;

    @Resource
    private TTaskStudentWithCpMapper tTaskStudentMapper;

    @Resource
    private CommonServiceWithCp commonService;

    /**
     * 查询任务
     *
     * @param id 任务ID
     * @return 任务
     */
    @Override
    public TTaskWithCp selectTTaskById(Long id)
    {
        TTaskWithCp tTask = tTaskMapper.selectTTaskById(id);
        List<TTaskContentWithCp> list = tTaskContentMapper.selectTTaskContentListByTaskId(id);
        tTask.setTaskContentList(list);
        tTask.setSysUsers(tTaskStudentMapper.selectByKcId(id));
        return tTask;
    }

    /**
     * 查询任务列表
     *
     * @param tTask 任务
     * @return 任务
     */
    @DataScope(deptAlias = "d") //错误用法 别名错误
    @EtpDataScope(alias = "t")
    @Override
    public List<TTaskWithCp> selectTTaskList(TTaskWithCp tTask)
    {
        return tTaskMapper.selectTTaskList(tTask);
    }

    /**
     * 新增任务
     *
     * @param tTask 任务
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertTTask(TTaskWithCp tTask)
    {
        // 修改
        if (null != tTask.getId()){
            tTask.setUpdateTime(DateUtils.getNowDate());
            tTask.setStates("0");
            this.updateTTask(tTask);
        }else {
            EtpDataScopeUtils.setDataScope(tTask);// 20250520 lyd etp数据权限相关字段

            tTask.setCreateTime(DateUtils.getNowDate());
            tTaskMapper.insertTTask(tTask);
            // 设置任务与企业关联关系
            //TTaskCompanyWithCp taskCompany = new TTaskCompanyWithCp();
            //taskCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
            //taskCompany.setRwid(tTask.getId());
            //taskCompany.setCreateBy(SecurityUtils.getUsername());
            //taskCompany.setCreateTime(DateUtils.getNowDate());
            //tTaskCompanyMapper.insertTTaskCompany(taskCompany);
            //插入标签关联表
            if (tTask.getParams().get("tagStr") != null) {
                String tagStr = tTask.getParams().get("tagStr").toString();
                iTTagService.commpanyInsertTag(tagStr);
                iTTagService.insertRelateTag(tTask.getId(),tagStr,"RW");
            }

            /*if (ETPConstants.OPENNESS_FULLY.equals(tTask.getKfcd())){
                List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
                transformObjectType(currentCompanyUsers,tTask);
            }*/
            // 插入课程与学员关联关系 1表示指定用户
            if (ETPConstants.OPENNESS_POINTER.equals(tTask.getKfcd())){
                iTaskStudentService.insert(tTask.getSysUsers(),tTask.getId());
            }
        }
        return tTask.getId();
    }

    /**
     * 修改任务
     *
     * @param tTask 任务
     * @return 结果
     */
    @Override
    public int updateTTask(TTaskWithCp tTask)
    {
        tTask.setUpdateTime(DateUtils.getNowDate());
        // 删除关联关系-标签
        iTTagService.deleteRelateTag(tTask.getId(),"RW");
        int res = tTaskMapper.updateTTask(tTask);

        //插入课程标签关联表
        if (null != tTask.getParams().get("tagStr")){
            String tagStr = tTask.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tTask.getId(),tTask.getParams().get("tagStr").toString(),"RW");
        }

        this.delTaskPointStudents(tTask.getSysUsers(),tTask.getId());
        /*if (ETPConstants.OPENNESS_FULLY.equals(tTask.getKfcd())){
            List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentCompanyUsers,tTask);
        }*/
        if(ETPConstants.OPENNESS_POINTER.equals(tTask.getKfcd())){
            iTaskStudentService.insert(tTask.getSysUsers(),tTask.getId());
        }


        return res;
    }


    /**
     * 将 SysUser 转变成 TCurriculumStudent,并插入关联表
     */
    @Transactional
    protected void transformObjectType(List<SysUser> sysUsers, TTaskWithCp task){
        List<TTaskStudentWithCp> list = tTaskStudentMapper.selectByKcId(task.getId());
        tTaskStudentMapper.delRealtionByKcId(task.getId());
        if (ETPConstants.OPENNESS_FULLY.equals(task.getKfcd())){ // 指定全部用户
            if (sysUsers.size() > 0){

                List<TTaskStudentWithCp> collect = sysUsers.stream().map(sysUser -> {
                    TTaskStudentWithCp student = new TTaskStudentWithCp();
                    student.setRwid(task.getId());
                    student.setXyid(sysUser.getUserId());
                    BeanUtils.copyBeanProp(student, sysUser);
                    return student;
                }).collect(Collectors.toList());

                collect.forEach(item -> {
                    list.forEach( i -> {
                        System.out.println(i);
                        if (i.getXyid().equals(item.getXyid()) && i.getRwid().equals(item.getRwid())){
                            item.setJd(i.getJd());
                        }
                    });
                });
                tTaskStudentMapper.insert(collect);
            }
        }
    }

    /**
     * 处理任务指定用户更新操作
     *   1. 先删除
     *   2. 保存进度
     *   3. 在插入
     * @param users 前端新提交的对应的指定学员
     * @param rwid  任务ID
     */
    @Transactional
    protected void delTaskPointStudents(List<TTaskStudentWithCp> users, Long rwid){
        List<TTaskStudentWithCp> list = tTaskStudentMapper.selectByKcId(rwid);
        tTaskStudentMapper.delRealtionByKcId(rwid);
        if (users.size() > 0){

            List<TTaskStudentWithCp> collect = users.stream().map(student -> {
                student.setCreateBy(SecurityUtils.getUsername());
                student.setUpdateBy(SecurityUtils.getUsername());
                student.setRwid(rwid);
                student.setXyid(student.getUserId());
                return student;
            }).collect(Collectors.toList());
            collect.forEach(item -> {
                list.forEach(i -> {
                    if (i.getUserId().equals(item.getXyid()) && i.getRwid().equals(item.getRwid())){
                        item.setJd(i.getJd());
                    }
                });
            });
            tTaskStudentMapper.insert(collect);
        }
    }

    /**
     * 批量删除任务
     *
     * @param ids 需要删除的任务ID
     * @return 结果
     */
    @Override
    public int deleteTTaskByIds(Long[] ids)
    {
        return tTaskMapper.deleteTTaskByIds(ids);
    }

    /**
     * 删除任务信息
     *
     * @param id 任务ID
     * @return 结果
     */
    @Override
    public int deleteTTaskById(Long id)
    {
        return tTaskMapper.deleteTTaskById(id);
    }

    @Override
    @Transactional
    public Long completeTask(TTaskWithCp tTask) {
        int res = 0;
        this.delTaskPointStudents(tTask.getSysUsers(),tTask.getId());
        tTask.setUpdateTime(DateUtils.getNowDate());
        this.updateTTask(tTask);

        // 把之前的任务内容删除
        tTaskContentMapper.deleteTTaskContentById(tTask.getId());
        // 插入任务内容项（课程、考试）信息
        List<TTaskContentWithCp> collect = tTask.getTaskContentList().stream().peek(tTaskContent -> {
            tTaskContent.setRwid(tTask.getId());
            tTaskContent.setSid(tTaskContent.getId());
            tTaskContent.setId(null);
        }).collect(Collectors.toList());
        for (TTaskContentWithCp item : collect) {
            res += tTaskContentMapper.insertTTaskContent(item);
        }
        return tTask.getId();
    }

    @Override
    public void pushBack(Long id) {
        tTaskMapper.pushBack(id);
    }

    @Override
    public boolean checkExistRW(TTaskWithCp task) {
        Long id = StringUtils.isNull(task.getId()) ? -1L : task.getId();
        TTaskWithCp result = tTaskMapper.checkExistRW(task.getMc());
        if(StringUtils.isNotNull(result) && result.getId().longValue()!=id.longValue() ){
            return true;
        }
        return false;
    }

}
