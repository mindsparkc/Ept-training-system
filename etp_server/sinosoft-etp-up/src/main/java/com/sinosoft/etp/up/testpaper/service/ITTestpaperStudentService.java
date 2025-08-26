package com.sinosoft.etp.up.testpaper.service;

import com.sinosoft.etp.up.testpaper.domain.TTestPaperSnapshotJson;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import com.sinosoft.etp.up.testpaper.dto.TestPaperReviewDetail;

import java.util.List;

/**
 * 试卷学员关联Service接口
 *
 *
 * @date 2022-02-28
 */
public interface ITTestpaperStudentService
{
    /**
     * 查询试卷学员关联
     *
     * @param id 试卷学员关联ID
     * @return 试卷学员关联
     */
    public TTestpaperStudent selectTTestpaperStudentById(Integer id);

    /**
     * 查询试卷学员关联列表
     *
     * @param tTestpaperStudent 试卷学员关联
     * @return 试卷学员关联集合
     */
    public List<TTestpaperStudent> selectTTestpaperStudentList(TTestpaperStudent tTestpaperStudent);

    /**
     * 新增试卷学员关联
     *
     * @param tTestpaperStudent 试卷学员关联
     * @return 结果
     */
    public int insertTTestpaperStudent(TTestpaperStudent tTestpaperStudent);

    /**
     * 修改试卷学员关联
     *
     * @param tTestpaperStudent 试卷学员关联
     * @return 结果
     */
    public int updateTTestpaperStudent(TTestpaperStudent tTestpaperStudent);

    /**
     * 批量删除试卷学员关联
     *
     * @param ids 需要删除的试卷学员关联ID
     * @return 结果
     */
    public int deleteTTestpaperStudentByIds(String[] ids);

    /**
     * 删除试卷学员关联信息
     *
     * @param id 试卷学员关联ID
     * @return 结果
     */
    public int deleteTTestpaperStudentById(String id);

    TestPaperReviewDetail getScore(String id);

    List<TTestpaperStudent> getHistoryExam(TTestpaperStudent tTestpaperStudent);

    /**
     * 查看答题信息
     * @param id
     * @return
     */
    TTestPaperSnapshotJson selectAnswerInfo(Long id);
}
