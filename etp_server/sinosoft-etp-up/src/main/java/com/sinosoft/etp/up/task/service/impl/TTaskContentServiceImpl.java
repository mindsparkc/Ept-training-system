package com.sinosoft.etp.up.task.service.impl;

import com.sinosoft.etp.up.task.domain.TTaskContent;
import com.sinosoft.etp.up.task.mapper.TTaskContentMapper;
import com.sinosoft.etp.up.task.service.ITTaskContentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TTaskContentServiceImpl implements ITTaskContentService
{
    @Autowired
    private TTaskContentMapper tTaskContentMapper;


    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TTaskContent> selectTTaskContentList(TTaskContent tTaskContent)
    {
        return tTaskContentMapper.selectTTaskContentList(tTaskContent);
    }


    /**
     * 修改【请填写功能名称】
     *
     * @param tTaskContent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTTaskContent(TTaskContent tTaskContent)
    {
        return tTaskContentMapper.updateTTaskContent(tTaskContent);
    }


}
