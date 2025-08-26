package com.sinosoft.etp.cp.questionnaire.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.questionnaire.domain.QuestionnaireTask;
import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireWithCp;
import com.sinosoft.etp.cp.questionnaire.service.ITQuestionnaireWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 问卷调查Controller
 *
 *
 * @date 2022-02-14
 */
@RestController
@RequestMapping("/etp/questionnaireWithCp")
public class TQuestionnaireWithCpController extends BaseController
{
    @Autowired
    private ITQuestionnaireWithCpService tQuestionnaireService;

    /**
     * 查询问卷调查列表
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:list')")
    @GetMapping("/list")
    public TableDataInfo list(TQuestionnaireWithCp tQuestionnaire)
    {
        String tags = tQuestionnaire.getTags();
        if (StringUtils.isNotEmpty(tags)){
            tQuestionnaire.getParams().put("tags",tags.split(","));
            tQuestionnaire.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TQuestionnaireWithCp> list = tQuestionnaireService.selectTQuestionnaireList(tQuestionnaire);
        return getDataTable(list, TQuestionnaireWithCp.class);
    }

    /**
     * 导出问卷调查列表
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:export')")
    @Log(title = "问卷调查", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TQuestionnaireWithCp tQuestionnaire)
    {
        List<TQuestionnaireWithCp> list = tQuestionnaireService.selectTQuestionnaireList(tQuestionnaire);
        ExcelUtil<TQuestionnaireWithCp> util = new ExcelUtil<TQuestionnaireWithCp>(TQuestionnaireWithCp.class);
        return util.exportExcel(list, "问卷调查数据");
    }

    /**
     * 获取问卷调查详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TQuestionnaireWithCp>(TQuestionnaireWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tQuestionnaireService.selectTQuestionnaireById(id)));
    }

    /**
     * 新增问卷调查
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:add')")
    @Log(title = "问卷调查", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQuestionnaireWithCp tQuestionnaire)
    {
        return AjaxResult.success(tQuestionnaireService.insertTQuestionnaire(tQuestionnaire));
    }

    /**
     * 修改问卷调查
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:edit')")
    @Log(title = "问卷调查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQuestionnaireWithCp tQuestionnaire)
    {
        return toAjax(tQuestionnaireService.updateTQuestionnaire(tQuestionnaire));
    }

    /**
     * 删除问卷调查
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:remove')")
    @Log(title = "问卷调查", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tQuestionnaireService.deleteTQuestionnaireByIds(ids));
    }

    @Log(title = "问卷调查撤回操作", businessType = BusinessType.OTHER)
    @GetMapping("/back")
    public AjaxResult back(String id){
        System.out.println(id);
        return toAjax(tQuestionnaireService.back(id));
    }

    /******************           问卷分析                        ******************/

    @GetMapping("/totalByWjid")
    public AjaxResult getTotalByWjid(Long wjid){
        return AjaxResult.success(tQuestionnaireService.getTotalByWjid(wjid));
    }

    /**
     * 问卷分析展示 卷子
     * @param tQuestionnaire
     * @return
     */
    @GetMapping("/studentList")
    public TableDataInfo studentList(TQuestionnaireWithCp tQuestionnaire)
    {
        startPage();
        List<TQuestionnaireWithCp> list = tQuestionnaireService.selectTQuestionnaireStudentList(tQuestionnaire);
        return getDataTable(list);
    }

    /**
     * 问卷各个选择题 题目分析统计
     * @param wjid
     * @return
     */
    @GetMapping("/choseAnalysis")
    public AjaxResult getChoseAnalysis(Long wjid){

        List<QuestionnaireTask> list  = tQuestionnaireService.getChoseAnalysis(wjid);
        //list按试题id分组
        Map<String, List<QuestionnaireTask>> groupByTmid = list.stream().collect(Collectors.groupingBy(QuestionnaireTask::getTmid));
        return AjaxResult.success(groupByTmid);
    }
    /**
     * 问卷各个问答题 题目分析统计
     * @param wjid
     * @return
     */
    @GetMapping("/wdAnalysis")
    public TableDataInfo getWdAnalysis(Long wjid,String tmid){
        startPage();
        List<QuestionnaireTask> list  = tQuestionnaireService.getWdAnalysis(wjid,tmid);
        return getDataTable(list);
    }
}
