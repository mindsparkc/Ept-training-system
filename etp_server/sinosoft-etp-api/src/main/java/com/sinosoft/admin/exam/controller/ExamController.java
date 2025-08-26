package com.sinosoft.admin.exam.controller;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.admin.exam.domain.ExamQuery;
import com.sinosoft.admin.exam.domain.ExamReqDTO;
import com.sinosoft.admin.exam.service.IExamService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.Exam;
import com.sinosoft.user.exam.domain.UserExam;
import com.sinosoft.user.exam.domain.UserExamRecord;
import com.sinosoft.user.exam.domain.UserExamRecordReqQuery;
import com.sinosoft.user.exam.domain.UserExamRecordSnapshot;
import com.sinosoft.user.exam.mapper.UserExamRecordSnapshotMapper;
import com.sinosoft.user.exam.service.IUserExamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试信息Controller
 *
 * @author ruoyi
 * @date 2022-09-04
 */
@RestController
@RequestMapping("/admin/v1/exam")
public class ExamController extends BaseController
{
    @Resource
    private IExamService examService;

    @Resource
    private IUserExamService userExamService;

    @Resource
    UserExamRecordSnapshotMapper userExamRecordSnapshotMapper;


    /**
     * 查询考试信息列表
     */
    @PreAuthorize("@ss.hasPermi('exam:exam:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExamQuery examQuery) {
        startPage();
        List<Exam> list = examService.selectExamList(examQuery);
        return getDataTable(list, Exam.class);
    }

    /**
     * 获取考试信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('exam:exam:query')")
    @GetMapping(value = "/{examId}")
    public AjaxResult getInfo(@PathVariable("examId") Long examId) {
        return AjaxResult.success(examService.selectExamById(examId));
    }

    /**
     * 新增考试信息
     */
    @PreAuthorize("@ss.hasPermi('exam:exam:add')")
    @Log(title = "考试信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExamReqDTO examDto) {
        Long examId = examService.insertExam(examDto);
        Map<String, Long> exam = new HashMap<>();
        exam.put("examId", examId);
        return AjaxResult.success(exam);
    }

    /**
     * 修改考试信息
     */
    @PreAuthorize("@ss.hasPermi('exam:exam:edit')")
    @Log(title = "考试信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExamReqDTO examDto) {
        return toAjax(examService.updateExam(examDto));
    }

    /**
     * 修改考试状态
     */
    @PreAuthorize("@ss.hasPermi('exam:exam:edit')")
    @Log(title = "考试信息", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult editStatus(@RequestBody ExamReqDTO examDto) {
        return toAjax(examService.updateExamStatus(examDto));
    }


    /**
     * 删除考试信息
     */
    @PreAuthorize("@ss.hasPermi('exam:exam:remove')")
    @Log(title = "考试信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{examIds}")
    public AjaxResult remove(@PathVariable Long[] examIds)
    {
        return toAjax(examService.deleteExamByIds(examIds));
    }


    /**
     * 校验考试名称是否存在
     */
    @Log(title = "校验考试名称", businessType = BusinessType.DELETE)
    @GetMapping("/checkName/{name}&{examId}")
    public AjaxResult checkName(@PathVariable String name,@PathVariable Long examId)
    {
        int i = examService.checkName(name,examId);
        boolean flag = false;
        if(i>0 && examId==0){
            flag = true;
        }else if(i>1 && examId!=0){
            flag = true;
        }
        return AjaxResult.success(flag);
    }

    /**
     * 获取学员考试列表，支持根据学员 ID、考试ID、开始考试时间（range - 范围）
     */
    @Log(title = "获取学员考试记录", businessType = BusinessType.DELETE)
    @GetMapping("/record/list")
    public TableDataInfo get(@PathParam("userId") Long userId,
                             @PathParam("examId") Long examId,
                             @RequestParam(value = "range",required = false) List<String> range,
                             @RequestParam(value = "status",required = false) String status) {
        startPage();
        if (!(range == null || range.size() == 0 || (range.size() == 2 && range.get(0).compareTo(range.get(1)) < 0))) {
            throw new RuntimeException("时间范围错误，请校验后重新输入！");
        }

        UserExamRecordReqQuery query = new UserExamRecordReqQuery();
        if (examId != null) query.setExamId(examId);
        query.setStatus(status);
        query.setUserId(userId);
        // 处理时间区间 YYYYMMDD
        if (range != null && range.size() == 2) {
            query.setRangStartTime(DateUtils.dateTime(DateUtils.YYYYMMDDHHMMSS, range.get(0) + "000000"));
            query.setRageEndTime(DateUtils.dateTime(DateUtils.YYYYMMDDHHMMSS, range.get(1) + "235959"));
        }

        List<UserExamRecord> userExamRecords = userExamService.queryExamRecord(query);

        return getDataTable(userExamRecords);
    }

    /**
     * 管理端考试判卷逻辑
     */
    @Log(title = "判卷 评卷 - 获取学员试卷", businessType = BusinessType.DELETE)
    @GetMapping("/record/{recordId}")
    public AjaxResult review(@PathVariable Long recordId) {
        // 提取学员快照
        UserExamRecordSnapshot snapshot = userExamRecordSnapshotMapper.selectExamRecordSnapshotById(recordId);
        if (snapshot.getSnapshot3() == null)
            return AjaxResult.success(snapshot.getSnapshot2());
        else
            return AjaxResult.success(snapshot.getSnapshot3());
    }

    /**
     * 管理端考试判卷逻辑
     */
    @Log(title = "判卷 评卷 - 提交判卷", businessType = BusinessType.DELETE)
    @PutMapping("/review/submit")
    public AjaxResult submitReview(@RequestBody UserExam userExam) {
        examService.submitReview(userExam);
        return AjaxResult.success("提交判卷成功");
    }
}
