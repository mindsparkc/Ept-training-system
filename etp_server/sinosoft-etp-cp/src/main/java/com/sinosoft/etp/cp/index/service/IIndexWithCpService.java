package com.sinosoft.etp.cp.index.service;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.opinion.domain.TOpinionWithCp;
import com.sinosoft.system.domain.SysNotice;

import java.util.List;
import java.util.Map;

public interface IIndexWithCpService {
    /**
     * 首页统计
     * @return
     */
    Map<String, Object> getTotal();

    /**
     * 当前课程
     * @return
     */
    List<TCurriculumWithCp> getCurrentCourse();

    /**
     * 课件
     * @return
     */
    List<TCoursewareWithCp> getCurrentCourseware();

    /**
     * 当前考试
     */
    List<TExaminationWithCp> getCurrentExaminations();


    List<SysNotice> getNotices();

    /** 本企业学员待反馈意见*/
    List<TOpinionWithCp> getOpinions(Long deptId);
}
