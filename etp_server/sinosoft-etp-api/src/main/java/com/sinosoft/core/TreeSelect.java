package com.sinosoft.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sinosoft.admin.category.domain.CategoryVO;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
//import com.sinosoft.admin.question.domain.vo.QuestionCategoryVO;

/**
 * Treeselect树结构实体类
 *
 *
 */
public class TreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(CategoryVO categoryVO) {
        this.id = categoryVO.getCatId();
        this.label = categoryVO.getName();
        this.children = categoryVO.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<TreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelect> children)
    {
        this.children = children;
    }
}
