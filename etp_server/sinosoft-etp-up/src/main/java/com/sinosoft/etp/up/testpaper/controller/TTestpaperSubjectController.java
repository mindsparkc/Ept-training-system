package com.sinosoft.etp.up.testpaper.controller;

import cn.jkingtools.utils.BeanUtils;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.utils.RedisUtil;
import com.sinosoft.etp.up.testpaper.domain.TTestPaperSnapshotJson;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import com.sinosoft.etp.up.testpaper.dto.PaperSnapshotDTO;
import com.sinosoft.etp.up.testpaper.dto.TSubjectAnswered;
import com.sinosoft.etp.up.testpaper.dto.TSubjectAnsweredReview;
import com.sinosoft.etp.up.testpaper.service.ITTestpaperStudentService;
import com.sinosoft.etp.up.testpaper.service.ITTestpaperSubjectService;
import com.sinosoft.etp.up.testpaper.vo.PaperSnapshotVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 试卷关联题目Controller
 *
 *
 * @date 2021-12-17
 */
@RestController
@RequestMapping("/etp/testpaperSubject")
public class TTestpaperSubjectController extends BaseController {

    @Autowired
    private ITTestpaperSubjectService tTestpaperSubjectService;

    @Autowired
    private ITTestpaperStudentService testpaperStudentService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 开始考试
     *
     * @param sjid
     * @return
     */
    @GetMapping("/getSubjectByPaper")
    public AjaxResult getSubjectByPaper(Long sjid, Long ksid) {
        /**
         * TODO: 需添加以下逻辑
         * TODO: 1. 考试若未到达开始时间，不允许考试，并提示错误信息：考试还未开始，请在 X天X时X分 之后参加考试
         * TODO: 2. 考试若已结束，不允许考试，并提示错误信息：考试已结束
         */
        Date date = new Date();
        PaperSnapshotDTO snapshotDTO = tTestpaperSubjectService.start(ksid);
        Long kskssj = snapshotDTO.getExamination().getKskssj().getTime();
        Long ksjssj = snapshotDTO.getExamination().getKsjssj().getTime();

        PaperSnapshotVo snapshotVo = BeanUtils.map(snapshotDTO, PaperSnapshotVo.class);
        /*
         * 再 PaperSnapshotDTO 中，试卷题目信息使用 Map 形式保存，这种形式是为了利用 MYSQL 的特性，
         * 根据 key 去更新数据库中的信息，但在 PaperSnapshotVo 中题目是以 list 的形式保存，这是第一阶段设计
         * 的时候的结构，所以在此处对题目进行转换
         */
        Set<Map.Entry<String, TSubjectAnsweredReview>> entrySet = snapshotDTO.getSj().entrySet();
        for (Map.Entry<String, TSubjectAnsweredReview> entry : entrySet) {
            snapshotVo.addQue(BeanUtils.map(entry.getValue(), TSubjectAnswered.class));
        }
        if(kskssj>date.getTime()){
            return AjaxResult.success("考试未开始！");
        }else if(ksjssj<date.getTime()){
            return  AjaxResult.success("考试已结束！");
        }else{
            String userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
            Map<String,Object> map = new HashMap<>();
            map.put("eventType","CLI_EXAM");
            map.put("value",ksid.toString());
            map.put("userId",userId);
            redisUtil.addStream("stream",map);
            return AjaxResult.success(snapshotVo);
        }
        /**
         * 使用 BeanUtils 转换返回给前端的数据结构
         */
    }

    /**
     * 提交考试
     *
     * @return
     */
    @PostMapping("/submitTheExam")
    public AjaxResult submitTheExam(@RequestBody TTestPaperSnapshotJson tTestPaperSnapshotJson) {
        tTestpaperSubjectService.markThePapers(tTestPaperSnapshotJson);
        return AjaxResult.success();
    }

    /**
     * 成绩查询接口
     *
     * @return
     */
    @GetMapping("/resultInquiry/{id}")
    public AjaxResult resultInquiry(@PathVariable("id") String id) {
        return AjaxResult.success(testpaperStudentService.getScore(id));
    }

    /**
     * 查询历史考试
     *
     * @return
     */
    @GetMapping("/historyExam")
    public TableDataInfo getHistoryExam(TTestpaperStudent tTestpaperStudent) {
        startPage();
        List<TTestpaperStudent> historyExam = testpaperStudentService.getHistoryExam(tTestpaperStudent);
        return getDataTable(historyExam, TTestpaperStudent.class);
    }

    /**
     * 查看答题信息
     * @param id
     * @return
     */
    @GetMapping("/selectAnswerInfo")
    public AjaxResult selectAnswerInfo(Long id){

        return AjaxResult.success(testpaperStudentService.selectAnswerInfo(id));
    }
}
