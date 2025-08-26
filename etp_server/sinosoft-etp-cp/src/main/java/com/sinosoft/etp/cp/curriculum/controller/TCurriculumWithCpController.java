package com.sinosoft.etp.cp.curriculum.controller;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumContentWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.curriculum.service.ITCurriculumWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * curriculumController
 *
 *
 * @date 2021-11-15
 */
@RestController
@RequestMapping("/etp/curriculumWithCp")
public class TCurriculumWithCpController extends BaseController
{
    @Autowired
    private ITCurriculumWithCpService tCurriculumService;

    /**
     * 查询curriculum列表
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCurriculumWithCp tCurriculum)
    {
        startPage();
        String tags = tCurriculum.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tCurriculum.getParams().put("tags", tags.split(","));
            tCurriculum.getParams().put("tagsCount", tags.split(",").length);
        }
        List<TCurriculumWithCp> list = tCurriculumService.selectTCurriculumList(tCurriculum);
        return getDataTable(list, TCurriculumWithCp.class);
    }

    /**
     * 新增curriculum（暂存，不包含课程的具体内容项）
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:add')")
    @Log(title = "curriculum", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCurriculumWithCp tCurriculum)
    {
        if(tCurriculumService.checkExistKC(tCurriculum))
        {
            return AjaxResult.error("已存在名称为【"+tCurriculum.getMc()+"】的课程,不允许新增");
        }
        return AjaxResult.success(tCurriculumService.insertTCurriculum(tCurriculum));
    }

    /**
     * 获取curriculum详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil(TCurriculumWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tCurriculumService.selectTCurriculumById(id)));
    }

    /**
     * 分步骤完善课程，第二步中的提交
     */
    @PostMapping("/completeTCurriculum")
    public AjaxResult completeTCurriculum(@RequestBody TCurriculumWithCp tCurriculum){
        return AjaxResult.success(tCurriculumService.completeTCurriculum(tCurriculum));
    }

    /**
     * 获取课程详情内容项
     * @param id 课程id
     */
    @GetMapping("/getTCurriculumContentDetailByCid/{id}")
    public AjaxResult getTCurriculumContentDetailByCid(@PathVariable("id") Long id){
        List<TCurriculumContentWithCp> contents = tCurriculumService.getTCurriculumContentDetailByCid(id);
        return AjaxResult.success(contents);
    }

    /**
     * 课程取消发布
     * @param id 课程id
     */
    @GetMapping("/back/{id}")
    public AjaxResult pushBack(@PathVariable("id") Long id){
        tCurriculumService.pushBack(id);
        return AjaxResult.success();
    }

    /**
     * 修改curriculum
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:edit')")
    @Log(title = "curriculum", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCurriculumWithCp tCurriculum)
    {
        if(tCurriculumService.checkExistKC(tCurriculum))
        {
            return AjaxResult.error("已存在名称为【"+tCurriculum.getMc()+"】的课程");
        }
        return toAjax(tCurriculumService.updateTCurriculum(tCurriculum));
    }

    @PutMapping(value="updateTjzt")
    public AjaxResult updateTjzt(@RequestBody TCurriculumWithCp tCurriculum)
    {
        return toAjax(tCurriculumService.updateTCurriculumTjzt(tCurriculum));
    }

    /**
     * 删除curriculum
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:remove')")
    @Log(title = "curriculum", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCurriculumService.deleteTCurriculumByIds(ids));
    }




    /***************************统计分析************************/

    /**
     * 课程完成总的用户数
     * @param kcid
     * @return
     */
    @GetMapping("/allCompletedKcUser")
    public AjaxResult getAllCompletedKcUser(Long kcid){
        return AjaxResult.success(tCurriculumService.getAllCompletedKcUser(kcid));
    }

    /**
     * 近七日课程完成人数
     * @param kcid
     * @return
     */
    @GetMapping("/completedKcUserByDate")
    public AjaxResult getCompletedKcUserByDate(Long kcid){
        String start = (DateUtils.getNdaysAgo(6)+" 00:00:00");
        Long startTime = DateUtils.parseDate(start).getTime();
        Long endTime = System.currentTimeMillis();;
        List<Map<String,Object>> list =tCurriculumService.getCompletedKcUserByDate(kcid,startTime,endTime);
        //初始化7天日期的一个map(日期：数量形式)
        Map<String, Integer> map = new TreeMap<>();
        for(int i =6;i>=0;i--){
            map.put(DateUtils.getNdaysAgo(i),0);
        }
        list.forEach(item->{
            map.put(item.get("date").toString(),Integer.parseInt(item.get("num").toString()));
        });
        return AjaxResult.success(map);
    }

    /**
     * 课件总计已完成
     * @param kcid
     * @return
     */
    @GetMapping("/completedCourse")
    public AjaxResult getCompletedCourse(Long kcid)
    {
        return AjaxResult.success(tCurriculumService.getCompletedCourse(kcid));
    }

    /**
     * 已领取课程用户数
     * @param kcid
     * @return
     */
    @GetMapping("/receivedKcUser")
    public AjaxResult getReceivedKcUser(Long kcid){
        return AjaxResult.success(tCurriculumService.getReceivedKcUser(kcid));
    }

    /**
     * 昨日学习课程用户数
     * @param kcid
     * @return
     */
    @GetMapping("/yesterdayStudyKc")
    public AjaxResult getYesterdayStudyKc(Long kcid)
    {
        return AjaxResult.success(tCurriculumService.getYesterdayStudyKc(kcid));
    }

    /**
     * 课程总计包含课件数
     * @param kcid
     * @return
     */
    @GetMapping("/getCourseNum")
    public AjaxResult getCourseNum(Long kcid)
    {
        return AjaxResult.success(tCurriculumService.getCourseNum(kcid));
    }

    /**
     * 课程各课件完成情况
     * @param tCurriculum
     * @return
     */
    @GetMapping("/completedKjByDate")
    public TableDataInfo getCompletedKjByDate(TCurriculumWithCp tCurriculum)
    {
        startPage();
        String startTime = DateUtils.getNdaysAgo(6)+" 00:00:00";
        String endTime = DateUtils.getDate()+" 23:59:59";
        //生成一个7天的数组
        String[] dates = new String[7];
        for(int i = 0;i < 7;i++){
            dates[i]=DateUtils.getNdaysAgo(i);
        }
        return getDataTable(tCurriculumService.getCompletedKjByDate(tCurriculum.getId(),startTime,endTime,dates));
    }
    /**
     * 统计分析--各课程课件完成情况
     * @param tCurriculum
     * @return
     */
    @GetMapping("/getCompletedKcKjDate")
    public TableDataInfo getCompletedKcKjDate(TCurriculumWithCp tCurriculum)
    {
        startPage();
        String startTime = DateUtils.getNdaysAgo(6)+" 00:00:00";
        String endTime = DateUtils.getDate()+" 23:59:59";
        //生成一个7天的数组
        String[] dates = new String[7];
        for(int i = 0;i < 7;i++){
            dates[i]=DateUtils.getNdaysAgo(i);
        }
        return getDataTable(tCurriculumService.getCompletedKcKjDate(startTime,endTime,dates));
    }

    /**
     * 已发布课程数
     * @return
     */
    @GetMapping("/getFbCurriculum")
    public AjaxResult getFbCurriculum(){
        return AjaxResult.success(tCurriculumService.getFbCurriculum());
    }


    @GetMapping("/checkExistKC")
    public AjaxResult checkExistKC(TCurriculumWithCp curriculum){
        boolean flag = tCurriculumService.checkExistKC(curriculum);
        return AjaxResult.success(flag);
    }

    //****************************课程统计***************************
    /**
     * 获取课程学习时间
     * @param kcid
     * @return
     */
    @GetMapping("/getKcLearnTime")
    public AjaxResult getKcLearnTime(Long kcid){
        return AjaxResult.success(tCurriculumService.getKcLearnTime(kcid));
    }
    @GetMapping("/getKcListByKjTime")
    public AjaxResult getKcListByKjTime(Long kcid){
        return AjaxResult.success(tCurriculumService.getKcListByKjTime(kcid));
    }
    @GetMapping("/getEveKcLearnTime")
    public AjaxResult getEveKcLearnTime(){
        return AjaxResult.success(tCurriculumService.getEveKcLearnTime());
    }
}
