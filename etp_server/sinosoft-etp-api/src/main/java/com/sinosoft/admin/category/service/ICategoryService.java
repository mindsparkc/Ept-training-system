package com.sinosoft.admin.category.service;

import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.CategoryVO;
import com.sinosoft.core.TreeSelect;

import java.util.List;

/**
 * 分类树操作的基础类
 *
 * create table #{tableName}
 * (
 *     cat_id               bigint not null auto_increment  comment '分类ID',
 *     name                 varchar(255)  comment '分类名称',
 *     parent_id            bigint default NULL  comment '分类父ID',
 *     ancestors            varchar(255)  comment '分类祖级列表',
 *     sort                 tinyint  comment '排序字段',
 *     del_flag             char(1) not null default '0'  comment '删除标记（0有效 2删除）',
 *     create_by            varchar(64) not null default ''  comment '创建者',
 *     create_time          timestamp default CURRENT_TIMESTAMP  comment '创建时间',
 *     update_by            varchar(64) not null default ''  comment '更新者',
 *     update_time          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  comment '更新时间',
 *     remark               varchar(500) not null default ''  comment '备注',
 *     primary key (cat_id)
 * )
 *
 * create table #{tableName}
 * (
 *     cat_id               bigint not null  comment '分类ID',
 *     obj_id                bigint not null  comment '题目ID'
 * )
 *
 * @author wanglijie
 */
public interface ICategoryService {
    // 查询分类
    public List<Category> select(CategoryType.CategoryEnum categoryEnum, Category category);

    public boolean checkNameExists(CategoryType.CategoryEnum categoryEnum, Long catId, Long parentId, String name);

    public boolean hasChild(CategoryType.CategoryEnum categoryEnum, List<Long> catIds);

    public List<TreeSelect> buildCategoryTreeSelect(List<Category> categories);
    public List<CategoryVO> buildCategoryTree(List<Category> categories);

    public Category selectById(CategoryType.CategoryEnum categoryEnum, Long catId);

    // 新增分类
    public int insert(CategoryType.CategoryEnum categoryEnum, Category category);

    // 删除分类
    public int deleteById(CategoryType.CategoryEnum categoryEnum, Long catId);
    public int deleteByIds(CategoryType.CategoryEnum categoryEnum, List<Long> catIds);

    // 更新分类
    public int updateCategory(CategoryType.CategoryEnum categoryEnum, Category category);
}
