package com.sinosoft.etp.cp.analysis.controller;

import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.analysis.domain.DeptAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.service.ITExaminationScoreAnaltsisService;
import com.sinosoft.etp.cp.examination.service.ITExaminationWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考试Controller
 *
 *
 * @date 2021-12-24
 */
@RestController
@RequestMapping("/etp/examinationScoreAnalysisWithCp")
public class TExaminationScoreAnalysisWithCpController extends BaseController
{
    @Autowired
    private ITExaminationWithCpService tExaminationService;

    @Autowired
    private ITExaminationScoreAnaltsisService tTExaminationScoreAnaltsisService;

    ///**
    // * 展示考试列表
    // */
    //@PreAuthorize("@ss.hasPermi('etp:examinationScoreAnalysis:list')")
    //@GetMapping("/list")
    //public TableDataInfo list(TExaminationScoreAnalysisWithCp tExaminationScoreAnalysis)
    //{
    //    startPage();
    //    String tags = tExaminationScoreAnalysis.getTags();
    //    if(StringUtils.isNotEmpty(tags)){
    //        tExaminationScoreAnalysis.getParams().put("tags", tags.split(","));
    //        tExaminationScoreAnalysis.getParams().put("tagsCount", tags.split(",").length);
    //    }
    //    List<TExaminationScoreAnalysisWithCp> list = tTExaminationScoreAnaltsisService.selectTExaminationList(tExaminationScoreAnalysis);
    //    return getDataTable(list);
    //}


    /**
     * 展示考试部门关联列表
     */
    @PreAuthorize("@ss.hasPermi('etp:examinationScoreAnalysis:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeptAndExaminationWithCp deptAndExaminationWithCp)
    {
        startPage();
        List<DeptAndExaminationWithCp> list = tTExaminationScoreAnaltsisService.selectTExaminationByDeptList(deptAndExaminationWithCp);
        return getDataTable(list);
    }


    /**
     * 获取考试详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
//        todo
//         实考人数：暂时无法获取确切的实考人数
//          参考率：根据实考人数和应考人数来计算
//          及格率：因为考试的自由度比较高，考卷的及格分数无法确定，暂时无法计算“及格率”
//          平均时长：考试没有记录考试的时长，无法计算平均时长。
//          获取考试成绩及分析结果,
//        此接口应返回数据，首先以考试分组，其次以部门分组：部门、应考人数、实考人数、参考率、及格率、最高分、最低分、平均分、平均时长
        Long dept_id = SecurityUtils.getLoginUser().getUser().getDeptId(); //管理员用户的部门ID，

        return AjaxResult.success(tTExaminationScoreAnaltsisService.selectTExaminationById(id));
    }

    @GetMapping(value = "/getSubjectInfo/{sjid}")
    public AjaxResult getSubjectInfo(@PathVariable("sjid")Long sjid){
        return AjaxResult.success(tTExaminationScoreAnaltsisService.getSubjectInfo(sjid));
    }

    @GetMapping(value = "/getSubjectAnalysis/{id}/{ksid}/{lx}")
    public AjaxResult getSubjectAnalysis(@PathVariable("id")Long id,@PathVariable("ksid")Long ksid,@PathVariable("lx")String lx){
        return AjaxResult.success(tTExaminationScoreAnaltsisService.getSubjectAnalysis(id,ksid,lx));
    }

}
