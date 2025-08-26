package com.sinosoft.admin.category.mapper;

import com.sinosoft.admin.category.domain.ObjectInterCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ObjectInterCategoryMapper {
    public List<ObjectInterCategory> selectInter(String sql);
    public int insertInter(String sql);
    public int updateInter(String sql);
    public int deleteInter(String sql);
}
