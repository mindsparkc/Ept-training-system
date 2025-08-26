package com.sinosoft.etp.up.curriculum.mapper;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TCurriculumStudentMapper {

    /**
     * 新增curriculum
     *
     * @param tCurriculumStudent
     * @return 结果
     */
    public int insertTCurriculumStudent(TCurriculumStudent tCurriculumStudent);
    public int countJoinCurriculumById(Long id);

    public int countCurriculumById(@Param("xyid") Long xyid,@Param("cid") Long cid);

    /**
     * 获取包含cid的任务
     * @param xyid
     * @param cid
     * @return
     */
    public List<Long> getTaskContainCurriculum(@Param("xyid") Long xyid,@Param("cid") Long cid);

    public Long getInitCourseware(@Param("xyid") Long xyid,@Param("cid") Long cid);

    /**
     * 修改
     *
     * @param tCurriculumStudent 【请填写功能名称】
     * @return 结果
     */
    public int updateTCurriculumStudent(TCurriculumStudent tCurriculumStudent);

    public int updateCurriculumJd(@Param("xyid") Long xyid,@Param("kcid") Long kcid,@Param("current") Long current);
}
