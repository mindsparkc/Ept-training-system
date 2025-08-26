package com.sinosoft.service;

import com.sinosoft.entity.Courseware;

import java.util.List;

/**
 * 查询数据库，获取要发布的转换任务
 *
 * @author WANGLIJIE
 */
public interface ICoursewareService {
    public List<Courseware> list();
    public Integer update(Courseware courseware);
}
