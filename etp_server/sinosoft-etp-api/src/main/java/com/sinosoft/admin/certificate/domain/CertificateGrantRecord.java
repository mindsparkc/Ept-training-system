package com.sinosoft.admin.certificate.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 电子证书颁发记录对象 certificate_grant_record
 *
 *
 * @date 2022-07-20
 */
public class CertificateGrantRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 颁发记录编号 */
    private Long recordId;

    /** 证书模板编号 */
    @Excel(name = "证书模板编号")
    private Long tempId;

    /** 证书编号（需要定义证书编号规则） */
    @Excel(name = "证书编号", readConverterExp = "需=要定义证书编号规则")
    private String certId;

    /** 证书名称（与模板表一致） */
    @Excel(name = "证书名称", readConverterExp = "与=模板表一致")
    private String certName;

    /** 生成证书原文件（word） */
    private String certFile;

    /** 证书预览地址（pdf） */
    private String previewFile;

    /** 证书过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证书过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 颁发方式（1人工颁发） */
    private String grantType;

    /** 颁发方式（中文） */
    private String grantDisplay;

    /** 证书所有者 */
    @Excel(name = "证书所有者")
    private Long userId;

    /** 证书状态（0暂存 1撤销 99有效） */
    private String status;
    private String nickName;

    private String content;

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId()
    {
        return recordId;
    }
    public void setTempId(Long tempId)
    {
        this.tempId = tempId;
    }

    public Long getTempId()
    {
        return tempId;
    }
    public void setCertId(String certId)
    {
        this.certId = certId;
    }

    public String getCertId()
    {
        return certId;
    }
    public void setCertName(String certName)
    {
        this.certName = certName;
    }

    public String getCertName()
    {
        return certName;
    }
    public void setCertFile(String certFile)
    {
        this.certFile = certFile;
    }

    public String getCertFile()
    {
        return certFile;
    }
    public void setPreviewFile(String previewFile)
    {
        this.previewFile = previewFile;
    }

    public String getPreviewFile()
    {
        return previewFile;
    }
    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
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
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("recordId", getRecordId())
                .append("tempId", getTempId())
                .append("certId", getCertId())
                .append("certName", getCertName())
                .append("certFile", getCertFile())
                .append("previewFile", getPreviewFile())
                .append("expireTime", getExpireTime())
                .append("grantType", getGrantType())
                .append("grantDisplay", getGrantDisplay())
                .append("userId", getUserId())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
