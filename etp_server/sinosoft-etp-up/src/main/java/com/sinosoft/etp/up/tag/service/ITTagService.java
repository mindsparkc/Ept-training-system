package com.sinosoft.etp.up.tag.service;


import com.sinosoft.etp.common.domain.TTag;

import java.util.List;


/**
 * 标签管理Service接口
 *
 *
 * @date 2021-11-15
 */
public interface ITTagService
{
    /**
     * 查询标签管理
     *
     * @param id 标签管理ID
     * @return 标签管理
     */
    public TTag selectTTagById(Long id);

    /**
     * 查询标签管理列表
     *
     * @param tTag 标签管理
     * @return 标签管理集合
     */
    public List<TTag> selectTTagList(TTag tTag);


    /**
     * 新增标签管理
     *
     * @param tTag 标签管理
     * @return 结果
     */
    public int insertTTag(TTag tTag);

    /**
     * 修改标签管理
     *
     * @param tTag 标签管理
     * @return 结果
     */
    public int updateTTag(TTag tTag);

    /**
     * 批量删除标签管理
     *
     * @param ids 需要删除的标签管理ID
     * @return 结果
     */
    public int deleteTTagByIds(Long[] ids);

    /**
     * 删除标签管理信息
     *
     * @param id 标签管理ID
     * @return 结果
     */
    public int deleteTTagById(Long id);

    /**
     * 查询xx-标签关联表
     * @param type
     * @return
     */
    public List<TTag> listRelateTag(String type);

    /**
     * 企业新增标签管理
     *
     * @param tagStr 标签管理
     * @return 结果
     */
    public int commpanyInsertTag(String tagStr);

    /**
     * 企业新增标签关联表
     * @param modelId 模块id（题库id，课件i的....）
     * @param modelType 模块类型：TK(题库),KJ(课件)....
     * @return 结果
     */
    public int insertRelateTag(Long modelId,String tagStr,String modelType);

    public int deleteRelateTag(Long modelId,String modelType);

    /**
     * 加载企业关联标签缓存数据
     *  @param modelType 模块类型：TK(题库),KJ(课件)....
     */
    public void loadingTagsCache(String modelType);

}
