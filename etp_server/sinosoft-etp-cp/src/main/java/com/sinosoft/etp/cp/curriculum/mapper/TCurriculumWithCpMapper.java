package com.sinosoft.etp.cp.curriculum.mapper;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * curriculumMapper接口
 *
 *
 * @date 2021-11-15
 */
public interface TCurriculumWithCpMapper
{
    /**
     * 查询curriculum
     *
     * @param id curriculumID
     * @return curriculum
     */
    public TCurriculumWithCp selectTCurriculumById(Long id);

    /**
     * 查询curriculum列表
     *
     * @param tCurriculum curriculum
     * @return curriculum集合
     */
    public List<TCurriculumWithCp> selectTCurriculumList(TCurriculumWithCp tCurriculum);

    /**
     * 新增curriculum
     *
     * @param tCurriculum curriculum
     * @return 结果
     */
    public int insertTCurriculum(TCurriculumWithCp tCurriculum);

    /**
     * 修改curriculum
     *
     * @param tCurriculum curriculum
     * @return 结果
     */
    public int updateTCurriculum(TCurriculumWithCp tCurriculum);

    /**
     * 修改推荐状态
     * @param tCurriculum
     * @return
     */
    public int updateTCurriculumTjzt(TCurriculumWithCp tCurriculum);

    /**
     * 删除curriculum
     *
     * @param id curriculumID
     * @return 结果
     */
    public int deleteTCurriculumById(Long id);

    /**
     * 批量删除curriculum
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCurriculumByIds(Long[] ids);

    /**
     * 退回已发布的课程
     * @param id
     */
    void pushBack(@Param("id") Long id);

    /**
     * 发布课程
     * @param id
     */
    void push(@Param("id") Long id);

    public TCurriculumWithCp checkExistKC(@Param("mc") String mc);

    public int getKcLearnTime(@Param("kcid") Long kcid);

    public List<Map<String,Object>> getKcListByKjTime(@Param("kcid") Long kcid);
    public List<Map<String,Object>> getEveKcLearnTime();
}
