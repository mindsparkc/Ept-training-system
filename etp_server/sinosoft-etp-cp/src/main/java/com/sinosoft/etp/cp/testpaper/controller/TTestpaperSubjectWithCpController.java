package com.sinosoft.etp.cp.testpaper.controller;

import com.alibaba.fastjson.JSON;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.subject.domain.TSubjectWithCp;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperSubjectWithCp;
import com.sinosoft.etp.cp.testpaper.service.ITTestpaperSubjectWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 试卷关联题目Controller
 *
 *
 * @date 2021-12-17
 */
@RestController
@RequestMapping("/etp/testpaperSubjectWithCp")
public class TTestpaperSubjectWithCpController extends BaseController
{
    @Autowired
    private ITTestpaperSubjectWithCpService tTestpaperSubjectService;
    /**
     * 查询试卷关联题目列表
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaperSubject:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTestpaperSubjectWithCp tTestpaperSubject)
    {
        startPage();
        List<TTestpaperSubjectWithCp> list = tTestpaperSubjectService.selectTTestpaperSubjectList(tTestpaperSubject);
        return getDataTable(list);
    }

    /**
     * 导出试卷关联题目列表
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaperSubject:export')")
    @Log(title = "试卷关联题目", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TTestpaperSubjectWithCp tTestpaperSubject)
    {
        List<TTestpaperSubjectWithCp> list = tTestpaperSubjectService.selectTTestpaperSubjectList(tTestpaperSubject);
        ExcelUtil<TTestpaperSubjectWithCp> util = new ExcelUtil<TTestpaperSubjectWithCp>(TTestpaperSubjectWithCp.class);
        return util.exportExcel(list, "试卷关联题目数据");
    }

    /**
     * 获取试卷关联题目详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaperSubject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tTestpaperSubjectService.selectTTestpaperSubjectById(id));
    }

    /**
     * 新增试卷关联题目
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaperSubject:add')")
    @Log(title = "试卷关联题目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTestpaperSubjectWithCp tTestpaperSubject)
    {
        return toAjax(tTestpaperSubjectService.insertTTestpaperSubject(tTestpaperSubject));
    }

    /**
     * 修改试卷关联题目
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaperSubject:edit')")
    @Log(title = "试卷关联题目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTestpaperSubjectWithCp tTestpaperSubject)
    {
        return toAjax(tTestpaperSubjectService.updateTTestpaperSubject(tTestpaperSubject));
    }

    /**
     * 删除试卷关联题目
     */
    @PreAuthorize("@ss.hasPermi('etp:testpaperSubject:remove')")
    @Log(title = "试卷关联题目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTestpaperSubjectService.deleteTTestpaperSubjectByIds(ids));
    }

    @PostMapping(value="savePaperSubject")
    @Transactional
    public AjaxResult savePaperSubject(@RequestBody Map<String,Object> data){
        List<TTestpaperSubjectWithCp> dataList = new ArrayList<>();
        Long sjid = Long.parseLong(data.get("sjid").toString());
        //删除试卷关联题目
        tTestpaperSubjectService.deleteTestpaperSubjectBySjid(sjid);
        data.forEach((key,value)->{
            if(!"sjid".equals(key)){
                String obj = JSON.toJSONString(value);
                List<TSubjectWithCp> list = JSON.parseArray(obj, TSubjectWithCp.class);
                for(int i = 0;i < list.size();i++){
                    if(null != list.get(i)){
                        TTestpaperSubjectWithCp paperSubject = new TTestpaperSubjectWithCp();
                        paperSubject.setSjid(sjid);
                        paperSubject.setTkid(list.get(i).getId());
                        paperSubject.setSort((long) (i+1));
                        paperSubject.setType(key);
                        paperSubject.setCreateBy(SecurityUtils.getUsername());
                        paperSubject.setUpdateBy(SecurityUtils.getUsername());
                        paperSubject.setCreateTime(new Date());
                        paperSubject.setUpdateTime(new Date());
                        dataList.add(paperSubject);
                    }
                }
            }


        });
        return toAjax(tTestpaperSubjectService.insertTTestpaperSubjects(dataList));
    }


    /**
     * 获取试卷关联的题目
     * @param sjid
     * @return
     */
    @GetMapping("getSubjectByPaper")
    public AjaxResult getSubjectByPaper( Long sjid)
    {

        List<Map<String,Object>> list = tTestpaperSubjectService.getSubjectByPaper(sjid);
//        Map<String,>
        //数据按type 分组
        Map<String, List<Map<String,Object>>> groupByList = list.stream().collect(Collectors.groupingBy(e -> e.get("type").toString()));

        return AjaxResult.success(groupByList);
    }

    /**
     * 获取题目标签-用于随机生成考卷展示标签
     * @param sjid
     * @return
     */
    @GetMapping("getPaperSubjectTags")
    public AjaxResult getPaperSubjectTags( Long sjid)
    {

        List<Map<String,Object>> list = tTestpaperSubjectService.getPaperSubjectTags(sjid);
        //数据按type 分组
        Map<String, List<Map<String,Object>>> groupByList = list.stream().collect(Collectors.groupingBy(e -> e.get("type").toString()));

        return AjaxResult.success(groupByList);
    }

    /**
     * 更新题目分数
     * @param paramMap
     * @return
     */
    @PostMapping(value = {"updateScore"})
    public AjaxResult updateScore(@RequestBody Map<String,Object> paramMap)
    {
        Long sjid = Long.parseLong(paramMap.get("sjid").toString()) ;
        String fb = paramMap.get("fb").toString();
        Map<String,Object> fenMap = (Map<String, Object>) paramMap.get("fenMap");
        if(fenMap.size()>0){
            fenMap.forEach((key,value) ->{
                tTestpaperSubjectService.updateScore(sjid,key,value.toString(),fb);
            });
        }else{
            tTestpaperSubjectService.updateScore(sjid,null,null,fb);
        }

        return AjaxResult.success();
    }

    /**
     * 获取题目分值
     * @param sjid
     * @return
     */
    @GetMapping("selectScoreByType")
    public AjaxResult selectScoreByType(Long sjid){
        List<Map<String,Object>> list = tTestpaperSubjectService.selectScoreByType(sjid);
        Map<String,Object> score_map = new HashMap<>();
        list.forEach(map ->{
            score_map.put(map.get("type").toString(),map.get("score"));
        });
        return AjaxResult.success(score_map);
    }

    /**
     * 评卷
     */
    @Log(title = "评卷", businessType = BusinessType.UPDATE)
    @PostMapping(value="updateDa")
    public AjaxResult updateDa(@RequestBody Map<String,Object> params)
    {
        return toAjax(tTestpaperSubjectService.updateDa(params));
    }

}
