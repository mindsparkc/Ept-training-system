package com.sinosoft.etp.cp.testpaper.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperWithCp;
import com.sinosoft.etp.cp.testpaper.service.ITTestpaperWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 试卷Controller
 *
 *
 * @date 2021-11-19
 */
@RestController
@RequestMapping("/etp/testpaperWithCp")
public class TTestpaperWithCpController extends BaseController
{
    @Autowired
    private ITTestpaperWithCpService tTestpaperService;

    /**
     * 查询试卷列表
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaper:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTestpaperWithCp tTestpaper)
    {
        startPage();
        String tags = tTestpaper.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tTestpaper.getParams().put("tags", tags.split(","));
            tTestpaper.getParams().put("tagsCount", tags.split(",").length);
        }
        List<TTestpaperWithCp> list = tTestpaperService.selectTTestpaperList(tTestpaper);
        return getDataTable(list, TTestpaperWithCp.class);
    }

    /**
     * 导出试卷列表
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaper:export')")
    @Log(title = "试卷", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TTestpaperWithCp tTestpaper)
    {
        List<TTestpaperWithCp> list = tTestpaperService.selectTTestpaperList(tTestpaper);
        ExcelUtil<TTestpaperWithCp> util = new ExcelUtil<TTestpaperWithCp>(TTestpaperWithCp.class);
        return util.exportExcel(list, "试卷数据");
    }

    /**
     * 获取试卷详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaper:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TTestpaperWithCp>(TTestpaperWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tTestpaperService.selectTTestpaperById(id)));
    }

    /**
     * 新增试卷
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaper:add')")
    @Log(title = "试卷", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTestpaperWithCp tTestpaper)
    {

        if(tTestpaperService.checkExistSJ(tTestpaper))
        {
            return AjaxResult.error("已存在名称为【"+tTestpaper.getSjmc()+"】的试卷,不允许保存");
        }
        return toAjaxHavaId(tTestpaperService.insertTTestpaper(tTestpaper));
    }

    /**
     * 修改试卷
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaper:edit')")
    @Log(title = "试卷", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTestpaperWithCp tTestpaper)
    {
        if(tTestpaperService.checkExistSJ(tTestpaper))
        {
            return AjaxResult.error("已存在名称为【"+tTestpaper.getSjmc()+"】的试卷,不允许保存");
        }
        return toAjax(tTestpaperService.updateTTestpaper(tTestpaper));
    }

    /**
     * 删除试卷
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaper:remove')")
    @Log(title = "试卷", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTestpaperService.deleteTTestpaperByIds(ids));
    }


    /**
     * 试卷取消发布
     * @param id 试卷id
     */
    @GetMapping("/back/{id}")
    public AjaxResult pushBack(@PathVariable("id") Long id){
        tTestpaperService.pushBack(id);
        return AjaxResult.success();
    }

    @GetMapping("/checkExistSJ")
    public AjaxResult checkExistSJ(TTestpaperWithCp tTestpaper){
        boolean flag = tTestpaperService.checkExistSJ(tTestpaper);
        return AjaxResult.success(flag);
    }
}
