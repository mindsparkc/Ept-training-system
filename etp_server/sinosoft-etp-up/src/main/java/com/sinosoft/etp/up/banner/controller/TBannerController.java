package com.sinosoft.etp.up.banner.controller;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.utils.DeviceUtil;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.etp.up.banner.domain.TBanner;
import com.sinosoft.etp.up.banner.service.ITBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 轮播图Controller
 *
 *
 * @date 2022-02-22
 */
@RestController
@RequestMapping("/etp/banner")
public class TBannerController extends BaseController
{
    @Autowired
    private ITBannerService tBannerService;

    /**
     * 查询轮播图列表
     */
    @GetMapping("/list")
    public AjaxResult list()
    {
        String userAgent = ServletUtils.getRequest().getHeader("user-agent");
        // 【0移动、1PC端】
        String source = "0";
        if(DeviceUtil.checkAgentIsMobile(userAgent)){
            System.out.println("请求来自移动端访问>>>>>>>>>>>>>>");
        }else {
            source = "1";
        }
        List<TBanner> list = tBannerService.selectTBannerList(source);
        List<TBanner> result = new ArrayList<>();
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TBanner>(TBanner.class);
        if(null!=list && list.size()>0){
            List<Map<String,Object>> data = JSONObject.parseObject(list.get(0).getImageurl(),ArrayList.class);
            data.forEach(item->{
                if((boolean)item.get("status")){
                    TBanner tBanner = new TBanner();
                    tBanner.setImageurl(item.get("url").toString());
                    tBanner.setOpenurl(item.get("openUrl")!=null?item.get("openUrl").toString():"");
                    tBanner.setTitle(item.get("title")!=null?item.get("title").toString():"");
                    result.add((TBanner)responseFieldUtil.response(tBanner));
                }
            });
        }



        return AjaxResult.success(result);
    }



    /**
     * 获取轮播图详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tBannerService.selectTBannerById(id));
    }


}
