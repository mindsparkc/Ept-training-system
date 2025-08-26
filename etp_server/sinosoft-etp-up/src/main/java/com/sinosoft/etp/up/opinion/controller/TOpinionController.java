package com.sinosoft.etp.up.opinion.controller;

import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.up.opinion.domain.TOpinion;
import com.sinosoft.etp.up.opinion.service.ITOpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 意见反馈Controller
 *
 *
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/etp/opinion")
public class TOpinionController extends BaseController
{
    @Autowired
    private ITOpinionService tOpinionService;

    /**
     * 查询意见反馈列表
     */
    @PreAuthorize("@ss.hasPermi('etp:opinion:list')")
    @GetMapping("/list")
    public TableDataInfo list(TOpinion tOpinion)
    {
        startPage();
        List<TOpinion> list = tOpinionService.selectTOpinionList(tOpinion);
        return getDataTable(list, TOpinion.class);
    }

    /**
     * 导出意见反馈列表
     */
    @PreAuthorize("@ss.hasPermi('etp:opinion:export')")
    @Log(title = "意见反馈", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TOpinion tOpinion)
    {
        List<TOpinion> list = tOpinionService.selectTOpinionList(tOpinion);
        ExcelUtil<TOpinion> util = new ExcelUtil<TOpinion>(TOpinion.class);
        return util.exportExcel(list, "意见反馈数据");
    }

    /**
     * 获取意见反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:opinion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TOpinion>(TOpinion.class);
        return AjaxResult.success(responseFieldUtil.response(tOpinionService.selectTOpinionById(id)));
    }

    /**
     * 新增意见反馈
     */
    @PreAuthorize("@ss.hasPermi('etp:opinion:add')")
    @Log(title = "意见反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOpinion tOpinion)
    {
//        获取学员ID
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        SecurityUtils.getLoginUser().getUser().getDeptId();
        tOpinion.setXyid(xyid);
        String fj = tOpinion.getFj();
        if(StringUtils.isNotEmpty(fj)){
            List<Map<String,String>> fj_List= JSONObject.parseObject(fj, ArrayList.class);
            fj_List.forEach(map ->{
                map.keySet().removeIf(key -> key =="uid");
                map.keySet().removeIf(key -> key =="status");
            });
            tOpinion.setFj(JSONObject.toJSONString(fj_List));
        }

        return toAjax(tOpinionService.insertTOpinion(tOpinion));
    }

    /**
     * 修改意见反馈
     */
    @PreAuthorize("@ss.hasPermi('etp:opinion:edit')")
    @Log(title = "意见反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOpinion tOpinion)
    {
        return toAjax(tOpinionService.updateTOpinion(tOpinion));
    }

    /**
     * 删除意见反馈
     */
    @PreAuthorize("@ss.hasPermi('etp:opinion:remove')")
    @Log(title = "意见反馈", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tOpinionService.deleteTOpinionByIds(ids));
    }
}
