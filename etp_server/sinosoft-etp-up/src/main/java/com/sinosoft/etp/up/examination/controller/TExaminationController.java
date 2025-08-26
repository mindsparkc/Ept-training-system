package com.sinosoft.etp.up.examination.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.etp.up.examination.domain.TExamination;
import com.sinosoft.etp.up.examination.service.ITExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 考试Controller
 *
 *
 * @date 2021-12-24
 */
@RestController
@RequestMapping("/etp/examination")
public class TExaminationController extends BaseController
{
    @Autowired
    private ITExaminationService tExaminationService;

    /**
     * 查询考试列表
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:list')")
    @GetMapping("/list")
    public TableDataInfo list(TExamination tExamination)
    {
        startPage();
        String tags = tExamination.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tExamination.getParams().put("tags", tags.split(","));
            tExamination.getParams().put("tagsCount", tags.split(",").length);
        }
        Date now = new Date();
        tExamination.setPushTime(now);
        List<TExamination> list = tExaminationService.selectTExaminationList(tExamination);
        return getDataTable(list, TExamination.class);
    }


    /**
     * 获取考试详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TExamination>(TExamination.class);
        return AjaxResult.success(responseFieldUtil.response(tExaminationService.selectTExaminationById(id)));
    }


    /** 查看考试记录 */
    @GetMapping(value = "/hisInfo/{id}")
    public AjaxResult getHisInfo(@PathVariable("id") Long id){
        return AjaxResult.success(tExaminationService.getTExaminationById(id));
    }



    /** 心跳检测 */
    @GetMapping(value="/heartbeat/{id}")
    public AjaxResult heartBeat(@PathVariable("id") Long id){
        return AjaxResult.success("心跳检测正常");
    }

}
