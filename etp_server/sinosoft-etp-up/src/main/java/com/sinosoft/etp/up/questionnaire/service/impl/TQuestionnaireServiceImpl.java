package com.sinosoft.etp.up.questionnaire.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaire;
import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireStudent;
import com.sinosoft.etp.up.questionnaire.mapper.TQuestionnaireMapper;
import com.sinosoft.etp.up.questionnaire.mapper.TQuestionnaireStudentMapper;
import com.sinosoft.etp.up.questionnaire.service.ITQuestionnaireService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 问卷调查Service业务层处理
 *
 *
 * @date 2022-02-16
 */
@Service
public class TQuestionnaireServiceImpl implements ITQuestionnaireService
{
    @Resource
    private TQuestionnaireMapper tQuestionnaireMapper;

    @Resource
    private TQuestionnaireStudentMapper tQuestionnaireStudentMapper;

    /**
     * 查询问卷调查
     *
     * @param id 问卷调查ID
     * @return 问卷调查
     */
    @Override
    public TQuestionnaire selectTQuestionnaireById(Long id)
    {
        return tQuestionnaireMapper.selectTQuestionnaireById(id,SecurityUtils.getLoginUser().getUser().getUserId());
    }

    /**
     * 查询问卷调查列表
     *
     * @param tQuestionnaire 问卷调查
     * @return 问卷调查
     */
    @EtpDataScope(alias = "tq")
    @Override
    public List<TQuestionnaire> selectTQuestionnaireList(TQuestionnaire tQuestionnaire)
    {
        //获取当前时间
        Date now = DateUtils.getNowDate();
        tQuestionnaire.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        List<TQuestionnaire> result = tQuestionnaireMapper.selectTQuestionnaireList(tQuestionnaire);
        return result;
    }

    /**
     * 新增问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    @Override
    public int insertTQuestionnaire(TQuestionnaire tQuestionnaire)
    {
        tQuestionnaire.setCreateTime(DateUtils.getNowDate());
        return tQuestionnaireMapper.insertTQuestionnaire(tQuestionnaire);
    }

    /**
     * 修改问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    @Override
    public int updateTQuestionnaire(TQuestionnaire tQuestionnaire)
    {
        TQuestionnaireStudent tQuestionnaireStudent = new TQuestionnaireStudent();
        String kfcd = tQuestionnaire.getKfcd();
        tQuestionnaireStudent.setUpdateTime(DateUtils.getNowDate());
        tQuestionnaireStudent.setWjid(tQuestionnaire.getId());
        tQuestionnaireStudent.setWjnr(tQuestionnaire.getNr());
        tQuestionnaireStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        int row = 0;
        if("0".equals(kfcd)){
            row = tQuestionnaireStudentMapper.insertTQuestionnaireStudent(tQuestionnaireStudent);
        }else{
            row = tQuestionnaireStudentMapper.updateTQuestionnaireStudent(tQuestionnaireStudent);
        }
        return row;
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
    public Map<String, Object> getHomeQuestionnaire() {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        return tQuestionnaireMapper.getHomeQuestionnaire(xyid);
    }
}
