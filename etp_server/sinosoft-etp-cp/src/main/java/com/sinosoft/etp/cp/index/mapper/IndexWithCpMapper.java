package com.sinosoft.etp.cp.index.mapper;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.opinion.domain.TOpinionWithCp;
import com.sinosoft.system.domain.SysNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IndexWithCpMapper {

    /**
     * 统计课件数量
     */
    Map<String, Object> getCourseTotal();

    /**
     * 统计课程数量
     */
    Map<String, Object> getCurriculumTotal();

    /**
     * 统计试题数量
     */
    Map<String, Object> getSubjectTotal();

    /**
     * 统计试卷数量
     */
    Map<String, Object> getPaperTotal();

    /**
     * 当前课程
     * */
    List<TCurriculumWithCp> getCurrentCourse();

    /**
     * 课件
     */
    List<TCoursewareWithCp> getCurrentCourseware();

    /**
     * 当前考试
     */
    List<TExaminationWithCp> getCurrentExaminations();

    /**
     * 通知公告
     * @return
     */
    List<SysNotice> getNotices();

    /**
     * 本部门学员待反馈意见
     * @return
     */
    List<TOpinionWithCp> getOpinions(@Param("deptId")Long deptId);
}
