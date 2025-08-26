package com.sinosoft.user.exam.mapper;

import com.sinosoft.core.domain.Paper;

public interface UserPaperMapper {
    public Paper selectPaperById(Long paperId);
}
