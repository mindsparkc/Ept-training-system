package com.sinosoft.etp.cp.examination.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.examination.domain.TExaminationInfoWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.examination.service.ITExaminationWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试Controller
 *
 *
 * @date 2021-12-24
 */
@RestController
@RequestMapping("/etp/examinationWithCp")
public class TExaminationWithCpController extends BaseController
{
    @Autowired
    private ITExaminationWithCpService tExaminationService;

    /**
     * 查询考试列表
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:list')")
    @GetMapping("/list")
    public TableDataInfo list(TExaminationWithCp tExamination)
    {
        startPage();
        String tags = tExamination.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tExamination.getParams().put("tags", tags.split(","));
            tExamination.getParams().put("tagsCount", tags.split(",").length);
        }
        List<TExaminationWithCp> list = tExaminationService.selectTExaminationList(tExamination);
        return getDataTable(list, TExaminationWithCp.class);
    }

    /**
     * 导出考试列表
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:export')")
    @Log(title = "考试", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TExaminationWithCp tExamination)
    {
        List<TExaminationWithCp> list = tExaminationService.selectTExaminationList(tExamination);
        ExcelUtil<TExaminationWithCp> util = new ExcelUtil<TExaminationWithCp>(TExaminationWithCp.class);
        return util.exportExcel(list, "考试数据");
    }

    /**
     * 获取缺考人员信息
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:query')")
    @GetMapping(value = "/getMissExam/{id}")
    public AjaxResult getMissExam(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TExaminationWithCp>(TExaminationWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tExaminationService.selectMissExamById(id)));
    }

    /**
     * 获取考试详情信息
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getExamination(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TExaminationWithCp>(TExaminationWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tExaminationService.selectTExaminationById(id)));
    }

    /**
     * 获取考试详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:edit')")
    @GetMapping(value = "/info/{id}&{pjzt}")
    public AjaxResult getInfo(@PathVariable("id") Long id,@PathVariable("pjzt") String pjzt)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TExaminationWithCp>(TExaminationWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tExaminationService.selectTExaminationInfo(id,pjzt)));
    }

    /**
     * 导出考试列表
     */
    @Log(title = "考试", businessType = BusinessType.EXPORT)
    @GetMapping("/exportCj/{ksid}")
    public AjaxResult exportCj(@PathVariable("ksid") Long ksid)
    {
        List<TExaminationInfoWithCp> list = tExaminationService.selectTExaminationList(ksid);
        ExcelUtil<TExaminationInfoWithCp> util = new ExcelUtil<>(TExaminationInfoWithCp.class);
        return util.exportExcel(list, "考试数据");
    }

    /**
     * 新增考试
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:add')")
    @Log(title = "考试", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TExaminationWithCp tExamination)
    {
        if(tExaminationService.checkExistKS(tExamination))
        {
            return AjaxResult.error("已存在名称为【"+tExamination.getMc()+"】的考试，不能保存");
        }
        //发布需要判断当前时间是否早于考试时间半小时
        if("99".equals(tExamination.getFb())){
            boolean time = tExamination.getKskssj().getTime() - System.currentTimeMillis() > 30*60*1000;
            if(time){
                Long id = tExaminationService.insertTExamination(tExamination);
                return AjaxResult.success("发布成功",id);
            }else{
                return error("当前时间需要早于考试时间半小时，才能发布");
            }
        }else{
            Long id = tExaminationService.insertTExamination(tExamination);
            return AjaxResult.success("暂存成功",id);
        }
    }

    /**
     * 修改考试
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:edit')")
    @Log(title = "考试", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TExaminationWithCp tExamination)
    {
        if(tExaminationService.checkExistKS(tExamination))
        {
            return AjaxResult.error("已存在名称为【"+tExamination.getMc()+"】的考试，不能保存");
        }
        //发布需要判断当前时间是否早于考试时间半小时
        if("99".equals(tExamination.getFb())){
           boolean time = tExamination.getKskssj().getTime() - System.currentTimeMillis() > 30*60*1000;
           if(time){
               tExaminationService.updateTExamination(tExamination);
               return success("发布成功");
           }else{
               return error("当前时间需要早于考试时间半小时，才能发布");
           }
        }else{
            //如果考试已经开始，不允许修改
            if(tExamination.getKskssj().getTime() < System.currentTimeMillis()){
                return error("考试已经开始，不允许修改");
            }
            tExaminationService.updateTExamination(tExamination);
            return success("暂存成功");
        }

    }

    @PreAuthorize("@ss.hasPermi('etp:examination:edit')")
    @PostMapping(value="/updateTExaminationFb")
    public AjaxResult updateTExaminationFb(@RequestBody TExaminationWithCp tExamination){
        //如果考试已经开始，不允许 取消发布
        if(tExamination.getKskssj().getTime() < System.currentTimeMillis()){
            return error("考试已经开始，不允许取消发布");
        }
        return toAjax(tExaminationService.updateTExaminationFb(tExamination));
    }
    /**
     * 删除考试
     */
    @PreAuthorize("@ss.hasPermi('etp:examination:remove')")
    @Log(title = "考试", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        //判断考试是否已经开始，开始了 不允许删除
        List<Long> list = tExaminationService.checkExaminations(ids);
        if(null!=list && list.size()>0){
            return error("删除数据中存在已经开始的考试，不允许删除");
        }
        return toAjax(tExaminationService.deleteTExaminationByIds(ids));
    }

    /**
     * 校验考试名称是否已经存在
     * @param tExamination
     * @return
     */
    @GetMapping("/checkExistKS")
    public AjaxResult checkExistKS(TExaminationWithCp tExamination){
        boolean flag = tExaminationService.checkExistKS(tExamination);
        return AjaxResult.success(flag);
    }
}
