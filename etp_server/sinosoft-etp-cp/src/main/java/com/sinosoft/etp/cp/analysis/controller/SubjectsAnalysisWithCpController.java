package com.sinosoft.etp.cp.analysis.controller;

import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.etp.cp.analysis.service.SubjectAnalysisService;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/etp/subjectsAnalysisWithCp")
public class SubjectsAnalysisWithCpController extends BaseController {


    @Resource
    private SubjectAnalysisService subjectAnalysisService;

    @GetMapping("/coursesAndExams/{sid}")
    public AjaxResult AnalysisCoursesAndExams(@PathVariable("sid") String sid){

        Map<Object,Object> res = new HashMap<>();
        CompletableFuture<List<TCurriculumWithCp>> coursesFuture = CompletableFuture.supplyAsync(() -> {
            return subjectAnalysisService.courseAnalysisData(sid);
        });
        CompletableFuture<List<TExaminationWithCp>> examsFuture = CompletableFuture.supplyAsync(() -> {
            return subjectAnalysisService.examsAnalysisData(sid);
        });
        try {
            // 分页
            startPage();
            List<TCurriculumWithCp> tCurricula = coursesFuture.get();
            startPage();
            List<TExaminationWithCp> tExaminations = examsFuture.get();

            TableDataInfo tCurriculaTable = getDataTable(tCurricula);
            TableDataInfo tExaminationsTable = getDataTable(tExaminations);

            res.put("tCurriculaTable",tCurriculaTable);
            res.put("tExaminationsTable",tExaminationsTable);


        }catch (Exception e){
            res.put("tCurriculaTable",new TableDataInfo());
            res.put("tExaminationsTable",new TableDataInfo());
            logger.error(e.getMessage());
        }
        return AjaxResult.success(res);
    }


}
