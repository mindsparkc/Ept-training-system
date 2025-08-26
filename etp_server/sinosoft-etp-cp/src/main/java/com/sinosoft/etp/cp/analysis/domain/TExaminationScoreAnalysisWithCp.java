package com.sinosoft.etp.cp.analysis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.etp.cp.examination.domain.TExaminationStudentWithCp;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 考试对象 t_examination
 *
 * 
 * @date 2021-12-24
 */
public class TExaminationScoreAnalysisWithCp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 考试名称 */
    @Excel(name = "考试名称")
    private String mc;

    /** 考试描述 */
    @Excel(name = "考试描述")
    private String ms;

    /** 封面 */
    @Excel(name = "封面")
    private String fm;

    /** 发布状态：0-暂存，1-发布 */
    @Excel(name = "发布状态：0-暂存，1-发布")
    private String fb;

    /** 考试链接 */
    @Excel(name = "考试链接")
    private String kslj;

    /** 开放程度：完全公开-0
，限定用户-1 */
    @Excel(name = "开放程度：完全公开-0，限定用户-1")
    private String kfcd;

    /** 试卷id */
    @Excel(name = "试卷id")
    private Long sjid;

    /** 考试开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date kskssj;

    /** 考试结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ksjssj;

    /** 答卷时长 */
    @Excel(name = "答卷时长")
    private Long djsc;

    /** 答题次数 */
    @Excel(name = "答题次数")
    private String dtcs;

    /** 分数等级：{key:"不及格",value:[{key:"min",value:0},{key:"max",value:60}]},
{key:"及格",value:[{key:"min",value:60},{key:"max",value:80}]},
{key:"良好",value:[{key:"min",value:80},{key:"max",value:90}]},
{key:"优秀",value:[{key:"min",value:90},{key:"max",value:100}]},
] */
    private String fsdj;

    /** 总分数 */
    @Excel(name = "总分数")
    private Long zfs;

    /** 删除 */
    private String delFlag;
    private String tags;

    /** 应考人数 */
    private Integer peopleNum;

    /** 最高分 */
    private Integer topScore;

    /** 最低分 */
    private Integer lastScore;

    /** 平均分 */
    private Double averageScore;

    /** 考试状态 */
    private String examStatus;

    /** @type update   2021-12-31  限定用户*/
    private List<TExaminationStudentWithCp> sysUsers;

    public List<TExaminationStudentWithCp> getSysUsers() {
        return sysUsers;
    }

    public TExaminationScoreAnalysisWithCp setSysUsers(List<TExaminationStudentWithCp> sysUsers) {
        this.sysUsers = sysUsers;
        return this;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getTopScore() {
        return topScore;
    }

    public void setTopScore(Integer topScore) {
        this.topScore = topScore;
    }

    public Integer getLastScore() {
        return lastScore;
    }

    public void setLastScore(Integer lastScore) {
        this.lastScore = lastScore;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getMc()
    {
        return mc;
    }

    public void setMc(String mc)
    {
        this.mc = mc;
    }

    public String getMs()
    {
        return ms;
    }

    public void setMs(String ms)
    {
        this.ms = ms;
    }

    public String getFm()
    {
        return fm;
    }

    public void setFm(String fm)
    {
        this.fm = fm;
    }

    public String getFb()
    {
        return fb;
    }

    public void setFb(String fb)
    {
        this.fb = fb;
    }

    public String getKslj()
    {
        return kslj;
    }

    public void setKslj(String kslj)
    {
        this.kslj = kslj;
    }

    public String getKfcd()
    {
        return kfcd;
    }

    public void setKfcd(String kfcd)
    {
        this.kfcd = kfcd;
    }

    public Long getSjid()
    {
        return sjid;
    }

    public void setSjid(Long sjid)
    {
        this.sjid = sjid;
    }

    public Date getKskssj()
    {
        return kskssj;
    }

    public void setKskssj(Date kskssj)
    {
        this.kskssj = kskssj;
    }

    public Date getKsjssj()
    {
        return ksjssj;
    }

    public void setKsjssj(Date ksjssj)
    {
        this.ksjssj = ksjssj;
    }

    public Long getDjsc()
    {
        return djsc;
    }

    public void setDjsc(Long djsc)
    {
        this.djsc = djsc;
    }

    public String getDtcs()
    {
        return dtcs;
    }

    public void setDtcs(String dtcs)
    {
        this.dtcs = dtcs;
    }

    public String getFsdj()
    {
        return fsdj;
    }

    public void setFsdj(String fsdj)
    {
        this.fsdj = fsdj;
    }

    public Long getZfs()
    {
        return zfs;
    }

    public void setZfs(Long zfs)
    {
        this.zfs = zfs;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mc", getMc())
            .append("ms", getMs())
            .append("fm", getFm())
            .append("fb", getFb())
            .append("kslj", getKslj())
            .append("kfcd", getKfcd())
            .append("sjid", getSjid())
            .append("kskssj", getKskssj())
            .append("ksjssj", getKsjssj())
            .append("djsc", getDjsc())
            .append("dtcs", getDtcs())
            .append("fsdj", getFsdj())
            .append("zfs", getZfs())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
