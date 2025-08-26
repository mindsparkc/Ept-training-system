package com.sinosoft.core.domain;

import cn.jkingtools.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 题目信息对象 question
 *
 * @author wanglijie
 * @date 2022-08-09
 */
public class Question extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 题目ID */
    private Long quId;

    /** 父题目ID */
    @Excel(name = "父题目ID")
    private Long parentId;

    /** 版本号（Linux时间戳） */
    @Excel(name = "版本号", readConverterExp = "L=inux时间戳")
    private Long version;

    /** 题干内容 */
    @PreProcessField
    private String content;

    /** 题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部 */
    @Excel(name = "题干内容是可能会有富文本标签的，而该字段移除的富文本标签。并且存储该字段会对content进行截取，而不是存储全部")
    private String display;

    /** 题目类型：1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题。取字典 question_type */
    @Excel(name = "题目类型：1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题。取字典 question_type")
    private String type;

    /** 难度等级，取字典 question_level */
    @Excel(name = "难度等级，取字典 question_level")
    private String level;

    /** 学员填空题与问答题的答案 */
    private String answered;

    /** 题目解析 */
    @Excel(name = "题目解析")
    @PreProcessField
    private String analysis;

    /** 判卷评语 */
    private String review;

    /** 是否练习（1是 0否） */
    @Excel(name = "是否练习", readConverterExp = "1=是,0=否")
    private boolean tarinFlag;

    /** 是否考试（1是 0否） */
    @Excel(name = "是否考试", readConverterExp = "1=是,0=否")
    private boolean examFlag;

    /** 有效性（0有效 1手动废弃） */
    @Excel(name = "有效性", readConverterExp = "0=有效,1=手动废弃")
    private String status;

    /** 有效期截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 排序，当设置题目随机策略时，该值随机生成，并用于排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 数据归属用户 */
    private Long userId;

    /** 数据归属部门 */
    private Long deptId;

    /** 删除标记（0有效 2删除） */
    private String delFlag;

    /**
     * 候选答案，以 json 的形式保存在数据库中
     */
    List<QuestionAnswer> answers;

    /**
     * 子题目，仅在题目类型是组合题时生效，以 json 形式保存在数据库中
     */
    List<Question> children;

    /** 学员是否答对 */
    private boolean rightFlag;

    /** 学员是否答题 */
    private boolean answeredFlag;

    /** 题目分数 */
    private BigDecimal score;
    /** 学员得分 */
    private BigDecimal useScore;

    /** 所属试卷编号 */
    private Long paperId;
    /** 试卷单元编号 */
    private Long paperUnitId;

    /** 题目的 hash 值，用于排重 */
    private String hashKey;

    public Long getPaperUnitId() {
        return paperUnitId;
    }

    public void setPaperUnitId(Long paperUnitId) {
        this.paperUnitId = paperUnitId;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public void setQuId(Long quId)
    {
        this.quId = quId;
    }

    public Long getQuId()
    {
        return quId;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setVersion(Long version)
    {
        this.version = version;
    }

    public Long getVersion()
    {
        return version;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setDisplay(String display)
    {
        this.display = display;
    }

    public String getDisplay()
    {
        return display;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getLevel()
    {
        return level;
    }
    public void setAnalysis(String analysis)
    {
        this.analysis = analysis;
    }

    public String getAnalysis()
    {
        return analysis;
    }
    public void setTarinFlag(boolean tarinFlag)
    {
        this.tarinFlag = tarinFlag;
    }

    public boolean getTarinFlag()
    {
        return tarinFlag;
    }
    public void setExamFlag(boolean examFlag)
    {
        this.examFlag = examFlag;
    }

    public boolean getExamFlag()
    {
        return examFlag;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return StringUtils.isBlank(delFlag)?"0":delFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswer> answers) {
        this.answers = answers;
    }

    public List<Question> getChildren() {
        return children;
    }

    public void setChildren(List<Question> children) {
        this.children = children;
    }

    public boolean isTarinFlag() {
        return tarinFlag;
    }

    public boolean isExamFlag() {
        return examFlag;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getUseScore() {
        return useScore;
    }

    public void setUseScore(BigDecimal useScore) {
        this.useScore = useScore;
    }

    public boolean isRightFlag() {
        return rightFlag;
    }

    public void setRightFlag(boolean rightFlag) {
        this.rightFlag = rightFlag;
    }

    public boolean isAnsweredFlag() {
        return answeredFlag;
    }

    public void setAnsweredFlag(boolean answeredFlag) {
        this.answeredFlag = answeredFlag;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getHashKey() {
        if (this.hashKey == null && type != null) {
            if (!type.equals("6"))
                this.setHashKey(getHashKey(this));
            else {
                List<String> strings = this.getChildren().stream().map(question -> getHashKey(question)).collect(Collectors.toList());
                this.setHashKey(DigestUtils.md5Hex(String.join("_", strings)));
            }
        }
        return this.hashKey;
    }

    public String getHashKey(Question question) {
        if (question.hashKey == null && question.type!=null) {
            // 题干列表、选项列表，对选项进行排序 1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题
            List<String> list = new ArrayList<>();
            // 单选、多选、判断题
            if (StringUtils.equalsAny(question.type, "1", "2", "3"))
                if (question.getAnswers() != null && question.getAnswers().size() > 0) {
                    System.out.println(question.getAnswers());
                    for (Object obj : question.getAnswers()) {
                        if (!(obj instanceof QuestionAnswer)) continue;
                        list.add(((QuestionAnswer) obj).getContent() + ((QuestionAnswer) obj).getRightFlag());
                    }
                }
                else if (question.type.equals("4")) { // 填空题会有多个空
                    if (question.getAnswers() != null && question.getAnswers().size() > 0)
                        for (Object obj : question.getAnswers()) {
                            if (!(obj instanceof QuestionAnswer)) continue;
                            List<String> collect = ((QuestionAnswer) obj).getContents().stream().sorted(Comparator.comparing(String::hashCode))
                                    .map(Objects::toString).collect(Collectors.toList());
                            list.add(String.join("_", collect) + ((QuestionAnswer) obj).getRightFlag());
                        }
                }
            list.add(question.getContent());

            question.setHashKey(DigestUtils.md5Hex(String.join("_", list))); // 设置 hashKey
        }
//        return question.getHashKey();
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("quId", getQuId())
                .append("parentId", getParentId())
                .append("version", getVersion())
                .append("content", getContent())
                .append("display", getDisplay())
                .append("type", getType())
                .append("level", getLevel())
                .append("analysis", getAnalysis())
                .append("tarinFlag", getTarinFlag())
                .append("examFlag", getExamFlag())
                .append("status", getStatus())
                .append("expireTime", getExpireTime())
                .append("userId", getUserId())
                .append("deptId", getDeptId())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
