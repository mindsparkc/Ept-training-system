package com.sinosoft.etp.up.curriculum.mapper;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程习题记录Mapper接口
 *
 *
 * @date 2022-05-09
 */
public interface TCurriculumSubjectMapper
{
    /**
     * 查询习题记录
     *
     * @param kcid 课程ID
     * @param stid 试题ID
     * @param xyid 学员ID
     * @return 记录
     */
    public TCurriculumSubject selectTCurriculumSubjectById(@Param("kcid") Long kcid,@Param("stid") Long stid,@Param("xyid") Long xyid);

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
     * 删除习题记录
     *
     * @param id 记录ID
     * @return 结果
     */
    public int deleteTCurriculumSubjectById(Long id);

    /**
     * 批量删除习题记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCurriculumSubjectByIds(Long[] ids);
}
