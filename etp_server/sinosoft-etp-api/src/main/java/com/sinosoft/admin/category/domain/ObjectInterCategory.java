package com.sinosoft.admin.category.domain;

public class ObjectInterCategory {
    private static final long serialVersionUID = 1L;

    private Long objId;
    private Long catId;

    private String ancestors;

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }
}
