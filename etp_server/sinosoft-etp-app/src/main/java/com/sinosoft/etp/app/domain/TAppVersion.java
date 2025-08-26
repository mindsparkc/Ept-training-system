package com.sinosoft.etp.app.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * app版本管理对象 t_app_version
 *
 *
 * @date 2022-07-06
 */
public class TAppVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** APP 主键 */
    @Excel(name = "APP 主键")
    private Long appId;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long versionCode;

    /** 版本号名称 */
    @Excel(name = "版本号名称")
    private String versionName;

    /** 版本升级描述 */
    @Excel(name = "版本升级描述")
    private String versionDescribe;

    /** 下载地址 */
    @Excel(name = "下载地址")
    @PreProcessField
    private String download;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 平台信息（安卓或ios） */
    private String platform;
    /** app标识 */
    private String appCode;
    /** 上传文件名称 */
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setVersionCode(Long versionCode)
    {
        this.versionCode = versionCode;
    }

    public Long getVersionCode()
    {
        return versionCode;
    }
    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getVersionName()
    {
        return versionName;
    }
    public void setVersionDescribe(String versionDescribe)
    {
        this.versionDescribe = versionDescribe;
    }

    public String getVersionDescribe()
    {
        return versionDescribe;
    }
    public void setDownload(String download)
    {
        this.download = download;
    }

    public String getDownload()
    {
        return download;
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
            .append("appId", getAppId())
            .append("versionCode", getVersionCode())
            .append("versionName", getVersionName())
            .append("versionDescribe", getVersionDescribe())
            .append("download", getDownload())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
