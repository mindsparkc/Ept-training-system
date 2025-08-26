package com.sinosoft.admin.knowledge.service;

import com.sinosoft.admin.knowledge.domain.Knowledge;

import java.util.List;

/**
 * 知识点Service接口
 *
 *
 * @date 2022-08-10
 */
public interface IKnowledgeService
{
    /**
     * 查询知识点
     *
     * @param knoledgeId 知识点ID
     * @return 知识点
     */
    public Knowledge selectKnowledgeById(Long knoledgeId);

    /**
     * 查询知识点列表
     *
     * @param knowledge 知识点
     * @return 知识点集合
     */
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge);

    /**
     * 查询知识点列表
     *
     * @param knoledgeIds 需要查询的知识点ID
     * @return 知识点集合
     */
    public List<Knowledge> selectKnowledgeByIds(Long[] knoledgeIds);

    /**
     * 新增知识点
     *
     * @param knowledge 知识点
     * @return 结果
     */
    public int insertKnowledge(Knowledge knowledge);

    /**
     * 修改知识点
     *
     * @param knowledge 知识点
     * @return 结果
     */
    public int updateKnowledge(Knowledge knowledge);

    /**
     * 批量删除知识点
     *
     * @param knoledgeIds 需要删除的知识点ID
     * @return 结果
     */
    public int deleteKnowledgeByIds(Long[] knoledgeIds);

    /**
     * 删除知识点信息
     *
     * @param knoledgeId 知识点ID
     * @return 结果
     */
    public int deleteKnowledgeById(Long knoledgeId);

    public List<Knowledge> getChildList(Long parentId);
}
