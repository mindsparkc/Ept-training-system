package com.sinosoft.etp.cp.questionnaire.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 问卷调查对象 t_questionnaire
 *
 * 
 * @date 2022-02-14
 */
public class TQuestionnaireWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 问卷名称 */
    @Excel(name = "问卷名称")
    private String wjmc;

    /** 启用状态 0 正常 1 禁用 */
    @Excel(name = "启用状态 0 正常 1 禁用")
    private String qyzt;

    /** 封面图 */
    @Excel(name = "封面图")
    @PreProcessField
    private String fm;

    /** 问卷描述 */
    @Excel(name = "问卷描述")
    private String ms;

    /** 发布状态 0 暂存 99 已发布 */
    @Excel(name = "发布状态 0 暂存 99 已发布")
    private String fb;

    /** 问卷内容 */
    @Excel(name = "问卷内容")
    private String nr;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 开放程度0公开，1指定用户，2不公开 */
    @Excel(name = "开放程度0公开，1指定用户，2不公开")
    private String kfcd;

    /** 是否匿名0否，1公开 */
    @Excel(name = "是否匿名0否，1公开")
    private String sfnm;

    /** 删除标志 */
    private String delFlag;

    private String tags;
    /** 用户名称 */
    private String nickName;
    private String xyid;

    private List<TQuestionnaireStudentWithCp> sysUsers;

    public List<TQuestionnaireStudentWithCp> getSysUsers() {
        return sysUsers;
    }

    public TQuestionnaireWithCp setSysUsers(List<TQuestionnaireStudentWithCp> sysUsers) {
        this.sysUsers = sysUsers;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public TQuestionnaireWithCp setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setWjmc(String wjmc)
    {
        this.wjmc = wjmc;
    }

    public String getWjmc()
    {
        return wjmc;
    }
    public void setQyzt(String qyzt)
    {
        this.qyzt = qyzt;
    }

    public String getQyzt()
    {
        return qyzt;
    }
    public void setFm(String fm)
    {
        this.fm = fm;
    }

    public String getFm()
    {
        return fm;
    }
    public void setMs(String ms)
    {
        this.ms = ms;
    }

    public String getMs()
    {
        return ms;
    }
    public void setFb(String fb)
    {
        this.fb = fb;
    }

    public String getFb()
    {
        return fb;
    }
    public void setNr(String nr)
    {
        this.nr = nr;
    }

    public String getNr()
    {
        return nr;
    }
    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setKfcd(String kfcd)
    {
        this.kfcd = kfcd;
    }

    public String getKfcd()
    {
        return kfcd;
    }
    public void setSfnm(String sfnm)
    {
        this.sfnm = sfnm;
    }

    public String getSfnm()
    {
        return sfnm;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getXyid() {
        return xyid;
    }

    public void setXyid(String xyid) {
        this.xyid = xyid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("wjmc", getWjmc())
                .append("qyzt", getQyzt())
                .append("fm", getFm())
                .append("ms", getMs())
                .append("fb", getFb())
                .append("nr", getNr())
                .append("beginTime", getBeginTime())
                .append("endTime", getEndTime())
                .append("kfcd", getKfcd())
                .append("sfnm", getSfnm())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
