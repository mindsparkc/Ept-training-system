package com.sinosoft.etp.cp.analysis.service.impl;

import com.sinosoft.etp.cp.analysis.mapper.SubjectAnalysisWithCpMapper;
import com.sinosoft.etp.cp.analysis.service.SubjectAnalysisService;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubjectAnalysisServiceImpl implements SubjectAnalysisService {

    @Resource
    private SubjectAnalysisWithCpMapper subjectAnalysisMapper;

    @Override
    public List<TCurriculumWithCp> courseAnalysisData(String sid) {
        return subjectAnalysisMapper.courseAnalysisData(sid);
    }

    @Override
    public List<TExaminationWithCp> examsAnalysisData(String sid) {
        return subjectAnalysisMapper.examsAnalysisData(sid);
    }
}
