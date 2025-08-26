package com.sinosoft.etp.cp.task.domain;

import com.sinosoft.common.core.domain.BaseEntity;

/**
 * 任务内容项实体类
 *
 * */
public class TTaskContentWithCp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id 自增 */
    private Long id;
    /** 任务ID*/
    private Long rwid;
    /** 课程或者考试ID */
    private Long sid;
    /** 类型 kc-课程 ks-考试*/
    private String source_type;
    /** 课程或者考试名称 */
    private String source_name;
    /** 排序 */
    private int sort;

    public String getSource_name() {
        return source_name;
    }

    public TTaskContentWithCp setSource_name(String source_name) {
        this.source_name = source_name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TTaskContentWithCp setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getRwid() {
        return rwid;
    }

    public TTaskContentWithCp setRwid(Long rwid) {
        this.rwid = rwid;
        return this;
    }

    public Long getSid() {
        return sid;
    }

    public TTaskContentWithCp setSid(Long sid) {
        this.sid = sid;
        return this;
    }

    public String getSource_type() {
        return source_type;
    }

    public TTaskContentWithCp setSource_type(String source_type) {
        this.source_type = source_type;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public TTaskContentWithCp setSort(int sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public String toString() {
        return "TTaskContent{" +
                "id=" + id +
                ", rwid=" + rwid +
                ", sid=" + sid +
                ", source_type='" + source_type + '\'' +
                ", source_name='" + source_name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
