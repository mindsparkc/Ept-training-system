package com.sinosoft.etp.cp.curriculum.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * curriculum对象 t_curriculum_content
 *
 * 
 * @date 2021-11-25
 */
public class TCurriculumContentWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long cid;

    /** 课件或者试题ID */
    @Excel(name = "课件或者试题ID")
    private Long sid;

    /** 课件或者练习kj、st */
    @Excel(name = "课件或者练习kj、st")
    private String source_type;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 课件或者练习的名称*/
    private String source_name;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCid(Long cid)
    {
        this.cid = cid;
    }

    public Long getCid()
    {
        return cid;
    }
    public void setSid(Long sid)
    {
        this.sid = sid;
    }

    public Long getSid()
    {
        return sid;
    }

    public String getSource_type() {
        return source_type;
    }

    public TCurriculumContentWithCp setSource_type(String source_type) {
        this.source_type = source_type;
        return this;
    }

    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }

    public String getSource_name() {
        return source_name;
    }

    public TCurriculumContentWithCp setSource_name(String source_name) {
        this.source_name = source_name;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("cid", getCid())
                .append("sid", getSid())
                .append("sourceType", getSource_type())
                .append("sort", getSort())
                .append("source_name", getSource_name())
                .toString();
    }
}
