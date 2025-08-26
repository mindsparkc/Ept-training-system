package com.sinosoft.etp.up.testpaper.mapper;

import java.util.List;
import java.util.Map;

/**
 * 试卷关联题目Mapper接口
 *
 *
 * @date 2021-12-17
 */
public interface TTestpaperSubjectMapper
{
    /**
     * 查询试卷关联题目
     *
     * @param sjid 试卷关联题目ID
     * @return 试卷关联题目
     */
    public List<Map<String,Object>> getSubjectByPaper(Long sjid);

    /**
     * 获取题目分值
     * @param sjid
     * @return
     */
    public double selectScore(Long sjid);
}
