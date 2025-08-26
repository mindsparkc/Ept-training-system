package com.sinosoft.admin.exam.service.impl;

import cn.jkingtools.utils.BeanUtils;
import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.admin.exam.domain.ExamQuery;
import com.sinosoft.admin.exam.domain.ExamReqDTO;
import com.sinosoft.admin.exam.domain.ExamRespDTO;
import com.sinosoft.admin.exam.mapper.ExamMapper;
import com.sinosoft.admin.exam.mapper.ExamOpenRuleMapper;
import com.sinosoft.admin.exam.service.IExamService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.annotation.VipCheck;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.*;
import com.sinosoft.core.enums.ExamRecordStatusEnum;
import com.sinosoft.core.enums.ExamShowScoreEnum;
import com.sinosoft.core.enums.OpenTypeEnum;
import com.sinosoft.core.enums.QuestionTypeEnum;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.user.exam.domain.UserExam;
import com.sinosoft.user.exam.domain.UserExamRecord;
import com.sinosoft.user.exam.domain.UserExamRecordReqQuery;
import com.sinosoft.user.exam.domain.UserExamRecordSnapshot;
import com.sinosoft.user.exam.mapper.UserExamRecordMapper;
import com.sinosoft.user.exam.mapper.UserExamRecordSnapshotMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-04
 */
@Service
public class ExamServiceImpl implements IExamService {
    @Resource
    private ExamMapper examMapper;

    @Resource
    private IObjectInterCategoryService objectInterCategoryService;

    @Resource
    private ICategoryService categoryService;

    @Resource
    private ExamOpenRuleMapper examOpenRuleMapper;

    @Resource
    UserExamRecordSnapshotMapper userExamRecordSnapshotMapper;

    @Resource
    UserExamRecordMapper userExamRecordMapper;
    private static final CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.EXAM_CATEGORY;

    /**
     * 查询考试信息
     *
     * @param examId 考试信息ID
     * @return 考试信息
     */
    @Override
    public ExamRespDTO selectExamById(Long examId) {
        Exam exam = examMapper.selectExamById(examId);
        if(exam==null){
            throw new RuntimeException("获取考试详情失败！");
        }
        ExamRespDTO examRespDTO = BeanUtils.map(exam,ExamRespDTO.class);
        // 补充分类信息
        List<ObjectInterCategory> interCategories = objectInterCategoryService.selectObjectInterCategory(categoryEnum, Arrays.asList(examId));
        List<List<Long>> categories = new ArrayList<>();
        for (ObjectInterCategory interCategory : interCategories) {
            String[] split = interCategory.getAncestors().split(",");
            categories.add(Arrays.stream(split).filter(s->{
                if (s.equals("0")) {
                    return false;
                }
                return true;
            }).map(s -> Long.valueOf(s)).collect(Collectors.toList()));
        }
        examRespDTO.setCategoryIds(categories);

        // 补充开放规则
        List<ExamOpenRule> examOpenRule = examOpenRuleMapper.selectExamOpenRuleById(examId);
        examRespDTO.setOpenRules(examOpenRule.stream().map(r -> r.getLimitId()).collect(Collectors.toList()));

        return examRespDTO;
    }

    /**
     * 查询考试信息列表
     *
     * @param examQuery 考试信息
     * @return 考试信息
     */
    @EtpDataScope(alias = "ex")
    @Override
    public List<Exam> selectExamList(ExamQuery examQuery) {
        List<Exam> examList = examMapper.selectExamList(examQuery);

        for (Exam exam : examList) {
            boolean pwdFlag = StringUtils.isNotBlank(exam.getPassword());
            exam.setPwdFlag(pwdFlag);
            exam.setPassword(null);
        }
        return examList;
    }

    /**
     * 查询考试信息列表
     *
     * @param examIds 需要查询的考试信息ID
     * @return 考试信息集合
     */
    public List<Exam> selectExamByIds(Long[] examIds) {
        return examMapper.selectExamByIds(examIds);
    }

    /**
     * 新增考试信息
     *
     * @param examDto 考试信息
     * @return 结果
     */
    @VipCheck(resource="exam")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertExam(ExamReqDTO examDto) {
        examDto.setExamId(IdUtil.nextId());

        // 试卷归属分类
        if (examDto.hasCategoryIds()) {
            List<ObjectInterCategory> categories = getExamInterCategory(examDto);
            // 批量插入试卷归属分类
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories);
        }

        // 试卷开放规则 (开放策略（1公开 2限定部门 3限定岗位 4限定成员 5口令密码）)
        // 如果不指定开放规则，即 openType == null，则不更新试卷开放规则
        String openType = examDto.getOpenType();
        if (org.apache.commons.lang3.StringUtils.equalsAny(openType,
                OpenTypeEnum.DEPT.getCode(), OpenTypeEnum.POST.getCode(), OpenTypeEnum.USER.getCode())) {
            List<ExamOpenRule> examOpenRules = getExamOpenRules(examDto);
            // 将限定规则写入到开放规则表
            if(examOpenRules != null && examOpenRules.size()>0)
                examOpenRuleMapper.batchInsertExamOpenRule(examOpenRules);
        }else if (openType.equals(OpenTypeEnum.PASSWD.getCode())) {
            // 如果是口令密码，对口令进行加密
            String encryptPassword = SecurityUtils.encryptPassword(examDto.getPassword());
            examDto.setPassword(encryptPassword);
        }

        // 若未设置发布时间，则立即发布；若未设置开始时间，则立即开始
        if (examDto.getPublishTime() == null) {
            examDto.setPublishTime(DateUtils.getNowDate());
        }

//        if (examDto.getStartTime() == null) {
//            examDto.setStartTime(examDto.getPublishTime());
//        }

        Exam exam = BeanUtils.map(examDto, Exam.class);
        exam.setCreateBy(SecurityUtils.getUsername());
        exam.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        exam.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        EtpDataScopeUtils.setDataScope(exam);// 20250520 lyd etp数据权限相关字段
        int i = examMapper.insertExam(exam);
        if (i <=0 ) throw new RuntimeException("保存试卷失败");

        return exam.getExamId();
    }

    /**
     * 获取试卷的关联分类
     *
     * @param examDto
     * @return
     */
    private List<ObjectInterCategory> getExamInterCategory(ExamReqDTO examDto) {
        return examDto.getCategoryIds().stream().map(ancestors -> {
            ObjectInterCategory interCategory = new ObjectInterCategory();
            Long catId = ancestors.get(ancestors.size()-1);
            interCategory.setCatId(catId);
            interCategory.setObjId(examDto.getExamId());
            // 设置祖级列表
            Category category = categoryService.selectById(categoryEnum, catId);
            interCategory.setAncestors(category.getAncestors()==null||category.getAncestors().equals("0")||category.getAncestors().length()==0?
                    String.valueOf(catId):category.getAncestors()+","+catId);
            return interCategory;
        }).collect(Collectors.toList());
    }

    /**
     * 修改考试信息
     *
     * @param examDto 考试信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateExam(ExamReqDTO examDto) {
        // 先删除原分类关联关系
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(examDto.getExamId()));
        if (examDto.hasCategoryIds()) {
            List<ObjectInterCategory> categories = getExamInterCategory(examDto);
            // 批量插入试卷归属分类
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories);
        }

        // 试卷开放规则 (开放策略（1公开 2部门 3群组 4限定成员 5口令密码）)
        String openType = examDto.getOpenType();
        if (!(openType.equals("1") || openType.equals("5"))) {
            List<ExamOpenRule> examOpenRules = getExamOpenRules(examDto);
            // 删除原规则，并将新的限定规则写入到开放规则表
            examOpenRuleMapper.deleteExamOpenRuleByExamId(examDto.getExamId());
            if (examOpenRules != null && examOpenRules.size() >0)
                examOpenRuleMapper.batchInsertExamOpenRule(examOpenRules);
        }else if (openType.equals("5")) {
            // 如果是口令密码，对口令进行加密
            if (StringUtils.isEmpty(examDto.getPassword())) throw new RuntimeException("必须设置考试口令密码！");
            String encryptPassword = SecurityUtils.encryptPassword(examDto.getPassword());
            examDto.setPassword(encryptPassword);
        }

        Exam exam = BeanUtils.map(examDto, Exam.class);
        exam.setUpdateTime(DateUtils.getNowDate());
        exam.setUpdateBy(SecurityUtils.getUsername());
        return examMapper.updateExam(exam);
    }

    // 主表的openType和关联表的type值一致
    private List<ExamOpenRule> getExamOpenRules(ExamReqDTO examDto) {
        return examDto.getOpenRules().stream().map(id -> {
            ExamOpenRule openRule = new ExamOpenRule();
            openRule.setExamId(examDto.getExamId());
            openRule.setType(examDto.getOpenType());
            openRule.setLimitId(id);
            return openRule;
        }).collect(Collectors.toList());
    }

    /**
     * 仅修改考试状态
     *
     * @param examDto
     * @return
     */
    @Override
    public int updateExamStatus(ExamReqDTO examDto) {
        Exam exam = new Exam();
        exam.setExamId(examDto.getExamId());
        exam.setStatus(examDto.getStatus());
        exam.setUpdateTime(DateUtils.getNowDate());
        exam.setUpdateBy(SecurityUtils.getUsername());
        return examMapper.updateExamStatus(exam);
    }

    /**
     * 批量删除考试信息
     *
     * @param examIds 需要删除的考试信息ID
     * @return 结果
     */
    @Override
    public int deleteExamByIds(Long[] examIds) {
//        return examMapper.deleteExamByIds(examIds);
        // 判断，若已有学员参加考试，则不允许删除考试
        int count = userExamRecordMapper.selectUserExamRecordCountByExamIds(examIds);
        if (count > 0) {
            throw new RuntimeException("已有学员参加本场考试，不允许删除。可选择“取消发布”");
        }
        return examMapper.logicalDeleteExamByIds(examIds);
    }

    /**
     * 删除考试信息信息
     *
     * @param examId 考试信息ID
     * @return 结果
     */
    @Override
    public int deleteExamById(Long examId) {
//        return examMapper.deleteExamById(examId);
        // 判断，若已有学员参加考试，则不允许删除考试
        int count = userExamRecordMapper.selectUserExamRecordCountByExamIds((Long[]) Arrays.asList(examId).toArray());
        if (count > 0) {
            throw new RuntimeException("已有学员参加本场考试，不允许删除。可选择“取消发布”");
        }
        return examMapper.logicalDeleteExamById(examId);
    }

    /**
     * 校验考试名称是否存在
     * @param name 考试名称
     * @return
     */
    @Override
    public int checkName(String name,Long examId) {
        return examMapper.checkName(name,examId);
    }


    /**
     * 提交评卷
     * @param userExam
     */
    @Override
    public void submitReview(UserExam userExam) {
        UserExamRecordSnapshot snapshot = userExamRecordSnapshotMapper.selectExamRecordSnapshotById(userExam.getRecordId());
        UserExam resultExam = snapshot.getSnapshot();
        resultExam.setReviewTime(DateUtils.getNowDate());
        resultExam.setReviewUserId(SecurityUtils.getLoginUser().getUser().getUserId());

        // 获取学员所有题目答案，并转为 Map<unitId_quId, value>
        Map<String, PaperUnitItem> userUnitItemMap = getUserQuestion(userExam);

        // 合并评语、评分
        BigDecimal examTotalScore = BigDecimal.ZERO;      // 试卷总分
        for (PaperUnit paperUnit : resultExam.getPaperUnit()) {
            BigDecimal unitTotalScore = BigDecimal.ZERO;  // 单元总分
            for (PaperUnitItem question : paperUnit.getQuestions()) {
                BigDecimal userScore = BigDecimal.ZERO; // 单题目得分
                // 如果是组合题，处理子题目
                if (question.getType().equals(QuestionTypeEnum.COMBINATORIAL.getCode())) {
                    for (Question child : question.getChildren()) {
                        String key = paperUnit.getPaperUnitId() + "_" + child.getQuId();
                        PaperUnitItem reviewQu = userUnitItemMap.get(key);
                        // 将评卷得分赋予题目
                        child.setUseScore(reviewQu.getUseScore());
                        child.setReview(reviewQu.getReview());
                        child.setRightFlag(reviewQu.isRightFlag());
                        userScore=userScore.add(child.getUseScore()==null?BigDecimal.ZERO:child.getUseScore());
                    }
                }else {
                    String key = paperUnit.getPaperUnitId() + "_" + question.getQuId();
                    PaperUnitItem reviewQu = userUnitItemMap.get(key);
                    // 将评卷得分赋予题目
                    question.setUseScore(reviewQu.getUseScore());
                    question.setReview(reviewQu.getReview());
                    question.setRightFlag(reviewQu.isRightFlag());
                    userScore=userScore.add(question.getUseScore()==null?BigDecimal.ZERO:question.getUseScore());
                }
                question.setUseScore(userScore);                    // 设置题目得分
                unitTotalScore=unitTotalScore.add(userScore==null?BigDecimal.ZERO:userScore);       // 题目分值累计，得单元分值
            }
            paperUnit.setUserScore(unitTotalScore);                 // 设置单元得分
            examTotalScore=examTotalScore.add(unitTotalScore==null?BigDecimal.ZERO:unitTotalScore);      // 单元分值累计，的试卷分值
        }
        resultExam.setUserScore(examTotalScore);                    // 设置试卷总分
        // 更新评卷快照
        snapshot.setSnapshot3(resultExam);
        userExamRecordSnapshotMapper.updateExamRecordSnapshot(snapshot);

        // 更新考试记录
        UserExamRecord examRecord = new UserExamRecord();
        examRecord.setRecordId(resultExam.getRecordId());
        examRecord.setUserScore(examTotalScore);
        if (StringUtils.equalsAny(resultExam.getShowScore(),
                ExamShowScoreEnum.Judgmented.getCode()))  // 批改后显示成绩
            examRecord.setStatus(ExamRecordStatusEnum.DONE.getCode()); // 未判卷
        userExamRecordMapper.updateExamRecord(examRecord);
    }

    /**
     * 将评卷情况组织成一个 Map，有利于进行数据合并
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
                            org.apache.commons.beanutils.BeanUtils.copyProperties(item, child);
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
}
