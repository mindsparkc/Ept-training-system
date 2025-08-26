package com.sinosoft.etp.cp.courseware.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.courseware.service.ITCoursewareServiceWithCp;
import com.sinosoft.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课件管理Controller
 *
 *
 * @date 2021-11-08
 */
@RestController
@RequestMapping("/etp/coursewareWithCp")
public class TCoursewareWithCpController extends BaseController
{
    @Autowired
    private ITCoursewareServiceWithCp tCoursewareService;
    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询课件管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:list')")

    @GetMapping("/list")
    public TableDataInfo list(TCoursewareWithCp tCourseware)
    {
        String tags = tCourseware.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tCourseware.getParams().put("tags", tags.split(","));
            tCourseware.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TCoursewareWithCp> list = tCoursewareService.selectTCoursewareList(tCourseware);
        return getDataTable(list, TCoursewareWithCp.class);
    }

    /**
     * 导出课件管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:export')")
    @Log(title = "课件管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TCoursewareWithCp tCourseware)
    {
        List<TCoursewareWithCp> list = tCoursewareService.selectTCoursewareList(tCourseware);
        ExcelUtil<TCoursewareWithCp> util = new ExcelUtil<TCoursewareWithCp>(TCoursewareWithCp.class);
        return util.exportExcel(list, "课件管理数据");
    }

    /**
     * 获取课件管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        TCoursewareWithCp tCoursewareWithCp = tCoursewareService.selectTCoursewareById(id);
        // BY WLJ - 处理注解
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TCoursewareWithCp>(TCoursewareWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tCoursewareWithCp));
    }

    /**
     * 新增课件管理
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:add')")
    @Log(title = "课件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCoursewareWithCp tCourseware)
    {
        if(tCoursewareService.checkExistKJ(tCourseware))
        {
            return AjaxResult.error("已存在名称为【"+tCourseware.getMc()+"】的课件,不允许新增");
        }
        if("1".equals(tCourseware.getSuffix())){
            tCourseware.setSuffix("ppt");
        }else{
            String  suffix= tCourseware.getKjdz().substring(tCourseware.getKjdz().lastIndexOf(".")+1);
            tCourseware.setSuffix(suffix);
        }
        return toAjax(tCoursewareService.insertTCourseware(tCourseware));
    }

    /**
     * 修改课件管理
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:edit')")
    @Log(title = "课件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCoursewareWithCp tCourseware)
    {
        if(tCoursewareService.checkExistKJ(tCourseware))
        {
            return AjaxResult.error("已存在名称为【"+tCourseware.getMc()+"】的课件,不允许修改");
        }

        TCoursewareWithCp tCoursewareWithCp = tCoursewareService.selectTCoursewareById(tCourseware.getId());
        if(StringUtils.isNotEmpty(tCoursewareWithCp.getKjdz())){
            //判断课件是否重新上传
            String oldKjdz = tCoursewareWithCp.getKjdz().substring(tCoursewareWithCp.getKjdz().lastIndexOf("/"));
            String newKjdz = tCourseware.getKjdz().substring(tCourseware.getKjdz().lastIndexOf("/"));
            if(oldKjdz.equals(newKjdz)){
                //说明没有上传新的课件
                tCourseware.setStep(null);
            }else{
                tCourseware.setStep("0");
                if("1".equals(tCourseware.getSuffix())){
                    tCourseware.setSuffix("ppt");
                }else{
                    String suffix= tCourseware.getKjdz().substring(tCourseware.getKjdz().lastIndexOf(".")+1);
                    tCourseware.setSuffix(suffix);
                }
            }
        }

        // TODO: 替换 nr 中的 imag 地址
        tCourseware.setNr(tCourseware.getNr().replace(serverConfig.getUrl(), "{storage.path.url}"));

        return toAjax(tCoursewareService.updateTCourseware(tCourseware));
    }

    /**
     * 删除课件管理
     */
    @PreAuthorize("@ss.hasPermi('etp:courseware:remove')")
    @Log(title = "课件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCoursewareService.deleteTCoursewareByIds(ids));
    }

    @GetMapping("/selectedKjByKc")
    public AjaxResult getSelectedKjByKc(Long kcid)
    {
        List<TCoursewareWithCp> list = tCoursewareService.getSelectedKjByKc(kcid);
        return AjaxResult.success(list);
    }

    /**
     * 校验课件名称是否已经存在
     * @param tCourseware
     * @return
     */
    @GetMapping("/checkExistKJ")
    public AjaxResult checkExistKS(TCoursewareWithCp tCourseware){
        boolean flag = tCoursewareService.checkExistKJ(tCourseware);
        return AjaxResult.success(flag);
    }
}
