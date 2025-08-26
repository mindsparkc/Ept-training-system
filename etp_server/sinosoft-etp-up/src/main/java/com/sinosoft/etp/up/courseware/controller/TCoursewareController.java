package com.sinosoft.etp.up.courseware.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.etp.up.courseware.domain.TCourseware;
import com.sinosoft.etp.up.courseware.service.ITCoursewareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课件管理Controller
 *
 *
 * @date 2021-11-08
 */
@RestController
@RequestMapping("/etp/courseware")
public class TCoursewareController extends BaseController
{
    @Autowired
    private ITCoursewareService tCoursewareService;

    /**
     * 查询课件管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:list')")

    @GetMapping("/list")
    public TableDataInfo list(TCourseware tCourseware)
    {
        String tags = tCourseware.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tCourseware.getParams().put("tags", tags.split(","));
            tCourseware.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TCourseware> list = tCoursewareService.selectTCoursewareList(tCourseware);
        return getDataTable(list);
    }


    /**
     * 获取课件管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TCourseware>(TCourseware.class);
        return AjaxResult.success(responseFieldUtil.response(tCoursewareService.selectTCoursewareById(id)));
    }



}
