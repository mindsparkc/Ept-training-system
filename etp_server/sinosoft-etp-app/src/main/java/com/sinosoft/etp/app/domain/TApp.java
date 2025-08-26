package com.sinosoft.etp.app.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * app管理对象 t_app
 *
 *
 * @date 2022-07-06
 */
public class TApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** APP 名称 */
    @Excel(name = "APP 标识")
    private String appCode;

    /** APP 名称 */
    @Excel(name = "APP 名称")
    private String appName;

    /** APP 描述 */
    @Excel(name = "APP 描述")
    private String appDescribe;

    /** APP 图标 */
    @Excel(name = "APP 图标")
    private String appIcon;

    /** APP 平台标识，AD 或 IOS */
    @Excel(name = "APP 平台标识，AD 或 IOS")
    private String platform;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    private Long versionCode;

    private String versionDescribe;

    //-----------------------


    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDescribe() {
        return versionDescribe;
    }

    public void setVersionDescribe(String versionDescribe) {
        this.versionDescribe = versionDescribe;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppName()
    {
        return appName;
    }
    public void setAppDescribe(String appDescribe)
    {
        this.appDescribe = appDescribe;
    }

    public String getAppDescribe()
    {
        return appDescribe;
    }
    public void setAppIcon(String appIcon)
    {
        this.appIcon = appIcon;
    }

    public String getAppIcon()
    {
        return appIcon;
    }
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPlatform()
    {
        return platform;
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
            .append("name", getAppName())
            .append("describe", getAppDescribe())
            .append("icon", getAppIcon())
            .append("platform", getPlatform())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
