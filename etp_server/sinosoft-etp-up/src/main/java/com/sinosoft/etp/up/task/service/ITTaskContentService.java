package com.sinosoft.etp.up.task.service;


import com.sinosoft.etp.up.task.domain.TTaskContent;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 *
 * @date 2021-12-08
 */
public interface ITTaskContentService {
    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TTaskContent> selectTTaskContentList(TTaskContent tTaskContent);


    /**
     * 修改【请填写功能名称】
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 结果
     */
    public int updateTTaskContent(TTaskContent tTaskContent);


}
