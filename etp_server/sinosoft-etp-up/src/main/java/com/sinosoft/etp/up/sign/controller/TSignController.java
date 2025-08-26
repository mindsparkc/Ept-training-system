package com.sinosoft.etp.up.sign.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.up.sign.domain.TSign;
import com.sinosoft.etp.up.sign.service.ITSignService;
import com.sinosoft.etp.up.sign.service.ITSignStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * signController
 *
 *
 * @date 2022-02-09
 */
@RestController
@RequestMapping("/etp/sign")
public class TSignController extends BaseController
{
    @Autowired
    private ITSignService tSignService;
    @Autowired
    private ITSignStudentService tSignStudentService;

    /**
     * 查询sign列表
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSign tSign)
    {
        String tags = tSign.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tSign.getParams().put("tags", tags.split(","));
            tSign.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        tSign.setXyid(xyid);
        List<TSign> list = tSignService.selectTSignList(tSign);
        return getDataTable(list, TSign.class);
    }

    /**
     * 导出sign列表
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:export')")
    @Log(title = "sign", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TSign tSign)
    {
        List<TSign> list = tSignService.selectTSignList(tSign);
        ExcelUtil<TSign> util = new ExcelUtil<TSign>(TSign.class);
        return util.exportExcel(list, "sign数据");
    }

    /**
     * 获取sign详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TSign>(TSign.class);
        return AjaxResult.success(responseFieldUtil.response(tSignService.selectTSignById(id)));
    }

    /**
     * 新增sign
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:add')")
    @Log(title = "sign", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSign tSign)
    {
        return toAjax(tSignService.insertTSign(tSign));
    }

    /**
     * 修改报名状态
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:edit')")
    @Log(title = "sign", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSign tSign)
    {
//
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        tSign.setXyid(xyid);
        return toAjax(tSignService.updateTSign(tSign));
    }

    /**
     * 删除sign
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:remove')")
    @Log(title = "sign", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSignService.deleteTSignByIds(ids));
    }


    /**
     * 活动报名
     *
     */
    @PostMapping("studentSign")
    public AjaxResult studentSign(TSign tSign){
        //更新活动标志
        String sfbm = tSign.getSfbm();
        tSign = tSignService.selectTSignById(tSign.getId());
        Long date = System.currentTimeMillis();
        Long jssj = tSign.getJssj().getTime();
        int res = 0;
        if(jssj>date){
            tSign.setSfbm(sfbm);
            res = tSignService.updateTSign(tSign);
        }
        return AjaxResult.success(res);
    }
}
