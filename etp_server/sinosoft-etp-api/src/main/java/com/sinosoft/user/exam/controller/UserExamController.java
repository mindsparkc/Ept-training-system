package com.sinosoft.user.exam.controller;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.PaperUnit;
import com.sinosoft.core.domain.PaperUnitItem;
import com.sinosoft.core.domain.Question;
import com.sinosoft.core.domain.QuestionAnswer;
import com.sinosoft.core.enums.QuestionTypeEnum;
import com.sinosoft.user.exam.domain.*;
import com.sinosoft.user.exam.domain.UserExamReqQuery;
import com.sinosoft.user.exam.exception.UserExamException;
import com.sinosoft.user.exam.service.IUserExamService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import oshi.driver.mac.net.NetStat;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 供学员端请求，并开始考试
 *
 *
 *
 * @author wanglijie
 */
@RestController
@RequestMapping("/user/v1/exam")
public class UserExamController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserExamController.class);

    @Resource
    private IUserExamService userExamService;

//    @Resource
//    private IUserPaperService userPaperService;

    /**
     * 请求考试列表
     */
    @PreAuthorize("@ss.hasPermi('user:exam:list')")
    @GetMapping("list")
    public TableDataInfo getExamList(UserExamReqQuery examQuery) {
        if (!examQuery.validated())
            throw new RuntimeException("查询参数无效，请核对输入后重新查询！！！");
        startPage();
        List<UserSimpleExam> exams = userExamService.getExams(examQuery);
        for (UserSimpleExam exam : exams) {
            if (StringUtils.isNotBlank(exam.getCover()))
                exam.setCover(exam.getCover().replace("{storage.path.url}", ResponseFieldUtil.getUrl()));
        }
        return getDataTable(exams, UserSimpleExam.class);
    }

    /**
     * 请求考试基本信息
     */
    @PreAuthorize("@ss.hasPermi('user:exam:query')")
    @GetMapping("/{examId}")
    public AjaxResult getExamInfo(@PathVariable Long examId) {
        UserSimpleExam info = userExamService.getExamInfo(examId);
        // 如果考试有未结束的考试，则在此返回未结束的考试的 record 列表，并且在 /start 中不允许开始新的考试
        UserExamRecordReqQuery query = new UserExamRecordReqQuery();
        query.setExamId(examId);
        query.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        query.setStatus("0");
        info.setRecords(userExamService.queryExamRecord(query));
        if(info.getCover() != null){
            info.setCover(info.getCover().replace("{storage.path.url}", ResponseFieldUtil.getUrl()));
        }

        return AjaxResult.success(info);
    }

    /**
     * 获取考试试卷
     *      需要判断是否有资格参加本次考试
     */
    @PreAuthorize("@ss.hasPermi('user:exam:room')")
    @PostMapping("/start")
    public AjaxResult startExam(@RequestBody UserExamStart userExamStart) {
        UserExam userExam = new UserExam();
        try {
            // 校验是否有权限开始考试，若有权限开始考试则返回考试基本信息
            UserSimpleExam userSimpleExam = userExamService.checkAbleToStartTheExam(userExamStart.getExamId(), userExamStart.getPassword());
            org.apache.commons.beanutils.BeanUtils.copyProperties(userExam, userSimpleExam);
        }catch (UserExamException | IllegalAccessException | InvocationTargetException e) {
            log.info(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }

        // 校验当前考试是否有未结束的
        if (userExamStart.getRecordId() == null) {
            // TODO: 暂时屏蔽该校验
//            List<UserExamRecord> userExamRecords = userExamService.queryExamRecord(userExamStart.getExamId(), "0", null);
//            if (userExamRecords!=null && userExamRecords.size() > 0) {
//                // 存在未结束 的考试，可以选择继续考试
//                userExamStart.setRecordId(userExamRecords.get(0).getRecordId());
//                return AjaxResult.error("请先完成未交卷的考试！");
//            }
        }else {
            // 如果 RecordId 不为 null，则表示是继续之前未结束的考试，需提取快照，直接 return。不走下面生成试卷的逻辑
            userExam = userExamService.queryUserExamByRecordId(userExamStart.getRecordId());
            // 如果是继续考试，不移除学员已答题
            userExam = UserExamRemoveAnswer(userExam, true, true, true,
                    false, true, true);
            return AjaxResult.success(userExam);
        }

        // 生成试卷
        userExam = userExamService.makeupExamContent(userExam);

        // 移除题目答案及解析
        userExam = UserExamRemoveAnswer(userExam, true, true, true,
                true, true, true);
        return AjaxResult.success(userExam);
    }

    /**
     * 提交一个题目的答案
     */
    @PreAuthorize("@ss.hasPermi('user:exam:room')")
    @PostMapping("/submit/answer/{recordId}")
    public AjaxResult submitAnswer(@PathVariable Long recordId, @RequestBody PaperUnitItem unitItem) {
        userExamService.submitAnswer(recordId, unitItem);
        return AjaxResult.success();
    }

    /**
     * 提交试卷
     */
    @PreAuthorize("@ss.hasPermi('user:exam:room')")
    @PostMapping("/giveup/{recordId}")
    public AjaxResult GiveUpExam(@PathVariable Long recordId) {
        // 放弃考试 - 加载 Redis 中的答题，并保存学员答题快照
        userExamService.endExam(recordId, "3", "学员主动放弃考试");
        return AjaxResult.success();
    }

    /**
     * 提交试卷
     */
    @PreAuthorize("@ss.hasPermi('user:exam:room')")
    @PostMapping("/submit")
    public AjaxResult submitExam(@RequestBody UserExam userExam) {
        // TODO: 判断是否允许交卷
        // 提交试卷，将试卷保存至数据库
        UserExam exam = userExamService.makeupUserExam(userExam, "1", null);
        Map<String, Object> result = new HashMap<>();
        result.put("examId", exam.getExamId());
        result.put("name", exam.getName());
        result.put("showScore", exam.getShowScore());
        if (exam.getShowScore().equals(1)) {
            result.put("userScore", exam.getUserScore());
        }
        boolean userScore = !exam.getShowScore().equals("1");
        UserExamRemoveAnswer(exam, userScore, true, true, false, true, true);
        // showScore @Excel(name = "成绩显示", readConverterExp = "0=不显示成绩,1=立即显示,2=批改后显示,3=定时发布,4=人工发布")
        return AjaxResult.success(result);
    }

    /**
     * 查询某条考试记录，及快照
     */
    @PreAuthorize("@ss.hasPermi('user:exam:record')")
    @GetMapping("/record/{recordId}")
    public AjaxResult queryExamByRecordId(@PathVariable Long recordId) {
        // 查询考试成绩
        UserExamRecordResp resp = userExamService.queryExamByRecordId(recordId);

//        showScore @Excel(name = "成绩显示", readConverterExp = "0=不显示成绩,1=立即显示,2=批改后显示,3=定时发布,4=人工发布")

        // 根据是否允许查看答案、解析等移除数据
        /** showExam查看试卷（0不允许查看 1不显示答案及解析 2仅显示答案 3显示答案及解析） */
        /** 显示试卷评语（0不显示 1显示） */
        boolean userScore = false;    // 移除学员成绩
        boolean review = false;       // 移除判卷评语
        boolean rightFlag = false;    // 移除学员是否答对的判卷
        boolean userAnswer = false;   // 移除选择与判断学员是否答对
        boolean rightAnswer = false;  // 移除参考答案
        boolean analysis = false;      // 移除题目解析
        if (resp.getExam().getShowExam().equals("0")) {
            // 不允许查看试卷
            return AjaxResult.error("该场考试已设置为不允许查看历史考试详情！");
        }else if (resp.getExam().getShowExam().equals("1")) {
            analysis = true;     // 不显示解析
            rightAnswer = true;  // 不显示正确答案
        }else if (resp.getExam().getShowExam().equals("2")) { analysis = true;     // 不显示解析
        }else if (resp.getExam().getShowExam().equals("3")) {
            // 显示答案及解析
        }

        if (resp.getExam().getShowComment().equals("0")) review = true; // 不显示判卷记录

        /** resp.getStatus() 试卷状态（0未交卷 1未判卷 4未发布 8放弃考试 9已完成） */
        /** resp.getExam().getShowScore() 0=不显示成绩,1=立即显示,2=批改后显示,3=定时发布,4=人工发布 */
        if (resp.getExam().getShowScore().equals("0")) userScore = true;
        else if (resp.getExam().getShowScore().equals("2") && !resp.getStatus().equals("9")) userScore = true;


        UserExam userExam = UserExamRemoveAnswer(resp.getExam(), userScore, review, rightFlag, userAnswer, rightAnswer, analysis);
        resp.setExam(userExam);

        return AjaxResult.success(resp);
    }

    /**
     * 查看历史考试记录，根据考试 ID 或学员 ID 查询
     */
    @PreAuthorize("@ss.hasPermi('user:exam:history')")
    @GetMapping("/history")
    public TableDataInfo historyExamRecord(@RequestParam(value = "examId", required = false) Long examId,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "range",required = false) List<String> range,
                                           @RequestParam(value = "status",required = false) String status) {
        // 查询考试列表
        startPage();
        List<UserExamRecord> userExamRecords = userExamService.queryExamRecord(examId, name, status, range);
        return getDataTable(userExamRecords);
    }



    /**
     * 移除题目答案及解析
     *
     * @param userExam
     * @return
     */
    private UserExam UserExamRemoveAnswer(UserExam userExam,
                                          boolean userScore,    // 移除学员成绩
                                          boolean review,       // 移除判卷评语
                                          boolean rightFlag,    // 移除学员是否答对的判卷
                                          boolean userAnswer,   // 移除选择与判断学员是否答对
                                          boolean rightAnswer,  // 移除参考答案
                                          boolean analysis      // 移除题目解析
                                   ) {
        // 移除组卷规则
        List<PaperUnit> paperUnits = userExam.getPaperUnit();
        for (PaperUnit paperUnit : paperUnits) {
            paperUnit.setUnitRule(null);
            List<PaperUnitItem> questions = paperUnit.getQuestions();
            if (questions == null || questions.size() == 0) continue;
            for (PaperUnitItem question : paperUnit.getQuestions()) {
                // 移除题目中学员考试不得获取到的内容
                question = UserExamRemoveAnswer0(question,
                        userScore, review, rightFlag, userAnswer, rightAnswer, analysis);
                List<Question> children = question.getChildren();
                if (children != null) {
                    for (Question child : children) {
                        UserExamRemoveAnswer0(child,
                                userScore, review, rightFlag, userAnswer, rightAnswer, analysis);
                    }
                }

            }
        }

        return userExam;
    }

//    private UserExam UserExamRemoveAnswer(UserExam userExam) {
//        // 移除组卷规则
//        List<PaperUnit> paperUnits = userExam.getPaperUnit();
//        for (PaperUnit paperUnit : paperUnits) {
//            paperUnit.setUnitRule(null);
//            List<PaperUnitItem> questions = paperUnit.getQuestions();
//            if (questions == null || questions.size() == 0) continue;
//            for (PaperUnitItem question : paperUnit.getQuestions()) {
//                // 移除题目中学员考试不得获取到的内容
//                question = UserExamRemoveAnswer0(question);
//                List<Question> children = question.getChildren();
//                for (Question child : children) {
//                    UserExamRemoveAnswer0(child);
//                }
//            }
//        }
//
//        return userExam;
//    }

    /**
     * 移除 Answer 的 content 和 contents 和 rightFlag
     * @return
     */
    private PaperUnitItem UserExamRemoveAnswer0(PaperUnitItem question,
                                                boolean userScore,    // 移除学员成绩
                                                boolean review,       // 移除判卷评语
                                                boolean rightFlag,    // 移除学员是否答对的判卷
                                                boolean userAnswer,   // 移除选择与判断学员是否答对
                                                boolean rightAnswer,  // 移除参考答案
                                                boolean analysis ) {  // 移除题目解析
        question.setRule(null);
        if (analysis) question.setAnalysis(null);    // 题目解析
        if (review) question.setReview(null);        // 判卷评语
        if (userScore) question.setUseScore(null);   // 题目分数
        if (rightFlag) question.setRightFlag(false); // 学员是否答对
        if (question.getAnswers() != null) {
            for (QuestionAnswer answer : question.getAnswers()) {
                if (analysis) answer.setAnalysis(null);  // 选项解析
                if (StringUtils.equalsAny(question.getType(), QuestionTypeEnum.SINGLE_CHOICE.getCode(),
                        QuestionTypeEnum.MULTIPLE_CHOICE.getCode(), QuestionTypeEnum.TRUE_FALSE.getCode())) {
                    // rightFlag 全部设置 false
                    if (rightAnswer) answer.setRightFlag(false); // 是否正确选项
                    if (userAnswer) answer.setSelectFlag(false); // 学员是否选中
                }else {
                    // 非多选、单选、判断，移除 content 字段，因为如填空、问答该字段可能保存有参考答案
                    // 但选择、判断中 content 保存选项的内容，不能移除
                    if (rightAnswer) {
                        answer.setContent(""); // 填空题答案
                        answer.setContents(new ArrayList<>());
                    }
                }
                if (userAnswer) answer.setSelectFlag(false); // 是否选中全部设置为 false
            }
        }
        question.setRemark(null);
        return question;
    }

    private Question UserExamRemoveAnswer0(Question question,
                                                boolean userScore,    // 移除学员成绩
                                                boolean review,       // 移除判卷评语
                                                boolean rightFlag,    // 移除学员是否答对的判卷
                                                boolean userAnswer,   // 移除选择与判断学员是否答对
                                                boolean rightAnswer,  // 移除参考答案
                                                boolean analysis ) {  // 移除题目解析
        if (analysis) question.setAnalysis(null);    // 题目解析
        if (review) question.setReview(null);        // 判卷评语
        if (userScore) question.setUseScore(null);   // 题目分数
        if (rightFlag) question.setRightFlag(false); // 学员是否答对
        if (question.getAnswers() != null) {
            for (QuestionAnswer answer : question.getAnswers()) {
                if (analysis) answer.setAnalysis(null);  // 选项解析
                if (StringUtils.equalsAny(question.getType(), QuestionTypeEnum.SINGLE_CHOICE.getCode(),
                        QuestionTypeEnum.MULTIPLE_CHOICE.getCode(), QuestionTypeEnum.TRUE_FALSE.getCode())) {
                    // rightFlag 全部设置 false
                    if (rightAnswer) answer.setRightFlag(false); // 是否正确选项
                    if (userAnswer) answer.setSelectFlag(false); // 学员是否选中
                }else {
                    // 非多选、单选、判断，移除 content 字段，因为如填空、问答该字段可能保存有参考答案
                    // 但选择、判断中 content 保存选项的内容，不能移除
                    if (rightAnswer) {
                        answer.setContent(null); // 填空题答案
                        answer.setContents(null);
                    }
                }
                if (userAnswer) answer.setSelectFlag(false); // 是否选中全部设置为 false
            }
        }
        question.setRemark(null);
        return question;
    }
}
