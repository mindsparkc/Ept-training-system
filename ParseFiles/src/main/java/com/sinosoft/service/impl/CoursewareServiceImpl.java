package com.sinosoft.service.impl;

import com.sinosoft.entity.Courseware;
import com.sinosoft.mapper.CoursewareMapper;
import com.sinosoft.service.ICoursewareService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CoursewareServiceImpl implements ICoursewareService {
    @Resource
    private CoursewareMapper coursewareMapper;

    @Override
    public List<Courseware> list() {
        List<Courseware> coursewares = coursewareMapper.list();
        return coursewares;
    }

    @Override
    public  Integer update(Courseware courseware) {
        return coursewareMapper.update(courseware);
    }
}
