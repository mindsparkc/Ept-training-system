package com.sinosoft.core.domain;

import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考试开放规则对象 exam_open_rule
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public class ExamOpenRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考试编号 */
    private Long examId;

    /** 规则类型（1限定部门 2限定群组 3限定用户 4限定岗位） */
    private String type;

    /** 限定ID（部门ID 群组ID 岗位ID 用户ID） */
    private Long limitId;

    public void setExamId(Long examId)
    {
        this.examId = examId;
    }

    public Long getExamId()
    {
        return examId;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setLimitId(Long limitId)
    {
        this.limitId = limitId;
    }

    public Long getLimitId()
    {
        return limitId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("examId", getExamId())
            .append("type", getType())
            .append("limitId", getLimitId())
            .toString();
    }
}
