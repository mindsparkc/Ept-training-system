package com.sinosoft.etp.cp.banner.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.etp.cp.banner.domain.TBannerWithCp;
import com.sinosoft.etp.cp.banner.service.ITBannerWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图Controller
 *
 *
 * @date 2022-03-09
 */
@RestController
@RequestMapping("/system/bannerWithCp")
public class TBannerWithCpController extends BaseController
{
    @Autowired
    private ITBannerWithCpService tBannerService;

    /**
     * 查询轮播图列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TBannerWithCp tBanner)
    {
        startPage();
        List<TBannerWithCp> list = tBannerService.selectTBannerList(tBanner);
        return getDataTable(list, TBannerWithCp.class);
    }


    /**
     * 新增轮播图
     */
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TBannerWithCp tBanner)
    {
        return toAjax(tBannerService.insertTBanner(tBanner));
    }

}
