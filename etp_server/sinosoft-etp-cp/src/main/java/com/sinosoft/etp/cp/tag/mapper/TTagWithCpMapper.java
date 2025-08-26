package com.sinosoft.etp.cp.tag.mapper;

import com.sinosoft.etp.common.domain.TTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 标签管理Mapper接口
 *
 *
 * @date 2021-11-15
 */
public interface TTagWithCpMapper
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
     * 删除标签管理
     *
     * @param id 标签管理ID
     * @return 结果
     */
    public int deleteTTagById(Long id);

    /**
     * 批量删除标签管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTagByIds(Long[] ids);

    public List<TTag> listRelateTag(@Param("tableName")String tableName);

    public int commpanyInsertTag(List<TTag> tags);

    public List<Integer> getTagIdsByName(String[] tagNames);

    public int insertRelateTag(@Param("map") Map<String,Object> map);

    public int  deleteRelateTag(@Param("relateId") Long relateModelId,@Param("tableName")String tableName,@Param("filed")String filed);
}
