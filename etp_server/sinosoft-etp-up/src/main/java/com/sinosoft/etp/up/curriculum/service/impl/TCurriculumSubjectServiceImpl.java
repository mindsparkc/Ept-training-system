package com.sinosoft.etp.up.curriculum.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumSubject;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumSubjectMapper;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 习题记录Service业务层处理
 *
 *
 * @date 2022-05-09
 */
@Service
public class TCurriculumSubjectServiceImpl implements ITCurriculumSubjectService
{
    @Autowired
    private TCurriculumSubjectMapper tCurriculumSubjectMapper;

    /**
     * 查询习题记录
     *
     * @param kcid 课程ID
     * @return 记录
     */
    @Override
    public TCurriculumSubject selectTCurriculumSubjectById(Long kcid,Long stid,Long xyid)
    {
        return tCurriculumSubjectMapper.selectTCurriculumSubjectById(kcid,stid,xyid);
    }

    /**
     * 查询习题记录列表
     *
     * @param tCurriculumSubject 记录
     * @return 记录
     */
    @Override
    public List<TCurriculumSubject> selectTCurriculumSubjectList(TCurriculumSubject tCurriculumSubject)
    {
        return tCurriculumSubjectMapper.selectTCurriculumSubjectList(tCurriculumSubject);
    }

    /**
     * 新增习题记录
     *
     * @param tCurriculumSubject 记录
     * @return 结果
     */
    @Override
    public int insertTCurriculumSubject(TCurriculumSubject tCurriculumSubject)
    {
        tCurriculumSubject.setCreateTime(DateUtils.getNowDate());
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        tCurriculumSubject.setXyid(xyid);
        return tCurriculumSubjectMapper.insertTCurriculumSubject(tCurriculumSubject);
    }

    /**
     * 修改习题记录
     *
     * @param tCurriculumSubject 记录
     * @return 结果
     */
    @Override
    public int updateTCurriculumSubject(TCurriculumSubject tCurriculumSubject)
    {
        tCurriculumSubject.setUpdateTime(DateUtils.getNowDate());
        return tCurriculumSubjectMapper.updateTCurriculumSubject(tCurriculumSubject);
    }

    /**
     * 批量删除习题记录
     *
     * @param ids 需要删除的记录ID
     * @return 结果
     */
    @Override
    public int deleteTCurriculumSubjectByIds(Long[] ids)
    {
        return tCurriculumSubjectMapper.deleteTCurriculumSubjectByIds(ids);
    }

    /**
     * 删除习题记录信息
     *
     * @param id 记录ID
     * @return 结果
     */
    @Override
    public int deleteTCurriculumSubjectById(Long id)
    {
        return tCurriculumSubjectMapper.deleteTCurriculumSubjectById(id);
    }
}
