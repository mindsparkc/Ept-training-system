package com.sinosoft.etp.cp.questionnaire.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.constant.ETPConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.bean.BeanUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.etp.cp.common.service.CommonServiceWithCp;
import com.sinosoft.etp.cp.questionnaire.domain.QuestionnaireTask;
import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireStudentWithCp;
import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireWithCp;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireCompanyWithCpMapper;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireStudentWithCpMapper;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireTaskMapper;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireWithCpMapper;
import com.sinosoft.etp.cp.questionnaire.service.ITQuestionnaireStudentWithCpService;
import com.sinosoft.etp.cp.questionnaire.service.ITQuestionnaireWithCpService;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 问卷调查Service业务层处理
 *
 *
 * @date 2022-02-14
 */
@Service
public class TQuestionnaireWithCpServiceImpl implements ITQuestionnaireWithCpService
{
    @Autowired
    private TQuestionnaireWithCpMapper tQuestionnaireMapper;

    @Resource
    private TQuestionnaireCompanyWithCpMapper tQuestionnaireCompanyMapper;

    @Resource
    private ITTagWithCpService iTTagService;


    @Resource
    private ITQuestionnaireStudentWithCpService itQuestionnaireStudentService;
    @Resource
    private CommonServiceWithCp commonService;
    @Resource
    private TQuestionnaireStudentWithCpMapper tQuestionnaireStudentMapper;

    @Resource
    private TQuestionnaireTaskMapper tQuestionnaireTaskMapper;


    /**
     * 查询问卷调查
     *
     * @param id 问卷调查ID
     * @return 问卷调查
     */
    @Override
    public TQuestionnaireWithCp selectTQuestionnaireById(Long id)
    {
        TQuestionnaireWithCp tQuestionnaire = tQuestionnaireMapper.selectTQuestionnaireById(id);
        tQuestionnaire.setSysUsers(tQuestionnaireStudentMapper.selectBysId(id));
        return tQuestionnaire;
    }

    /**
     * 查询问卷调查列表
     *
     * @param tQuestionnaire 问卷调查
     * @return 问卷调查
     */
    @Override
    @DataScope(deptAlias = "d") //bug 部门表关联已删除
    @EtpDataScope(alias = "tq")
    public List<TQuestionnaireWithCp> selectTQuestionnaireList(TQuestionnaireWithCp tQuestionnaire)
    {
        return tQuestionnaireMapper.selectTQuestionnaireList(tQuestionnaire);
    }

    /**
     * 新增问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertTQuestionnaire(TQuestionnaireWithCp tQuestionnaire)
    {
        EtpDataScopeUtils.setDataScope(tQuestionnaire);// 20250520 lyd etp数据权限相关字段

        tQuestionnaire.setCreateTime(DateUtils.getNowDate());
        tQuestionnaireMapper.insertTQuestionnaire(tQuestionnaire);

        // 问卷与企业关联表
        //TQuestionnaireCompanyWithCp tQuestionnaireCompany = new TQuestionnaireCompanyWithCp();
        //tQuestionnaireCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tQuestionnaireCompany.setWjid(tQuestionnaire.getId());
        //tQuestionnaireCompanyMapper.insertTQuestionnaireCompany(tQuestionnaireCompany);

        // 标签
        if (tQuestionnaire.getParams().get("tagStr") != null){
            String tagStr = tQuestionnaire.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tQuestionnaire.getId(),tagStr,"WJ");

        }

        // 指定用户
        if (ETPConstants.OPENNESS_POINTER.equals(tQuestionnaire.getKfcd())){
            itQuestionnaireStudentService.insert(tQuestionnaire.getSysUsers(),tQuestionnaire.getId());
        }
        /*if (ETPConstants.OPENNESS_FULLY.equals(tQuestionnaire.getKfcd())){
            List<SysUser> currentUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentUsers,tQuestionnaire);
        }*/


        return tQuestionnaire.getId();
    }

    /**
     * 将 SysUser 转变成 TSignStudent,并插入关联表
     */
    @Transactional
    protected void transformObjectType(List<SysUser> sysUsers, TQuestionnaireWithCp tQuestionnaire){
        List<TQuestionnaireStudentWithCp> list = tQuestionnaireStudentMapper.selectBysId(tQuestionnaire.getId());
        tQuestionnaireStudentMapper.delRealtionBysId(tQuestionnaire.getId());
        if (ETPConstants.OPENNESS_FULLY.equals(tQuestionnaire.getKfcd())){ // 指定全部用户
            if (sysUsers.size() > 0){

                List<TQuestionnaireStudentWithCp> collect = sysUsers.stream().map(sysUser -> {
                    TQuestionnaireStudentWithCp student = new TQuestionnaireStudentWithCp();
                    student.setWjid(tQuestionnaire.getId());
                    student.setXyid(sysUser.getUserId());
                    BeanUtils.copyBeanProp(student, sysUser);
                    return student;
                }).collect(Collectors.toList());

                collect.forEach(item -> {
                    list.forEach( i -> {
                        System.out.println(i);
                        if (i.getXyid().equals(item.getXyid()) && i.getWjid().equals(item.getWjid())){
                            item.setWjnr(i.getWjnr());
                        }
                    });
                });
                tQuestionnaireStudentMapper.insert(collect);
            }
        }
    }

    /**
     * 修改问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTQuestionnaire(TQuestionnaireWithCp tQuestionnaire)
    {
        tQuestionnaire.setUpdateTime(DateUtils.getNowDate());
        // 删除标签
        iTTagService.deleteRelateTag(tQuestionnaire.getId(),"WJ");
        int res = tQuestionnaireMapper.updateTQuestionnaire(tQuestionnaire);
        // 标签
        if (null != tQuestionnaire.getParams().get("tagStr")){
            String tagStr = tQuestionnaire.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tQuestionnaire.getId(),tagStr,"WJ");

        }

        /*if (ETPConstants.OPENNESS_FULLY.equals(tQuestionnaire.getKfcd())){
            List<SysUser> currentUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentUsers,tQuestionnaire);
        }else {
            this.delExaminationPointStudents(tQuestionnaire.getSysUsers(),tQuestionnaire.getId());
        }*/
        this.delExaminationPointStudents(tQuestionnaire.getSysUsers(),tQuestionnaire.getId());
        return res;
    }


    /**
     * @type create
     * 2021-12-31
     * 处理调查问卷指定用户更新操作
     *   1. 先删除（逻辑删除）
     *   2. 保存进度
     *   3. 在插入
     * @param users 前端新提交的对应的指定学员
     * @param sid  活动
     */
    @Transactional
    protected void delExaminationPointStudents(List<TQuestionnaireStudentWithCp> users, Long sid){
        List<TQuestionnaireStudentWithCp> list = tQuestionnaireStudentMapper.selectBysId(sid);
        tQuestionnaireStudentMapper.delRealtionBysId(sid);
        if (null != users && users.size() > 0){
            List<TQuestionnaireStudentWithCp> collect = users.stream().map(tSignStudent -> {
                tSignStudent.setCreateBy(SecurityUtils.getUsername());
                tSignStudent.setUpdateBy(SecurityUtils.getUsername());
                tSignStudent.setWjid(sid);
                tSignStudent.setXyid(tSignStudent.getUserId());
                return tSignStudent;
            }).collect(Collectors.toList());
            collect.forEach(item -> {
                list.forEach(i -> {
                    if (i.getUserId().equals(item.getXyid()) && i.getWjid().equals(item.getWjid())){
                        item.setWjnr(i.getWjnr());
                    }
                });
            });
            tQuestionnaireStudentMapper.insert(collect);
        }
    }
    /**
     * 批量删除问卷调查
     *
     * @param ids 需要删除的问卷调查ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireByIds(Long[] ids)
    {
        return tQuestionnaireMapper.deleteTQuestionnaireByIds(ids);
    }

    /**
     * 删除问卷调查信息
     *
     * @param id 问卷调查ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireById(Long id)
    {
        return tQuestionnaireMapper.deleteTQuestionnaireById(id);
    }

    @Override
    public int back(String id) {
        return tQuestionnaireMapper.back(id);
    }

    @Override
    public int getTotalByWjid(Long wjid) {
        return tQuestionnaireStudentMapper.getTotalByWjid(wjid);
    }

    @Override
    public List<TQuestionnaireWithCp> selectTQuestionnaireStudentList(TQuestionnaireWithCp tQuestionnaire) {
        return tQuestionnaireMapper.selectTQuestionnaireStudentList(tQuestionnaire);
    }

    @Override
    public List<QuestionnaireTask> getChoseAnalysis(Long wjid) {
        return tQuestionnaireTaskMapper.getChoseAnalysis(wjid);
    }
    @Override
    public List<QuestionnaireTask> getWdAnalysis(Long wjid,String tmid) {
        return tQuestionnaireTaskMapper.getWdAnalysis(wjid,tmid);
    }
}
