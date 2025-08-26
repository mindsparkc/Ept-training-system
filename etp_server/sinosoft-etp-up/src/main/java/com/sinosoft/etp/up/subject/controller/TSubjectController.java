package com.sinosoft.etp.up.subject.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.etp.up.subject.domain.TSubject;
import com.sinosoft.etp.up.subject.service.ITSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 题库Controller
 *
 *
 * @date 2021-11-08
 */
@RestController
@RequestMapping("/etp/subject")
public class TSubjectController extends BaseController
{
    @Autowired
    private ITSubjectService tSubjectService;

    /**
     * 查询题库列表
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSubject tSubject)
    {
        startPage();
        String tags = tSubject.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tSubject.getParams().put("tags", tags.split(","));
            tSubject.getParams().put("tagsCount", tags.split(",").length);
        }
        List<TSubject> list = tSubjectService.selectTSubjectList(tSubject);
        return getDataTable(list);
    }



    /**
     * 获取题库详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tSubjectService.selectTSubjectById(id));
    }



}
