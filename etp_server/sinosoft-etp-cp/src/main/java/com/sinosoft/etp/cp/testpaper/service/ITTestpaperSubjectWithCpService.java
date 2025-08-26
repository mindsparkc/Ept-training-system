package com.sinosoft.etp.cp.testpaper.service;

import com.sinosoft.etp.cp.testpaper.domain.TTestpaperSubjectWithCp;

import java.util.List;
import java.util.Map;

/**
 * 试卷关联题目Service接口
 *
 *
 * @date 2021-12-17
 */
public interface ITTestpaperSubjectWithCpService
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
    /**
     * 批量新增试卷关联题目
     *
     * @param list 试卷关联题目
     * @return 结果
     */
    public int insertTTestpaperSubjects(List<TTestpaperSubjectWithCp> list);

    /**
     * 修改试卷关联题目
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 结果
     */
    public int updateTTestpaperSubject(TTestpaperSubjectWithCp tTestpaperSubject);

    /**
     * 批量删除试卷关联题目
     *
     * @param ids 需要删除的试卷关联题目ID
     * @return 结果
     */
    public int deleteTTestpaperSubjectByIds(Long[] ids);

    /**
     * 删除试卷关联题目信息
     *
     * @param id 试卷关联题目ID
     * @return 结果
     */
    public int deleteTTestpaperSubjectById(Long id);
    public int deleteTestpaperSubjectBySjid(Long sjid);

    public List<Map<String,Object>> getSubjectByPaper(Long sjid);
    public List<Map<String,Object>> getPaperSubjectTags(Long sjid);

    public int updateScore(Long sjid,String type,String score,String fb);

    public List<Map<String,Object>> selectScoreByType(Long sjid);
    //增加试卷评卷信息
    public int updateDa(Map<String,Object>params);
}
