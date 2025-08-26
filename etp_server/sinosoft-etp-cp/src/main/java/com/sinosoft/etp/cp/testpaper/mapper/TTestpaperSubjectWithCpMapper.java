package com.sinosoft.etp.cp.testpaper.mapper;

import com.sinosoft.etp.cp.testpaper.domain.TTestpaperSubjectWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试卷关联题目Mapper接口
 *
 *
 * @date 2021-12-17
 */
public interface TTestpaperSubjectWithCpMapper
{
    /**
     * 查询试卷关联题目
     *
     * @param id 试卷关联题目ID
     * @return 试卷关联题目
     */
    public TTestpaperSubjectWithCp selectTTestpaperSubjectById(Long id);

    /**
     * 查询试卷关联题目列表
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 试卷关联题目集合
     */
    public List<TTestpaperSubjectWithCp> selectTTestpaperSubjectList(TTestpaperSubjectWithCp tTestpaperSubject);

    /**
     * 新增试卷关联题目
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 结果
     */
    public int insertTTestpaperSubject(TTestpaperSubjectWithCp tTestpaperSubject);

    public int insertTTestpaperSubjects(List<TTestpaperSubjectWithCp> list);

    /**
     * 修改试卷关联题目
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 结果
     */
    public int updateTTestpaperSubject(TTestpaperSubjectWithCp tTestpaperSubject);

    /**
     * 删除试卷关联题目
     *
     * @param id 试卷关联题目ID
     * @return 结果
     */
    public int deleteTTestpaperSubjectById(Long id);

    public int deleteTestpaperSubjectBySjid(Long sjid);

    /**
     * 批量删除试卷关联题目
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestpaperSubjectByIds(Long[] ids);

    public int updateScore(@Param("sjid") Long sjid, @Param("type")String type, @Param("score")String score);

    public List<Map<String,Object>> getSubjectByPaper(Long sjid);
    public List<Map<String,Object>> getPaperSubjectTags(Long sjid);

    /**
     * 获取题目分值
     * @param sjid
     * @return
     */
    public List<Map<String,Object>> selectScoreByType(Long sjid);

    public int updateDa(@Param("id")String id,@Param("url")String url,@Param("val")String val);
    public int updateDaScore(@Param("id")String id,@Param("url")String url,@Param("score")String score);
    public int updatePjzt(@Param("id")String id,@Param("userName")String userName);
    public String getScore(@Param("id")String id);
}
