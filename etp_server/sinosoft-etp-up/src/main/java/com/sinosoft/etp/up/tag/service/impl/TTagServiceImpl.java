package com.sinosoft.etp.up.tag.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.domain.TTag;
import com.sinosoft.etp.common.utils.TableFiledEnum;
import com.sinosoft.etp.common.utils.TagsUtils;
import com.sinosoft.etp.up.tag.mapper.TTagMapper;
import com.sinosoft.etp.up.tag.service.ITTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 标签管理Service业务层处理
 *
 *
 * @date 2021-11-15
 */
@Service
public class TTagServiceImpl implements ITTagService
{
    @Autowired
    private TTagMapper tTagMapper;

    /**
     * 查询标签管理
     *
     * @param id 标签管理ID
     * @return 标签管理
     */
    @Override
    public TTag selectTTagById(Long id)
    {
        return tTagMapper.selectTTagById(id);
    }

    /**
     * 查询标签管理列表
     *
     * @param tTag 标签管理
     * @return 标签管理
     */
    @Override
    public List<TTag> selectTTagList(TTag tTag)
    {
        return tTagMapper.selectTTagList(tTag);
    }

    /**
     * 新增标签管理
     *
     * @param tTag 标签管理
     * @return 结果
     */
    @Override
    public int insertTTag(TTag tTag)
    {
        tTag.setCreateTime(DateUtils.getNowDate());
        return tTagMapper.insertTTag(tTag);
    }

    /**
     * 修改标签管理
     *
     * @param tTag 标签管理
     * @return 结果
     */
    @Override
    public int updateTTag(TTag tTag)
    {
        tTag.setUpdateTime(DateUtils.getNowDate());
        return tTagMapper.updateTTag(tTag);
    }

    /**
     * 批量删除标签管理
     *
     * @param ids 需要删除的标签管理ID
     * @return 结果
     */
    @Override
    public int deleteTTagByIds(Long[] ids)
    {
        return tTagMapper.deleteTTagByIds(ids);
    }

    /**
     * 删除标签管理信息
     *
     * @param id 标签管理ID
     * @return 结果
     */
    @Override
    public int deleteTTagById(Long id)
    {
        return tTagMapper.deleteTTagById(id);
    }


    @Override
    public int commpanyInsertTag(String tagStr) {
       String[] tags = tagStr.split(",");
       Date time = new Date();
       List<TTag> list = new ArrayList<>();
       for(String item :tags){
           TTag tag = new TTag();
           tag.setName(item.replace(" ", ""));
           tag.setCreateBy(SecurityUtils.getUsername());
           tag.setCreateTime(time);
           tag.setUpdateTime(time);
           list.add(tag);
       }
       return tTagMapper.commpanyInsertTag(list);
    }

    @Override
    public int insertRelateTag(Long modelId,String tagStr,String modelType) {
        String[] tags = tagStr.split(",");
        //获取插入的标签 id
        List<Integer> tagIds = tTagMapper.getTagIdsByName(tags);
        //根据modelType 获取表名和关联字段
        Map<String,String> map =  TableFiledEnum.tableFiledMap(modelType);
        String tableName = map.get("tableName");
        String filed = map.get("filed");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("tableName",tableName);
        paramMap.put("filed",filed);

        //获取企业id
        //Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();

        Date time = new Date();
        String userName = SecurityUtils.getUsername();

        List<Map<String,Object>> list = new ArrayList<>();

        for(Integer tag:tagIds){
            Map<String,Object> tagMap = new HashMap<>();
            tagMap.put("bqid",tag);
            //tagMap.put("qyid",qyid);
            tagMap.put("createBy",userName);
            tagMap.put("createTime",time);
            tagMap.put("updateTime",time);
            tagMap.put("modelId",modelId);
            list.add(tagMap);
            paramMap.put("list",list);
        }

        int i = tTagMapper.insertRelateTag(paramMap);
        if(i>0){
            //新增成功，更新redis
            List<TTag> tagsDatas = getCompanyModelTags(modelType);
            TagsUtils.setTagsCache(modelType,tagsDatas);
        }
        return i;
    }

    @Override
    public int deleteRelateTag(Long modelId,String modelType) {
        //根据modelType 获取表名和关联字段
        Map<String,String> map =  TableFiledEnum.tableFiledMap(modelType);
        String tableName = map.get("tableName");
        String filed = map.get("filed");
        int i = tTagMapper.deleteRelateTag(modelId,tableName,filed);
        if(i>0){
            //删除成功，更新redis
            List<TTag> tagsDatas = getCompanyModelTags(modelType);
            TagsUtils.setTagsCache(modelType,tagsDatas);
        }
        return i;
    }

    @Override
    public List<TTag> listRelateTag(String type) {
        List<TTag> tagsDatas = TagsUtils.getTagCache(type);
        if(StringUtils.isNotEmpty(tagsDatas)){
            return tagsDatas;
        }
        return getCompanyModelTags(type);
    }


    /**
     * 放入缓存
     * @param modelType 模块类型：TK(题库),KJ(课件)....
     */
    @Override
    public void loadingTagsCache(String modelType) {
        List<TTag> tagsDatas = getCompanyModelTags(modelType);
        TagsUtils.setTagsCache(modelType,tagsDatas);
    }


    /**
     * 获取本企业某个功能的标签
     * @param modelType 模块类型：TK(题库),KJ(课件)....
     * @return
     */
    public List<TTag> getCompanyModelTags(String modelType){
        Map<String,String> map = TableFiledEnum.tableFiledMap(modelType);
        String tableName = map.get("tableName").toString();
        //Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();
        return tTagMapper.listRelateTag(tableName);
    }
}
