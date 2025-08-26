package com.sinosoft.etp.cp.task.service.impl;

import com.sinosoft.etp.cp.task.domain.TTaskContentWithCp;
import com.sinosoft.etp.cp.task.mapper.TTaskContentWithCpMapper;
import com.sinosoft.etp.cp.task.service.ITTaskContentWithCpService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TTaskContentWithCpServiceImpl implements ITTaskContentWithCpService
{
    @Autowired
    private TTaskContentWithCpMapper tTaskContentMapper;


    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TTaskContentWithCp> selectTTaskContentList(TTaskContentWithCp tTaskContent)
    {
        return tTaskContentMapper.selectTTaskContentList(tTaskContent);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTTaskContent(TTaskContentWithCp tTaskContent)
    {
        return tTaskContentMapper.insertTTaskContent(tTaskContent);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTTaskContent(TTaskContentWithCp tTaskContent)
    {
        return tTaskContentMapper.updateTTaskContent(tTaskContent);
    }


    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTTaskContentById(Long id)
    {
        return tTaskContentMapper.deleteTTaskContentById(id);
    }
}
