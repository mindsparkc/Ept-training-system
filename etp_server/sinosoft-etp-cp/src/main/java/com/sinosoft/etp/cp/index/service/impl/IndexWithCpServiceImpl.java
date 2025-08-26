package com.sinosoft.etp.cp.index.service.impl;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.index.mapper.IndexWithCpMapper;
import com.sinosoft.etp.cp.index.service.IIndexWithCpService;
import com.sinosoft.etp.cp.opinion.domain.TOpinionWithCp;
import com.sinosoft.system.domain.SysNotice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexWithCpServiceImpl implements IIndexWithCpService {

    @Resource
    private IndexWithCpMapper indexMapper;

    @Override
    public Map<String, Object> getTotal() {
        Map<String, Object> res  = new HashMap<>(4);
        // 课件
        res.put("CourseTotal",indexMapper.getCourseTotal());
        // 课程
        res.put("CurriculumTotal",indexMapper.getCurriculumTotal());
        // 试题
        res.put("SubjectTotal",indexMapper.getSubjectTotal());
        // 试卷
        res.put("PaperTotal",indexMapper.getPaperTotal());
        return res;
    }


    @Override
    public List<TCurriculumWithCp> getCurrentCourse() {
        return indexMapper.getCurrentCourse();
    }

    @Override
    public List<TCoursewareWithCp> getCurrentCourseware() {
        return indexMapper.getCurrentCourseware();
    }

    @Override
    public List<TExaminationWithCp> getCurrentExaminations() {
        return indexMapper.getCurrentExaminations();
    }

    @Override
    public List<SysNotice> getNotices() {
        return indexMapper.getNotices();
    }

    @Override
    public List<TOpinionWithCp> getOpinions(Long deptId) {
        return indexMapper.getOpinions(deptId);
    }
}
