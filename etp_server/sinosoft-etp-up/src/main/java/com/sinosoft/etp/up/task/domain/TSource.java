package com.sinosoft.etp.up.task.domain;

import com.sinosoft.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 任务对象 t_task
 *
 *
 * @date 2021-12-08
 */
public class TSource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 任务名称 */
    private String name;

    /** 任务描述 */
    private String type;
    private Double rate;

    /** 任务描述 */
    private List<TSource> sources;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TSource> getSources() {
        return sources;
    }

    public void setSources(List<TSource> sources) {
        this.sources = sources;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
