package com.sinosoft.admin.certificate.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 电子证书模板对象 certificate_template
 *
 *
 * @date 2022-07-15
 */
public class CertificateTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板编号 */
    private Long tempId;

    /** 证书名称 */
    @Excel(name = "证书名称")
    private String certName;

    /** 证书描述 */
    @Excel(name = "证书描述")
    private String content;

    /** 证书颁发机构 */
    @Excel(name = "证书颁发机构")
    private String authority;

    /** 证书有效期(年，0终身有效) */
    @Excel(name = "证书有效期(年，0终身有效)")
    private Long expireYear;

    /** 颁发方式（1人工颁发） */
    @Excel(name = "颁发方式", readConverterExp = "1=人工颁发")
    private String grantType;

    /** 颁发方式（中文） */
    @Excel(name = "颁发方式", readConverterExp = "中=文")
    private String grantDisplay;

    /** 模板地址 */
    @Excel(name = "模板地址")
    private String tempFile;

    /** 预览地址 */
    @Excel(name = "预览地址")
    private String previewFile;

    /** 证书状态（0暂存 99已发布） */
    @Excel(name = "证书状态", readConverterExp = "0=暂存,9=9已发布")
    private String status;

    /** 删除标记（0正常 2删除） */
    private String delFlag;
    /** 文件名称*/
    private String fileName;

    public void setTempId(Long tempId)
    {
        this.tempId = tempId;
    }

    public Long getTempId()
    {
        return tempId;
    }
    public void setCertName(String certName)
    {
        this.certName = certName;
    }

    public String getCertName()
    {
        return certName;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setAuthority(String authority)
    {
        this.authority = authority;
    }

    public String getAuthority()
    {
        return authority;
    }
    public void setExpireYear(Long expireYear)
    {
        this.expireYear = expireYear;
    }

    public Long getExpireYear()
    {
        return expireYear;
    }
    public void setGrantType(String grantType)
    {
        this.grantType = grantType;
    }

    public String getGrantType()
    {
        return grantType;
    }
    public void setGrantDisplay(String grantDisplay)
    {
        this.grantDisplay = grantDisplay;
    }

    public String getGrantDisplay()
    {
        return grantDisplay;
    }
    public void setTempFile(String tempFile)
    {
        this.tempFile = tempFile;
    }

    public String getTempFile()
    {
        return tempFile;
    }
    public void setPreviewFile(String previewFile)
    {
        this.previewFile = previewFile;
    }

    public String getPreviewFile()
    {
        return previewFile;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("tempId", getTempId())
                .append("certName", getCertName())
                .append("content", getContent())
                .append("authority", getAuthority())
                .append("expireYear", getExpireYear())
                .append("grantType", getGrantType())
                .append("grantDisplay", getGrantDisplay())
                .append("tempFile", getTempFile())
                .append("previewFile", getPreviewFile())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
