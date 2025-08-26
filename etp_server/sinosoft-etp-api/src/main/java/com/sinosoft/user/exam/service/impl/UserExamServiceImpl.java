package com.sinosoft.user.exam.service.impl;

import cn.jkingtools.utils.ArrayUtils;
import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.core.redis.RedisCache;
import com.sinosoft.common.exception.CustomException;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.*;
import com.sinosoft.core.enums.*;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.user.exam.domain.*;
import com.sinosoft.user.exam.domain.UserExamReqQuery;
import com.sinosoft.user.exam.exception.UserExamException;
import com.sinosoft.user.exam.mapper.*;
import com.sinosoft.user.exam.service.IUserExamService;
import com.sinosoft.user.exam.task.core.MsgEvent;
import org.apache.commons.beanutils.BeanUtils;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 学员用户查询考试信息的Service层实现类
 *
 * @author wanglijie
 */
@Service
public class UserExamServiceImpl implements IUserExamService {
    private static final Logger log = LoggerFactory.getLogger(UserExamServiceImpl.class);

    @Resource
    UserExamMapper userExamMapper;
    @Resource
    UserPaperUnitMapper userPaperUnitMapper;
    @Resource
    UserPaperMapper userPaperMapper;
    @Resource
    UserQuestionMapper userQuestionMapper;
    @Resource
    UserExamRecordMapper userExamRecordMapper;
    @Resource
    UserExamRecordSnapshotMapper userExamRecordSnapshotMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询考试列表
     * @param examQuery
     * @return
     */
    @EtpDataScope(alias = "ex") //可能用在两处，两个地方都要是ex
    @Override
    public List<UserSimpleExam> getExams(UserExamReqQuery examQuery) {
        examQuery.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        examQuery.setPostIds(SecurityUtils.getLoginUser().getUser().getPostIds());
        examQuery.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());

        List<UserSimpleExam> examList = userExamMapper.selectUserExamList(examQuery);

        for (UserSimpleExam exam : examList) {
            boolean pwdFlag = StringUtils.isNotBlank(exam.getPassword());
            exam.setPwdFlag(pwdFlag);
            exam.setPassword(null);
        }

        return examList;
    }

    /**
     * 查询考试详情
     *
     * @param examId
     * @return
     */
    @Override
    public UserSimpleExam getExamInfo(Long examId) {
        UserExamReqQuery query = new UserExamReqQuery();
        query.setExamId(examId);
        query.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        query.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        query.setPostIds(SecurityUtils.getLoginUser().getUser().getPostIds());
        //设置数据权限
        query.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, EtpDataScopeUtils.getDataScopeSql("ex"));
        UserSimpleExam userExam = userExamMapper.selectUserExamInfo(query);
        if (userExam == null) throw new RuntimeException("获取考试信息失败[" + examId + "]");

        boolean pwdFlag = StringUtils.isNotBlank(userExam.getPassword());
        userExam.setPwdFlag(pwdFlag);
        userExam.setPassword(null);

        userExam.setTotalScore(getPaper(userExam.getPaperId()).getTotalScore());
        userExam.setPaperUnit(getSimplePaperUnit(userExam.getPaperId()));
        return userExam;
    }

    /**
     * 查询用户即将结束的考试
     *
     * @return
     */
    @Override
    public UserSimpleExam getComingEnd() {
        UserExamReqQuery query = new UserExamReqQuery();
        query.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        query.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        query.setPostIds(SecurityUtils.getLoginUser().getUser().getPostIds());
        //设置数据权限
        query.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, EtpDataScopeUtils.getDataScopeSql("ex"));
        UserSimpleExam userExam = userExamMapper.selectComingEndExam(query);
        //if (userExam == null) throw new RuntimeException("暂无考试");
        return userExam;
    }

    /**
     * 获取试卷基础信息
     *
     * @param paperId
     * @return
     */
    private Paper getPaper(Long paperId) {
        Paper paper = userPaperMapper.selectPaperById(paperId);
        if (paper == null) throw new RuntimeException("查询试卷信息失败["+ paperId + "]");
        return paper;
    }

    /**
     * 获取简单的试卷组织结构，仅包含大题及大题成绩和题目数量
     *
     * @param paperId
     * @return
     */
    private List<UserPaperUnit> getSimplePaperUnit (Long paperId){
        // 补充试卷的基本信息，各单元题目数量
        List<PaperUnit> paperUnits = userPaperUnitMapper.selectPaperUnitByPaperId(paperId);
        if (paperUnits == null || paperUnits.size() == 0) throw new RuntimeException("查询试卷结构信息失败["+ paperId + "]");
        List<UserPaperUnit> paperUnit = new ArrayList<>();
        try {
            for (PaperUnit unit : paperUnits) {
                UserPaperUnit tmp = new UserPaperUnit();
                BeanUtils.copyProperties(tmp, unit);
                paperUnit.add(tmp);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return paperUnit;
    }

    /**
     * 校验是否有权限开始考试
     * 校验的内容包括
     * 1. 用户归属的部门或岗位是否有权限进行考试（在 selectUserExamInfo 中增加权限过滤处理）
     * 2. 是否已达到规定的考试时间
     * 3. 口令密码是否正确
     *
     * @return 如果通过校验，则会返回 UserSimpleExam 对象，为考试的基本信息
     */
    @Override
    public UserSimpleExam checkAbleToStartTheExam(Long examId, String password) {
        // 判断用户是否能加载到考试信息
        // 1. 公开 2.限定部门 4.限定岗位 5.口令登录
        // 学员已消耗考试机会/本次考试机会
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        UserExamReqQuery query = new UserExamReqQuery();
        query.setExamId(examId);
        query.setUserId(userId);
        query.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        query.setPostIds(SecurityUtils.getLoginUser().getUser().getPostIds());

        //设置数据权限
        query.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, EtpDataScopeUtils.getDataScopeSql("ex"));
        UserSimpleExam userExam = userExamMapper.selectUserExamInfo(query);
        if (userExam == null) {
            throw new UserExamException("未能获取到考试信息，请联系部门管理员！" + examId);
        }

        // 校验口令密码
        if (userExam.getOpenType().equals(OpenTypeEnum.PASSWD.getCode())) {
            boolean matchesPassword = SecurityUtils.matchesPassword(password, userExam.getPassword());
            if (!matchesPassword) throw new UserExamException("密码错误，请校验后重新输入！" + examId);
        }


        checkAbleToStartTheExam0(userExam, userId);

        boolean pwdFlag = StringUtils.isNotBlank(userExam.getPassword());
        userExam.setPwdFlag(pwdFlag);
        userExam.setPassword(null);
        return userExam;
    }

    /**
     * 校验考试是否已可以开始
     *
     * @param userExam
     */
    private void checkAbleToStartTheExam0(Exam userExam, Long userId) {
        Date nowDate = DateUtils.getNowDate();
        if (userExam.getTimeLimit().equals("1")) {
            if (userExam.getStartTime() == null) {
                throw new UserExamException("考试配置错误（未配置考试开始时间），请联系管理员");
            }
            // 限制考试开始时间，需要判断 startTime <= now()，且不超过 lateTime 允许迟到的时间
            if (nowDate.compareTo(userExam.getStartTime()) < 0) {
                throw new UserExamException("还未到达考试开始时间，请稍后再试！");
            }
            if(userExam.getLateTime() != null && userExam.getLateTime() != 0) {
                Date lateDate = DateUtils.addMinutes(userExam.getStartTime(), userExam.getLateTime());
                if (nowDate.compareTo(lateDate) > 0) {
                    throw new UserExamException("您已迟到，且已超过允许的迟到时间，不允许开始考试！");
                }
            }
        }else {
            // 不限制考试开始时间，则 pushedTime <= now() 即可开始
            if (nowDate.compareTo(userExam.getPublishTime()) <= 0) {
                throw new UserExamException("还未到达考试开始时间，请稍后再试！");
            }
        }

        // 判断考试是否已结束
        if (userExam.getEndTime()!= null && nowDate.compareTo(userExam.getEndTime()) > 0) {
            throw new UserExamException("该场考试已结束！");
        }

        // 判断考试次数是否已达上限
        UserExamRecordReqQuery query = new UserExamRecordReqQuery();
        query.setExamId(userExam.getExamId());
        query.setUserId(userId);
        if (userExam.getChance() != 0) {
            List<UserExamRecord> userExamRecords = userExamRecordMapper.selectUserExamRecord(query);
            if (userExamRecords.size() >= userExam.getChance()) {
                throw new RuntimeException("已达考试次数上限（" + userExam.getChance() + ")");
            }
        }
    }

    /**
     * 仅组织试卷
     * @param userExam
     * @return
     */
    @Override
    public UserExam makeupExamContent0(UserExam userExam) {
        Paper paper = userPaperMapper.selectPaperById(userExam.getPaperId());
        if (paper == null) throw new RuntimeException("查询试卷信息失败[" + userExam.getExamId() +"," + userExam.getPaperId() + "]");

        userExam.setPaper(paper);
        userExam.setTotalScore(paper.getTotalScore());
        // 获取考试单元
        List<PaperUnit> paperUnits = userPaperUnitMapper.selectPaperUnitByPaperId(userExam.getPaperId());

        // TODO: 保存已经加入到试卷中的题目，需要将固定题目先放到 exists 里面
        List<Long> exists = new ArrayList<>();

        String querySQL = buildQueryQuestionSql(paperUnits, exists);
        if (StringUtils.isBlank(querySQL)) {
            if (paper.getStatus().equals("99"))
                throw new RuntimeException("试卷信息提取错误，请与管理员联系【"  + userExam.getExamId() +"," + userExam.getPaperId() +  "】");
            else return userExam;
        }
        // 如果是随机组卷，getRuleId() 会对应多个题目，这时候需要将 Value 处理成一个数组
        Map<String, List<UserPaperUnitItemQuestion>> questionMap = userQuestionMapper.selectPaperUnitItemQuestionResult(querySQL)
                .stream().collect(Collectors.toMap(UserPaperUnitItemQuestion::getKey,
                        question -> { return ArrayUtils.transferArrayToList(question);},
                        (List<UserPaperUnitItemQuestion> o, List<UserPaperUnitItemQuestion> n) -> {o.addAll(n); return o;}));



        int quCount = 0;
        for (PaperUnit paperUnit : paperUnits) {
            List<PaperUnitItem> unitItems = paperUnit.getUnitRule();
            List<PaperUnitItem> questions = new ArrayList<>();
            int paperUnitQuCount = 0;
            if (unitItems == null || unitItems.size() == 0) continue;
            for (PaperUnitItem unitItem : unitItems) {
                if (unitItem.getStrategy().equals(PaperStrategyEnum.RANDOM.getCode())) {
                    // 如果是随机组卷，根据 questionMap 的 key 提取题目 "ruleId_level"
                    for (LevelAndCount levelAndCount : unitItem.getRule().getLc()) {
                        if(levelAndCount.getCount() == 0) continue;
                         List<UserPaperUnitItemQuestion> itemQuestions = questionMap.get(unitItem.getRuleId() + "_" + levelAndCount.getLevel());
                          if (itemQuestions == null || itemQuestions.size() < levelAndCount.getCount())
                            throw new RuntimeException("["+paperUnit.getDisplay() + "]不能满足组卷规则！");
                        for (int i=0, j=0; i< levelAndCount.getCount(); ) {
                            if (j>=itemQuestions.size()) {
                                throw new RuntimeException("随机组卷规则会获取到重复题目，不能满足组卷规则！");
                            }
                            UserPaperUnitItemQuestion question = itemQuestions.get(j);

                            PaperUnitItem item = new PaperUnitItem();
                            try {
                                BeanUtils.copyProperties(item, question);
                            } catch (IllegalAccessException |InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            item.setScore(unitItem.getScore() == null?paperUnit.getEachScore():unitItem.getScore());
                            item.setRuleId(unitItem.getRuleId());
                            item.setPaperId(unitItem.getPaperId());
                            item.setPaperUnitId(unitItem.getPaperUnitId());
                            item.setStrategy(unitItem.getStrategy());
                            // 若是选项随机，对 answers 排序
                            if(StringUtils.isNotBlank(paperUnit.getAnswerOrderFlag()) &&
                                    paperUnit.getAnswerOrderFlag().equals("1")) {
                                List<QuestionAnswer> answers = item.getAnswers().stream()
                                        .map(answer -> {
                                            answer.setSort(new Random().nextInt());
                                            return answer;
                                        })
                                        .sorted(Comparator.comparing(QuestionAnswer::getSort)).collect(Collectors.toList());
                                item.setAnswers(answers);
                            }
                            if (!exists.contains(item.getQuId())) {
                                exists.add(item.getQuId());
                                questions.add(item);
                                paperUnitQuCount++;
                                i++;// 当填充题目完毕，结束循环
                            }else {
                                System.out.println("出现重复");
                            }
                            j++; // 当遍历完题目列表，也没有符合要求的题目，结束循环

                        }
                    }

                }else {
                    List<UserPaperUnitItemQuestion> userPaperUnitItemQuestions = questionMap.get(unitItem.getQuId().toString());
                    if (userPaperUnitItemQuestions != null && userPaperUnitItemQuestions.size() > 0) {
                        PaperUnitItem item = new PaperUnitItem();
                        try {
                            UserPaperUnitItemQuestion userPaperUnitItemQuestion = userPaperUnitItemQuestions.get(0);
                            BeanUtils.copyProperties(item, userPaperUnitItemQuestion);
                            /* 分数的处理，如果 unitItem.getScore() == null，取单元默认分 */
                            item.setScore(unitItem.getScore() == null?paperUnit.getEachScore():unitItem.getScore());
                            item.setRuleId(unitItem.getRuleId());
                            item.setPaperId(unitItem.getPaperId());
                            item.setPaperUnitId(unitItem.getPaperUnitId());
                            item.setStrategy(unitItem.getStrategy());
                            // 子题目的分值
                            for (int i=0; item.getChildren()!=null && i<item.getChildren().size(); i++ ) {
                                Question child = item.getChildren().get(i);
                                child.setScore(unitItem.getChildren().get(i).getScore());
                                paperUnitQuCount++; // 子题目也单独计数，作为总题目数
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        // 若是选项随机，对 answers 排序
                        if(StringUtils.isNotBlank(paperUnit.getAnswerOrderFlag()) &&
                                paperUnit.getAnswerOrderFlag().equals("1")) {
                            List<QuestionAnswer> answers = item.getAnswers().stream()
                                    .map(answer -> {
                                        answer.setSort(new Random().nextInt());
                                        return answer;
                                    })
                                    .sorted(Comparator.comparing(QuestionAnswer::getSort)).collect(Collectors.toList());
                            item.setAnswers(answers);
                        }
                        exists.add(item.getQuId());
                        questions.add(item);
                        paperUnitQuCount++;
                    }
                }
                quCount+=paperUnitQuCount;
            }
            paperUnit.setQuCount(paperUnitQuCount);
            paperUnit.setUnitRule(unitItems);
            // 如果是题目顺序随机，对 questions 进行排序
            if(StringUtils.isNotBlank(paperUnit.getQuOrderFlag()) &&
                    paperUnit.getQuOrderFlag().equals("1")) {
                questions = questions.stream()
                        .map(q -> {
                            q.setSort(new Random().nextInt());
                            return q;
                        })
                        .sorted(Comparator.comparing(PaperUnitItem::getSort)).collect(Collectors.toList());
            }
            // 处理一下子题目，将 paperUnitId 拼接到子题目上
            for (PaperUnitItem question : questions) {
                if (question.getChildren()==null || question.getChildren().size()==0) continue;
                for (Question qu: question.getChildren()) {
                    qu.setPaperId(question.getPaperId());
                    qu.setPaperUnitId(question.getPaperUnitId());
                }
            }

            paperUnit.setQuestions(questions);
        }
        userExam.setPaperUnit(paperUnits);
        userExam.setQuCount(quCount);
        return userExam;
    }
    /**
     * 开始考试，组织试卷
     *
     * @param userExam
     * @return
     */
    @Override
    public UserExam makeupExamContent(UserExam userExam) {

        userExam = makeupExamContent0(userExam);
        // 设置考试记录 ID，将会被用来
        userExam.setRecordId(IdUtil.nextId());

        // 保存试卷记录
        UserExamRecord userExamRecord = new UserExamRecord();
        userExamRecord.setExamId(userExam.getExamId());
        userExamRecord.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        userExamRecord.setRecordId(userExam.getRecordId());
        userExamRecord.setTotalScore(userExam.getTotalScore());
        userExamRecord.setStartTime(DateUtils.getNowDate());

        if (userExam.getTotalTime() != null && userExam.getTotalTime() != 0L){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userExamRecord.getStartTime());
            calendar.add(Calendar.MINUTE, userExam.getTotalTime());
            userExamRecord.setLogicEndTime(calendar.getTime());
        }else if(userExam.getEndTime() != null) { // 如果不限制考试总时长，就需要判断考试结束时间，作为强制交卷逻辑时长
            userExamRecord.setLogicEndTime(userExam.getEndTime());
        }
        // 未交卷时，这里设置的逻辑上应结束的时间
        userExamRecord.setHandType("0");
        userExamRecord.setStatus("0"); // 未交卷
        userExamRecord.setQuCount(userExam.getQuCount()); // 题目数量
        userExamRecordMapper.insertExamRecord(userExamRecord);

        // 保存完整快照
        UserExamRecordSnapshot snapshot = new UserExamRecordSnapshot();
        snapshot.setRecordId(userExam.getRecordId());
        snapshot.setSnapshot(userExam);
        userExamRecordSnapshotMapper.insertExamRecordSnapshot(snapshot);

        // 将定时交卷任务发送至时间轮
        if (userExamRecord.getLogicEndTime() != null) {
            long delay = (userExamRecord.getLogicEndTime().getTime()-(new Date()).getTime())/1000;
            EventBus.getDefault().post(new MsgEvent(userExamRecord.getRecordId(), "开始考试 - 定时交卷 - " + delay,
                    delay));
        }

        return userExam;
    }

    private String buildQueryQuestionSql(List<PaperUnit> paperUnits, List<Long> exists) {
        // 如果是选题或抽题组卷，ruleId 即为 qu_id
        StringBuilder inSql = new StringBuilder("select qu_id `key`, qu_id as ruleId, qu_id, parent_id, version, content, display, type, level, FLOOR(1 + RAND() * (10000 - 1 + 1)) as sort,\n" +
                "               analysis, tarin_flag, exam_flag, status, expire_time, user_id, dept_id,\n" +
                "               del_flag, create_by, create_time, update_by, update_time, remark ,answers, children\n" +
                "        from question qu where del_flag=0 and status='0' and qu.qu_id in (");

        StringBuilder querySql = new StringBuilder();
        boolean hasQu = false;
        boolean hasRandom = false;
        AtomicInteger index = new AtomicInteger();
        // 遍历，获取 quIds
        StringBuilder quIds = new StringBuilder();
        for (PaperUnit paperUnit : paperUnits) {
            if (paperUnit.getUnitRule() == null || paperUnit.getUnitRule().size() == 0) continue;
            for (PaperUnitItem unitItem : paperUnit.getUnitRule()) {
                if (!org.apache.commons.lang3.StringUtils.equalsAny(unitItem.getStrategy(),
                        PaperStrategyEnum.RANDOM.getCode())) {
                    hasQu = true;
                    quIds.append(unitItem.getQuId()).append(",");

                    if (exists.contains(unitItem.getQuId())) {
                        throw new RuntimeException("选择的题目存在重复题目，请校验！");
                    }
                    exists.add(unitItem.getQuId());
                }
            }
        }
        if (hasQu) quIds = quIds.deleteCharAt(quIds.length()-1);
        for (PaperUnit paperUnit : paperUnits) {
            if (paperUnit.getUnitRule() == null || paperUnit.getUnitRule().size() == 0) continue;
            for (PaperUnitItem unitItem : paperUnit.getUnitRule()) {
                if (org.apache.commons.lang3.StringUtils.equalsAny(unitItem.getStrategy(),
                        PaperStrategyEnum.RANDOM.getCode())) {
                    hasRandom = true;
                    // 随机，调用 buildQueryQuestionSql0 生成查询 SQL
                    List<String> sql0 = buildQueryQuestionSql0(unitItem, paperUnit.getQuType(), quIds.toString());
                    if (sql0.size() != 0) {
                        sql0.stream().forEach(s->{
                            index.getAndIncrement();
                            querySql.append("select * from (").append(s).append(") ").append("a").append(index.get()).append(" union all ");
                        });
                    }
                }else {
                    hasQu = true;
                    inSql.append(unitItem.getQuId()).append(",");
                }
            }
        }

        inSql.deleteCharAt(inSql.length()-1).append(")");

        if (hasRandom && !hasQu)
            querySql.delete(querySql.length()-11, querySql.length()); // 删除最后一个 union all

        if (hasQu) querySql.append(" ").append(inSql.toString());

        return querySql.toString();
    }

    /**
     * 生成取随机试卷的 SQL
     *
     * @param item
     * @return
     */
    private List<String> buildQueryQuestionSql0(PaperUnitItem item, String quType, String quIds) {

        if (item.getRule() == null) return null;
        List<String> query = new ArrayList<>();
        for (LevelAndCount levelAndCount : item.getRule().getLc()) {
            StringBuilder query0 = new StringBuilder("select concat_ws('_', '"+ item.getRuleId() + "',level) `key`, " + item.getRuleId() + " ruleId, qu_id, parent_id, version, content, display, type, level, FLOOR(1 + RAND() * (10000 - 1 + 1)) as sort,\n" +
                    "               analysis, tarin_flag, exam_flag, status, expire_time, user_id, dept_id,\n" +
                    "               del_flag, create_by, create_time, update_by, update_time, remark ,answers, children\n" +
                    "        from question qu where  qu.del_flag=0 and qu.status='0' and qu.type ='"+quType+"' and ");
            if (quIds.length()>0) query0.append(" qu_id not in (").append(quIds).append(") and ");
            Long catId = item.getRule().getCatId().get(item.getRule().getCatId().size() - 1);
            query0.append(" `level` = " + levelAndCount.getLevel()).append(" and qu.qu_id in ( \n" +
                    "                select qic.obj_id from question_inter_category qic, question_category qc \n" +
                    "                where qic.cat_id = qc.cat_id and (qic.cat_id = ")
                    .append(catId)
                    .append(" or find_in_set('").append(catId).append("', qc.ancestors))) order by sort limit ")
                    .append(levelAndCount.getCount()*5);

            query.add(query0.toString());
        }

        return query;
    }

    /**
     * 将学员提交的答案与试卷快照合并
     *
     * @param userExam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserExam makeupUserExam(UserExam userExam, String handType, String remark) {
        UserExamReqQuery query = new UserExamReqQuery();
        query.setExamId(userExam.getExamId());
        query.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        query.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        query.setPostIds(SecurityUtils.getLoginUser().getUser().getPostIds());

        //设置数据权限
        query.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, EtpDataScopeUtils.getDataScopeSql("ex"));
        UserSimpleExam exam = userExamMapper.selectUserExamInfo(query);

        // 获取学员所有题目答案，并转为 Map<unitId_quId, value>
        Map<String, PaperUnitItem> userUnitItemMap = getUserQuestion(userExam);

        // UserExam
        UserExamRecordSnapshot snapshot = userExamRecordSnapshotMapper.selectExamRecordSnapshotById(userExam.getRecordId());
        UserExam resultExam = snapshot.getSnapshot();
        resultExam.setRecordId(userExam.getRecordId());

        // 合并学员答案至快照，并判卷
        resultExam = makeupUserExam0(resultExam, userUnitItemMap);

        // 更新试卷记录
        UserExamRecord userExamRecord = userExamRecordMapper.selectExamRecordById(userExam.getRecordId(), SecurityUtils.getLoginUser().getUser().getUserId());
        userExamRecord.setRecordId(resultExam.getRecordId());
        userExamRecord.setTotalScore(resultExam.getTotalScore()); // 试卷得分
        userExamRecord.setEndTime(DateUtils.getNowDate()); // 交卷时间\
        userExamRecord.setDoTime((userExamRecord.getEndTime().getTime() - userExamRecord.getStartTime().getTime())/1000);
        userExamRecord.setUserScore(resultExam.getUserScore());      // 设置学员得分
        userExamRecord.setQuCorrect(resultExam.getQuCorrect());
        userExamRecord.setHandType(handType);                  // 交卷方式
        userExamRecord.setRemark(remark);
        // 判断是否需要判卷
        // 试卷状态（ 0未交卷 1未判卷 4未发布 9已完成）
        // 成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
        // userExamRecord.setStatus("1");
        // 0不显示成绩 9已完成
        if (StringUtils.equalsAny(exam.getShowScore(),
                ExamShowScoreEnum.NoShowScore.getCode(),  // 不显示成绩
                ExamShowScoreEnum.ImShowScore.getCode())) // 立即显示成绩
            userExamRecord.setStatus(ExamRecordStatusEnum.DONE.getCode()); // 已完成
        else if (StringUtils.equalsAny(exam.getShowScore(),
                ExamShowScoreEnum.Judgmented.getCode()))  // 批改后显示成绩
            userExamRecord.setStatus(ExamRecordStatusEnum.NOT_JUDGED.getCode()); // 未判卷
        else if (StringUtils.equalsAny(exam.getShowScore(),
                ExamShowScoreEnum.TimingPublished.getCode(),   // 定时发布
                ExamShowScoreEnum.ManualPublished.getCode()))  // 人工发布
            userExamRecord.setStatus(ExamRecordStatusEnum.UNPUBLISHED.getCode()); // 已交卷，但未发布

        userExamRecordMapper.updateExamRecord(userExamRecord);

        // 更新完整快照
        UserExamRecordSnapshot recordSnapshot = new UserExamRecordSnapshot();
        recordSnapshot.setRecordId(userExam.getRecordId());
        recordSnapshot.setSnapshot2(resultExam);
        userExamRecordSnapshotMapper.updateExamRecordSnapshot(recordSnapshot);

        // 交卷后，移除 Redis 中的记录
        String recordKey = "EXAM_RECORD_" + exam.getRecordId();
        String key = "EXAM_RECORD_QU_" + exam.getRecordId();
        redisCache.deleteObject(recordKey);
        redisCache.deleteObject(key);
        return resultExam;
    }

    private UserExam makeupUserExam0(UserExam resultExam, Map<String, PaperUnitItem> userUnitItemMap){
        // 答对题目数量
        Integer quCorrect = 0;
        BigDecimal examTotalScore = BigDecimal.ZERO;      // 试卷总分
        for (PaperUnit paperUnit : resultExam.getPaperUnit()) {
            BigDecimal unitTotalScore = BigDecimal.ZERO;  // 单元总分
            for (PaperUnitItem question : paperUnit.getQuestions()) {
                BigDecimal userScore = BigDecimal.ZERO; // 单题目得分
                // 如果是组合题，处理子题目，通过 for 循环遍历子题目
                if (question.getType().equals(QuestionTypeEnum.COMBINATORIAL.getCode())) {
                    for (Question child : question.getChildren()) {
                        userAnswer2Snapshot(paperUnit, child, userUnitItemMap);
                        // 判断学员是否答对
                        BigDecimal score = checkAnswer(child);
                        child.setUseScore(score);
                        userScore=userScore.add(score);
                        if (question.isRightFlag()) quCorrect++;
                    }
                }else {
                    userAnswer2Snapshot(paperUnit, question, userUnitItemMap);    // 将学员答案拷贝到快照中
                    // 判断学员是否答对
                    userScore=userScore.add(checkAnswer(question));
                }
                if (question.isRightFlag()) quCorrect++;
                question.setUseScore(userScore);      // 设置题目得分
                unitTotalScore=unitTotalScore.add(userScore);        // 题目分值累计，得单元分值
            }
            paperUnit.setUserScore(unitTotalScore);  // 设置单元得分
            examTotalScore=examTotalScore.add(unitTotalScore);       // 单元分值累计，的试卷分值
        }
        resultExam.setUserScore(examTotalScore);       // 设置试卷总分
        resultExam.setQuCorrect(quCorrect);
        return resultExam;
    }

    /**
     * 将学员答题情况组织成一个 Map，有利于进行机器判卷
     *
     * @param userExam
     * @return
     */
    private Map<String, PaperUnitItem> getUserQuestion(final UserExam userExam) {
        List<PaperUnitItem> questions = new ArrayList<>();
        for (PaperUnit paperUnit : userExam.getPaperUnit()) {
            for (PaperUnitItem question : paperUnit.getQuestions()) {
                if (question.getType().equals(QuestionTypeEnum.COMBINATORIAL.getCode())) {
                    // 复选题，需要遍历子题目
                    for (Question child : question.getChildren()) {
                        PaperUnitItem item = new PaperUnitItem();
                        try {
                            BeanUtils.copyProperties(item, child);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        item.setPaperUnitId(paperUnit.getPaperUnitId());
                        questions.add(item);
                    }
                }else {
                    questions.add(question);
                }
            }
        }
        return questions.stream().collect(Collectors.toMap(k -> {
            return k.getPaperUnitId() + "_" + k.getQuId();
        }, v -> v));
    }

    /**
     * 学员答题情况写入快照
     *
     * @param paperUnit
     * @param snapshotItem
     * @param userUnitItemMap
     */
    private void userAnswer2Snapshot(PaperUnit paperUnit, Question snapshotItem,
                                               Map<String, PaperUnitItem> userUnitItemMap) {
        String key = paperUnit.getPaperUnitId() + "_" + snapshotItem.getQuId();
        PaperUnitItem userQu = userUnitItemMap.get(key);
        if (userQu == null) return;

        snapshotItem.setAnsweredFlag(userQu.isAnsweredFlag());
        snapshotItem.setAnswered(userQu.getAnswered());
        // 将学员答案赋予快照
        Map<Long, QuestionAnswer> userAnswerMap = userQu.getAnswers().stream().collect(Collectors
                .toMap(QuestionAnswer::getAnswerId, v -> v));
        for (QuestionAnswer answer : snapshotItem.getAnswers()) {
            QuestionAnswer questionAnswer = userAnswerMap.get(answer.getAnswerId());
            answer.setSelectFlag(questionAnswer.isSelectFlag());
            answer.setAnswered(questionAnswer.getAnswered());
        }
    }

    /**
     * 校验答案
     *
     * @param question
     * @return
     */
    private BigDecimal checkAnswer(Question question) {
        boolean rightFlag = false; // 设置学员答题是否正确
        // 核对学员答案
        if (org.apache.commons.lang3.StringUtils.equalsAny(question.getType(), QuestionTypeEnum.SINGLE_CHOICE.getCode(),
                QuestionTypeEnum.MULTIPLE_CHOICE.getCode(), QuestionTypeEnum.TRUE_FALSE.getCode())) {
            // 单选、复选、多选
            List<QuestionAnswer> answers = question.getAnswers();
            long count = answers.stream().filter(answer -> answer.isSelectFlag() != answer.isRightFlag()).count();
            if(count == 0) rightFlag = true;
        }else if (question.getType().equals(QuestionTypeEnum.FILL_BLANK.getCode())) {
            // 判断填空，可能会有多个空；每个空多个答案
            // 会返回学员回答错误的空，若 count >0 则该题目错误
            if (question.getAnswers() != null) {
                long count = question.getAnswers().stream().filter(answer -> {
                    String answered = answer.getAnswered(); // 学员答案
                    // 遍历参考答案，如果学员答案命中，则返回true,
                    // 如果 count 大于0，则说明有命中答案；但这里的逻辑是保留学员回答正确的空，即 count>0，则代表学员回答正确
                    // 回答正确的空就删掉，只保留回答错误的空
                    return answer.getContents().stream().filter(referAnswer -> referAnswer.equals(answered)).count() == 0;
                }).count();
                if(count == 0) rightFlag = true;
            }else {
                rightFlag = false;
            }

        }

        question.setRightFlag(rightFlag);
        BigDecimal decimal = rightFlag ? (question.getScore() == null ? BigDecimal.ZERO : question.getScore()) : BigDecimal.ZERO;
        return decimal;
    }

    /**
     * 根据考试记录 ID，查询某一考试详情
     *
     * @param recordId
     * @return
     */
    @Override
    public UserExamRecordResp queryExamByRecordId(Long recordId) {
        UserExamRecordResp resp = new UserExamRecordResp();
        Long userId = null;
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId();
        }catch (CustomException e) {

        }
        UserExamRecord userExamRecord = userExamRecordMapper.selectExamRecordById(recordId, userId);
        if (userExamRecord == null) throw new RuntimeException("查询考试记录出现错误，请联系管理员[" + recordId + "," + userId + "]");
        try {
            BeanUtils.copyProperties(resp, userExamRecord);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        UserExamRecordSnapshot snapshot = userExamRecordSnapshotMapper.selectExamRecordSnapshotById(recordId);
        resp.setExam(snapshot.getSnapshot2());
        // TODO: 根据考试信息，判断是否允许查看考试成绩等
        // show_score 成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
        // status 试卷状态（0未交卷 1已交卷 2已交卷但未判卷 3已判卷）
        return resp;
    }

    /**
     * 查询考试列表
     *
     * @param examId
     * @return
     */
    @Override
    public List<UserExamRecord> queryExamRecord(Long examId, String name, String status, List<String> range) {
        if (!(range == null || range.size() == 0 || (range.size() == 2 && range.get(0).compareTo(range.get(1)) < 0))) {
            throw new RuntimeException("时间范围错误，请校验后重新输入！");
        }

        UserExamRecordReqQuery query = new UserExamRecordReqQuery();
        if (examId != null) query.setExamId(examId);
        if (name != null) query.setName(name);
        query.setStatus(status);
        query.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        // 处理时间区间 YYYYMMDD
        if (range != null && range.size() == 2) {
            query.setRangStartTime(DateUtils.dateTime(DateUtils.YYYYMMDDHHMMSS, range.get(0) + "000000"));
            query.setRageEndTime(DateUtils.dateTime(DateUtils.YYYYMMDDHHMMSS, range.get(1) + "235959"));
        }
        return queryExamRecord(query);
    }

    @Override
    @EtpDataScope(alias = "e")
    public List<UserExamRecord> queryExamRecord(UserExamRecordReqQuery query) {
        return userExamRecordMapper.selectUserExamRecord(query);
    }

    /**
     * 提交一个题目的答案，将学员成绩保存到 Redis 中
     *
     * 学员答案在 Redis 中保存
     * EXAM_RECORD_{recordId}_paperUnitId_quId
     */
    @Override
    public void submitAnswer(Long recordId, PaperUnitItem unitItem) {
        // 校验考试是否进行中
        checkExamRecordIng(recordId, false);

        // 保存学员答案
        String key = "EXAM_RECORD_QU_" + recordId;
        redisCache.setCacheMapValue(key, unitItem.getPaperUnitId() +"_" + unitItem.getQuId(), unitItem);
    }

    private void checkExamRecordIng(Long recordId, boolean loadDB) {
        // 校验是否有效的考试记录，若无则从数据库提取
        String recordKey = "EXAM_RECORD_" + recordId;
        UserExamRecordResp userExamRecordResp = (UserExamRecordResp)redisCache.getCacheObject(recordKey);
        UserExamReqQuery query = new UserExamReqQuery();

        // 如果 loadDB == true，则会强制从数据库加载考试记录信息
        if (userExamRecordResp == null || loadDB == true) {
            // 提取考试记录信息，并保存到 Redis
            userExamRecordResp = queryExamByRecordId(recordId);
            // 如果考试已结束，数据就无需往 Redis 中保存了
            if (!"0".equals(userExamRecordResp.getStatus()) && !"0".equals(userExamRecordResp.getHandType())) {
                throw new RuntimeException("本场考试[" + recordId + "]考试已交卷，不允许再进行提交操作！");
            }
            redisCache.setCacheObject(recordKey, userExamRecordResp);
        }

        // 判断是否已交卷，则不允许再提交答案
        if (!"0".equals(userExamRecordResp.getStatus()) && !"0".equals(userExamRecordResp.getHandType())) {
            throw new RuntimeException("本场考试[" + recordId + "]考试已交卷，不允许再进行提交操作！");
        }

        query.setExamId(userExamRecordResp.getExamId());

        //设置数据权限
        query.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, EtpDataScopeUtils.getDataScopeSql("ex"));
        UserSimpleExam simpleExam = userExamMapper.selectUserExamInfo(query);
        // 判断考试状态是否正常，如考试是否已结束，是否已交卷
        Date nowDate = DateUtils.getNowDate();
        if (simpleExam.getEndTime() != null
                && nowDate.compareTo(simpleExam.getEndTime()) > 0) {
            // 触发强制交卷
            forcedSubmit(recordId, "2", DateUtils.getTime() + "提交题目答案，触发 —— 考试已结束，强制交卷！");
            throw new RuntimeException("本场考试[" + recordId + "]考试已于" + simpleExam.getEndTime() + "结束！");
        }

        // 预计交卷时间，如果是 0 则代表考试无限制时间，不需要判断考试强制结束时间
        if (simpleExam.getTotalTime()!= null && simpleExam.getTotalTime() > 0) {
            // 考试开始时间 + TotalTime，为该场考试的强制结束时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, simpleExam.getTotalTime());

            Date endTime = calendar.getTime(); // 强制结束时间
            if (endTime.compareTo(nowDate) < 0) {
                // 触发强制交卷
                forcedSubmit(recordId, "2", DateUtils.getTime() + "提交题目答案，触发 —— 考试已结束，强制交卷！强制结束-" + endTime);
                throw new RuntimeException("本场考试[" + recordId + "]考试已于" + endTime + "结束！");
            }
        }
    }

    /**
     * 触发强制交卷
     * @param recordId
     * @param remark
     */
    private void forcedSubmit(Long recordId, String handType, String remark) {
        // 从数据库获取学员的考试快照
        UserExamRecordSnapshot snapshot = userExamRecordSnapshotMapper.selectExamRecordSnapshotById(recordId);
        UserExam resultExam = snapshot.getSnapshot();

        // 从 Redis 获取学员的考试结果，合并到考试快照
        // 在 makeupUserExam 中，移除 Redis 中的考试记录，和学员答题
        String key = "EXAM_RECORD_QU_" + recordId;
        Map<String, PaperUnitItem> userUnitItemMap = redisCache.getCacheMap(key);
        // UserExam

        // 合并学员答案至快照，并判卷
        resultExam = makeupUserExam0(resultExam, userUnitItemMap);

        // 更新试卷记录
        Long userId = null;
        try {
            userId = SecurityUtils.getLoginUser().getUser().getUserId();
        }catch (CustomException e) {}

        UserExamRecord userExamRecord = userExamRecordMapper.selectExamRecordById(resultExam.getRecordId(), userId);
        userExamRecord.setRecordId(resultExam.getRecordId());
        userExamRecord.setTotalScore(resultExam.getTotalScore()); // 试卷得分
        userExamRecord.setEndTime(DateUtils.getNowDate()); // 交卷时间\
        userExamRecord.setDoTime((userExamRecord.getEndTime().getTime() - userExamRecord.getStartTime().getTime())/1000);
        userExamRecord.setUserScore(resultExam.getUserScore());      // 设置学员得分
        userExamRecord.setQuCorrect(resultExam.getQuCorrect());
        userExamRecord.setHandType(handType);                  // 交卷方式
        userExamRecord.setRemark(remark);
        // 判断是否需要判卷
        // 试卷状态（ 0未交卷 1未判卷 4未发布 8放弃考试 9已完成）
        // 成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
        // userExamRecord.setStatus("1");
        if ("3".equals(handType)) { // 放弃考试
            userExamRecord.setStatus(ExamRecordStatusEnum.GIVEUP.getCode());
        }else if (StringUtils.equalsAny(resultExam.getShowScore(), // 0不显示成绩 9已完成
                ExamShowScoreEnum.NoShowScore.getCode(),  // 不显示成绩
                ExamShowScoreEnum.ImShowScore.getCode())) // 立即显示成绩
            userExamRecord.setStatus(ExamRecordStatusEnum.DONE.getCode()); // 已完成
        else if (StringUtils.equalsAny(resultExam.getShowScore(),
                ExamShowScoreEnum.Judgmented.getCode()))  // 批改后显示成绩
            userExamRecord.setStatus(ExamRecordStatusEnum.NOT_JUDGED.getCode()); // 未判卷
        else if (StringUtils.equalsAny(resultExam.getShowScore(),
                ExamShowScoreEnum.TimingPublished.getCode(),   // 定时发布
                ExamShowScoreEnum.ManualPublished.getCode()))  // 人工发布
            userExamRecord.setStatus(ExamRecordStatusEnum.UNPUBLISHED.getCode()); // 已交卷，但未发布

        userExamRecordMapper.updateExamRecord(userExamRecord);

        // 更新完整快照
        UserExamRecordSnapshot recordSnapshot = new UserExamRecordSnapshot();
        recordSnapshot.setRecordId(resultExam.getRecordId());
        recordSnapshot.setSnapshot2(resultExam);
        userExamRecordSnapshotMapper.updateExamRecordSnapshot(recordSnapshot);

        String recordKey = "EXAM_RECORD_" + recordId;
        redisCache.deleteObject(recordKey);
        redisCache.deleteObject(key);
    }

    /**
     * 获取未考试的答题记录
     *
     * @param recordId
     * @return
     */
    @Override
    public UserExam queryUserExamByRecordId(Long recordId) {
        // 校验考试未结束
        checkExamRecordIng(recordId, true);

        // 获取快照
        UserExamRecordSnapshot snapshot = userExamRecordSnapshotMapper.selectExamRecordSnapshotById(recordId);
        UserExam userExam = snapshot.getSnapshot();

        // 合并 Redis 中存储的学员答案(因为调用开始考试接口，会移除掉学员作答及大题情况等，所以直接调用判卷的函数进行合并)
        String key = "EXAM_RECORD_QU_" + recordId;
        Map<String, PaperUnitItem> userUnitItemMap = redisCache.getCacheMap(key);
        userExam = makeupUserExam0(userExam, userUnitItemMap);

        return userExam;
    }

    /**
     * 放弃考试，放弃考试 - 加载 Redis 中的答题，并保存学员答题快照
     * @param recordId
     */
    @Override
    public void endExam(Long recordId, String handType, String remark) {
        // 校验考试未结束
        checkExamRecordIng(recordId, true);
        forcedSubmit(recordId, handType, remark);
    }

    @Override
    public int getCountNum(String type) {
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        Long[] postIds = SecurityUtils.getLoginUser().getUser().getPostIds();
        //可能用在两处，字段也不重名，不加别名
        return userExamMapper.selectCountNum(type,userId,deptId,postIds,EtpDataScopeUtils.getDataScopeSql(""));
    }
}
