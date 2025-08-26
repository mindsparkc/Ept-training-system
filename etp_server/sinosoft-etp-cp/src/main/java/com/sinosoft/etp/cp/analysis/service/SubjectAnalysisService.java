package com.sinosoft.etp.cp.analysis.service;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;

import java.util.List;

public interface SubjectAnalysisService {


    /**
     * 分析试题被哪些课程引用
     * @param sid 试题ID
     * @return List<TCurriculum>
     */
    List<TCurriculumWithCp> courseAnalysisData(String sid);

    /**
     * 分析试题被哪些考试引用
     * @param sid 试题ID
     * @return List<TExamination>
     */
    List<TExaminationWithCp> examsAnalysisData(String sid);
}
