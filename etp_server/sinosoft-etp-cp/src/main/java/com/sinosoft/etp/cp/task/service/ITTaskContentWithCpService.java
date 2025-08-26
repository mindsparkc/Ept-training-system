package com.sinosoft.etp.cp.task.service;


import com.sinosoft.etp.cp.task.domain.TTaskContentWithCp;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 *
 * @date 2021-12-08
 */
public interface ITTaskContentWithCpService {
    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TTaskContentWithCp> selectTTaskContentList(TTaskContentWithCp tTaskContent);

    /**
     * 新增【请填写功能名称】
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 结果
     */
    public int insertTTaskContent(TTaskContentWithCp tTaskContent);

    /**
     * 修改【请填写功能名称】
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 结果
     */
    public int updateTTaskContent(TTaskContentWithCp tTaskContent);


    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTTaskContentById(Long id);
}
