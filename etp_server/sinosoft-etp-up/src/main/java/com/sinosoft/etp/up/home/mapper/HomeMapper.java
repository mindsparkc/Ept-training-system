package com.sinosoft.etp.up.home.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 意见反馈Mapper接口
 *
 *
 * @author 86151
 * @date 2022-02-18
 */
public interface HomeMapper
{
    /**
     * 查询当前学员的证书数量
     * @param userId
     * @return
     */
    public int getZsNum(@Param("userId") Long userId);
}
