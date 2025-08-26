package com.sinosoft.etp.up.curriculum.service;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumSubject;

import java.util.List;

/**
 * 记录Service接口
 *
 *
 * @date 2022-05-09
 */
public interface ITCurriculumSubjectService
{
    /**
     * 查询习题记录
     *
     * @param kcid 课程ID
     * @param stid 试题ID
     * @param xyid 学员ID
     * @return 记录
     */
    public TCurriculumSubject selectTCurriculumSubjectById(Long kcid,Long stid,Long xyid);

    /**
     * 查询习题记录列表
     *
     * @param tCurriculumSubject 记录
     * @return 记录集合
     */
    public List<TCurriculumSubject> selectTCurriculumSubjectList(TCurriculumSubject tCurriculumSubject);

    /**
     * 新增习题记录
     *
     * @param tCurriculumSubject 记录
     * @return 结果
     */
    public int insertTCurriculumSubject(TCurriculumSubject tCurriculumSubject);

    /**
     * 修改习题记录
     *
     * @param tCurriculumSubject 记录
     * @return 结果
     */
    public int updateTCurriculumSubject(TCurriculumSubject tCurriculumSubject);

    /**
     * 批量删除习题记录
     *
     * @param ids 需要删除的记录ID
     * @return 结果
     */
    public int deleteTCurriculumSubjectByIds(Long[] ids);

    /**
     * 删除习题记录信息
     *
     * @param id 记录ID
     * @return 结果
     */
    public int deleteTCurriculumSubjectById(Long id);
}
