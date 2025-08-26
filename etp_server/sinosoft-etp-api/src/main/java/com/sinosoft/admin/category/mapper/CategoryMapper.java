package com.sinosoft.admin.category.mapper;

import com.sinosoft.admin.category.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 树形分类操作
 *
 * @author wanglijie
 */
@Mapper
public interface CategoryMapper {
    public List<Category> selectCategory(String sql);
    public int deleteCategory(String sql);
    public int insertCatgory(String sql);
    public int updateCategory(String sql);
}
