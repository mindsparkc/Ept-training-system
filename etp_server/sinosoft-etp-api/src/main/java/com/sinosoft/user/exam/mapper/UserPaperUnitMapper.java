package com.sinosoft.user.exam.mapper;

import com.sinosoft.core.domain.PaperUnit;

import java.util.List;

public interface UserPaperUnitMapper {
    public List<PaperUnit> selectPaperUnitByPaperId(Long paperId);
}
