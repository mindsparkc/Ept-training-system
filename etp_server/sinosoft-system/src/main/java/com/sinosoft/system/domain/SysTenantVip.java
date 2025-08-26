package com.sinosoft.system.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户vip资源控制对象 sys_tenant_vip
 *
 * @author sinosoft
 * @date 2025-05-29
 */
public class SysTenantVip extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** vip代码 0/1/2/9 */
    @Excel(name = "vip代码 0/1/2/9")
    private String vipCode;

    /** vip名称 免费版/初级vip/中级vip/超级vip */
    @Excel(name = "vip名称 免费版/初级vip/中级vip/超级vip")
    private String vipName;

    /** 允许最大部门数量 */
    @Excel(name = "允许最大部门数量")
    private Long maxDept;

    /** 允许最大人员数量 */
    @Excel(name = "允许最大人员数量")
    private Long maxUser;

    /** 允许最大课件数量 */
    @Excel(name = "允许最大课件数量")
    private Long maxCourseware;

    /** 允许最大课程数量 */
    @Excel(name = "允许最大课程数量")
    private Long maxCurriculum;

    /** 允许最大考试数量 */
    @Excel(name = "允许最大考试数量")
    private Long maxExam;

    /** 允许最大文件数量 */
    @Excel(name = "允许最大文件数量")
    private Long maxFile;

    /** 允许最大文件大小(G) */
    @Excel(name = "允许最大文件大小(G)")
    private Long maxFileSize;

    /** 是否删除(0：未删除   1：删除) */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setVipCode(String vipCode)
    {
        this.vipCode = vipCode;
    }

    public String getVipCode()
    {
        return vipCode;
    }
    public void setVipName(String vipName)
    {
        this.vipName = vipName;
    }

    public String getVipName()
    {
        return vipName;
    }
    public void setMaxDept(Long maxDept)
    {
        this.maxDept = maxDept;
    }

    public Long getMaxDept()
    {
        return maxDept;
    }
    public void setMaxUser(Long maxUser)
    {
        this.maxUser = maxUser;
    }

    public Long getMaxUser()
    {
        return maxUser;
    }
    public void setMaxCourseware(Long maxCourseware)
    {
        this.maxCourseware = maxCourseware;
    }

    public Long getMaxCourseware()
    {
        return maxCourseware;
    }
    public void setMaxCurriculum(Long maxCurriculum)
    {
        this.maxCurriculum = maxCurriculum;
    }

    public Long getMaxCurriculum()
    {
        return maxCurriculum;
    }
    public void setMaxExam(Long maxExam)
    {
        this.maxExam = maxExam;
    }

    public Long getMaxExam()
    {
        return maxExam;
    }
    public void setMaxFile(Long maxFile)
    {
        this.maxFile = maxFile;
    }

    public Long getMaxFile()
    {
        return maxFile;
    }
    public void setMaxFileSize(Long maxFileSize)
    {
        this.maxFileSize = maxFileSize;
    }

    public Long getMaxFileSize()
    {
        return maxFileSize;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vipCode", getVipCode())
            .append("vipName", getVipName())
            .append("maxDept", getMaxDept())
            .append("maxUser", getMaxUser())
            .append("maxCourseware", getMaxCourseware())
            .append("maxCurriculum", getMaxCurriculum())
            .append("maxExam", getMaxExam())
            .append("maxFile", getMaxFile())
            .append("maxFileSize", getMaxFileSize())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
