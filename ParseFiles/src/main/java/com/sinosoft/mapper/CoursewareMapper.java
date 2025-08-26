package com.sinosoft.mapper;

import com.sinosoft.entity.Courseware;

import java.util.List;

public interface CoursewareMapper {
    /**
     * 取所有待处理的作业
     *
     * @return
     */
    public List<Courseware> list();
    public Integer update(Courseware courseware);
}
