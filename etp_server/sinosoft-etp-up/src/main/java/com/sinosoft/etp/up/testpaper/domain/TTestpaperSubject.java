package com.sinosoft.etp.up.testpaper.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 试卷关联题目对象 t_testpaper_subject
 *
 *
 * @date 2021-12-17
 */
public class TTestpaperSubject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 题库id */
    @Excel(name = "题库id")
    private Long tkid;

    /** 试卷id */
    @Excel(name = "试卷id")
    private Long sjid;

    /** 顺序 */
    @Excel(name = "顺序")
    private Long sort;

    /** 题目类型 */
    @Excel(name = "题目类型")
    private String type;

    /** 分值 */
    @Excel(name = "分值")
    private String score;

    /** 删除 */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTkid(Long tkid)
    {
        this.tkid = tkid;
    }

    public Long getTkid()
    {
        return tkid;
    }
    public void setSjid(Long sjid)
    {
        this.sjid = sjid;
    }

    public Long getSjid()
    {
        return sjid;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setScore(String score)
    {
        this.score = score;
    }

    public String getScore()
    {
        return score;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tkid", getTkid())
            .append("sjid", getSjid())
            .append("sort", getSort())
            .append("type", getType())
            .append("score", getScore())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
