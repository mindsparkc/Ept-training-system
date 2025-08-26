package com.sinosoft.etp.up.courseware.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;

/**
 * 课件管理对象 t_courseware
 *
 *
 * @date 2021-11-08
 */
public class TCourseware extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 课件名称 */
    @Excel(name = "课件名称")
    private String mc;

    /** 课件描述 */
    @Excel(name = "课件描述")
    private String ms;

    /** 封面图片 */
    @Excel(name = "封面图片")
    @PreProcessField
    private String fm;

    /** 课件形式
        Office：1
        视频：2
        富文本：3
        外部链接：4
        其他文件：5
     */
    @Excel(name = "课件形式")
    private String xs;

    /** 课件链接
    “课件形式”不为3时，该字段有值 */
    @Excel(name = "课件链接")
    private String lj;

    /** 课件内容
    仅“课件形式”为3时，该字段有值 */
    @Excel(name = "课件内容")
    @PreProcessField
    private String nr;

    /**
     * 当“课件形式”为1、2,、5时该字段有值
     */
    @PreProcessField
    private String kjdz;

    /**
     * 课件文件名称
     */
    private String kjwjmc;

    /**
     * 课件最低学习时间
     */
    private String learnTime;


    /** 开放程度
    完全公开：0
    限定用户：1 */
    @Excel(name = "开放程度")
    private String kfcd;

    /** 删除标志 */
    private String delFlag;

    private String tags;

    private Long downNum;

    /** 文件类型后缀*/
    private String suffix;
    private String metaData;

    /** 历史学习时长*/
    private String lastStudyTime;

    public Long getDownNum() {
        return downNum;
    }

    public TCourseware setDownNum(Long downNum) {
        this.downNum = downNum;
        return this;
    }

    public String getLearnTime() {
        return learnTime;
    }

    public TCourseware setLearnTime(String learnTime) {
        this.learnTime = learnTime;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public TCourseware setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getKjwjmc() {
        return kjwjmc;
    }

    public TCourseware setKjwjmc(String kjwjmc) {
        this.kjwjmc = kjwjmc;
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
    public void setMc(String mc)
    {
        this.mc = mc;
    }

    public String getMc()
    {
        return mc;
    }
    public void setMs(String ms)
    {
        this.ms = ms;
    }

    public String getMs()
    {
        return ms;
    }
    public void setFm(String fm)
    {
        this.fm = fm;
    }

    public String getFm()
    {
        return fm;
    }
    public void setXs(String xs)
    {
        this.xs = xs;
    }

    public String getXs()
    {
        return xs;
    }
    public void setLj(String lj)
    {
        this.lj = lj;
    }

    public String getLj()
    {
        return lj;
    }
    public void setNr(String nr)
    {
        this.nr = nr;
    }

    public String getNr()
    {
        return nr;
    }
    public void setKfcd(String kfcd)
    {
        this.kfcd = kfcd;
    }

    public String getKfcd()
    {
        return kfcd;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getKjdz() {
        return kjdz;
    }

    public TCourseware setKjdz(String kjdz) {
        this.kjdz = kjdz;
        return this;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getLastStudyTime() {
        return lastStudyTime;
    }

    public void setLastStudyTime(String lastStudyTime) {
        this.lastStudyTime = lastStudyTime;
    }

    @Override
    public String toString() {
        return "TCourseware{" +
                "id=" + id +
                ", mc='" + mc + '\'' +
                ", ms='" + ms + '\'' +
                ", fm='" + fm + '\'' +
                ", xs='" + xs + '\'' +
                ", lj='" + lj + '\'' +
                ", nr='" + nr + '\'' +
                ", kjdz='" + kjdz + '\'' +
                ", kfcd='" + kfcd + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
