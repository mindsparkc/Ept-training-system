package com.sinosoft.etp.up.questionnaire.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaire;
import com.sinosoft.etp.up.questionnaire.service.ITQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 问卷调查Controller
 *
 *
 * @date 2022-02-16
 */
@RestController
@RequestMapping("/etp/questionnaire")
public class TQuestionnaireController extends BaseController
{
    @Autowired
    private ITQuestionnaireService tQuestionnaireService;

    /**
     * 查询问卷调查列表
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:list')")
    @GetMapping("/list")
    public TableDataInfo list(TQuestionnaire tQuestionnaire)
    {
        startPage();
        String tags =tQuestionnaire.getTags();
        if(StringUtils.isNotBlank(tags)){
            tQuestionnaire.getParams().put("tags",tags.split(","));
            tQuestionnaire.getParams().put("tagsCount",tags.split(",").length);
        }
        List<TQuestionnaire> list = tQuestionnaireService.selectTQuestionnaireList(tQuestionnaire);
        return getDataTable(list, TQuestionnaire.class);
    }

    /**
     * 导出问卷调查列表
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:export')")
    @Log(title = "问卷调查", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TQuestionnaire tQuestionnaire)
    {
        List<TQuestionnaire> list = tQuestionnaireService.selectTQuestionnaireList(tQuestionnaire);
        ExcelUtil<TQuestionnaire> util = new ExcelUtil<TQuestionnaire>(TQuestionnaire.class);
        return util.exportExcel(list, "问卷调查数据");
    }

    /**
     * 获取问卷调查详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        TQuestionnaire tQuestionnaire = tQuestionnaireService.selectTQuestionnaireById(id);
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TQuestionnaire>(TQuestionnaire.class);
        return AjaxResult.success(responseFieldUtil.response(tQuestionnaire));
    }

    /**
     * 新增问卷调查
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:add')")
    @Log(title = "问卷调查", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQuestionnaire tQuestionnaire)
    {
        return toAjax(tQuestionnaireService.insertTQuestionnaire(tQuestionnaire));
    }

    /**
     * 修改问卷调查
     */
    @PreAuthorize("@ss.hasPermi('etp:questionnaire:edit')")
    @Log(title = "问卷调查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQuestionnaire tQuestionnaire)
    {
        TQuestionnaire info = tQuestionnaireService.selectTQuestionnaireById(tQuestionnaire.getId());
        //当前时间 早于 开始时间
        if(null != info.getWjnr()){
            return AjaxResult.error("问卷已经作答，不能再次提交");
        }else if(info.getBeginTime().after(new Date())){
            return AjaxResult.error("问卷还未开始，不能进行提交");
        }else if(info.getEndTime().before(new Date())){
            //问卷结束时间早于当前时间
            return AjaxResult.error("问卷已经结束，不能进行提交");
        }
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
}
