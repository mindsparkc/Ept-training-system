package com.sinosoft.admin.paper.domain;

import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.user.exam.domain.UserExam;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 试卷预览对象 paper_preview
 *
 * @author sinosoft
 * @date 2022-10-08
 */
public class PaperPreview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 试卷编号 */
    private Long paperId;

    /** 版本号（Linux时间戳） */
    private Long version;

    /** 试卷预览快照 */
    private UserExam content;

    public void setPaperId(Long paperId)
    {
        this.paperId = paperId;
    }

    public Long getPaperId()
    {
        return paperId;
    }
    public void setVersion(Long version)
    {
        this.version = version;
    }

    public Long getVersion()
    {
        return version;
    }
    public void setContent(UserExam content)
    {
        this.content = content;
    }

    public UserExam getContent()
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("paperId", getPaperId())
                .append("version", getVersion())
                .append("content", getContent())
                .toString();
    }
}