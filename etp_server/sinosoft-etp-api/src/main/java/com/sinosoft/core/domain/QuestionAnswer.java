package com.sinosoft.core.domain;

import com.sinosoft.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class QuestionAnswer {
    /** 参考答案ID */
    private Long answerId;

    /** 题目ID */
    private Long quId;

    /** 答案内容 */
    private String content;

    /**
     * 如果是填空题的话，一个空可能会存在多个答案，存储在该字段中
     */
    private List<String> contents;

    /** 单选/复选/判断均通过该字段标注是否正确（1是 0否） */
    private boolean rightFlag;

    /** 学员是否选中 */
    private boolean selectFlag;

    /** 学员填空题与问答题的答案 */
    private String answered;

    /** 答案解析 */
    private String analysis;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    public void setAnswerId(Long answerId)
    {
        this.answerId = answerId;
    }

    public Long getAnswerId()
    {
        return answerId;
    }
    public void setQuId(Long quId)
    {
        this.quId = quId;
    }

    public Long getQuId()
    {
        return quId;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content != null?content:(contents!=null?contents.toString():null);
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public void setRightFlag(boolean rightFlag)
    {
        this.rightFlag = rightFlag;
    }

    public boolean getRightFlag()
    {
        return rightFlag;
    }
    public void setAnalysis(String analysis)
    {
        this.analysis = analysis;
    }

    public String getAnalysis()
    {
        return analysis;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public boolean isRightFlag() {
        return rightFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("answerId", getAnswerId())
                .append("quId", getQuId())
                .append("content", getContent())
                .append("rightFlag", getRightFlag())
                .append("analysis", getAnalysis())
                .toString();
    }
}