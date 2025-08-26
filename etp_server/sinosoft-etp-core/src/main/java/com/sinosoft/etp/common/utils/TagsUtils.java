package com.sinosoft.etp.common.utils;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.redis.RedisCache;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.spring.SpringUtils;
import com.sinosoft.etp.common.domain.TTag;

import java.util.List;


public class TagsUtils {
    public static final Long QYID = SecurityUtils.getLoginUser().getUser().getDeptId();

    /**
     * 设置企业关联标签缓存
     *
     * @param key 功能名例如：KJ(课件)、TK(题库)
     * @param tagsDatas 字典数据列表
     */
    public static void setTagsCache(String key, List<TTag> tagsDatas)
    {
        SpringUtils.getBean(RedisCache.class).setCacheObject(QYID+"_"+key, tagsDatas);
    }

    /**
     * 获取企业关联标签缓存
     *
     * @param key 参数键
     * @return tagsDatas 标签数据列表
     */
    public static List<TTag> getTagCache(String key)
    {
        Object cacheObj = SpringUtils.getBean(RedisCache.class).getCacheObject(QYID+"_"+key);
        if (StringUtils.isNotNull(cacheObj))
        {
            List<TTag> tagsDatas = StringUtils.cast(cacheObj);
            return tagsDatas;
        }
        return null;
    }
}
