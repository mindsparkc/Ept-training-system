package com.sinosoft.etp.up.testpaper.service;

import com.sinosoft.etp.up.testpaper.domain.TTestpaperSnapshot;

import java.util.List;

/**
 * 试卷快照
Service接口
 *
 * 
 * @date 2022-02-28
 */
public interface ITTestpaperSnapshotService
{
    /**
     * 查询试卷快照

     *
     * @param id 试卷快照
ID
     * @return 试卷快照

     */
    public TTestpaperSnapshot selectTTestpaperSnapshotById(Integer id);

    /**
     * 查询试卷快照
列表
     *
     * @param tTestpaperSnapshot 试卷快照

     * @return 试卷快照
集合
     */
    public List<TTestpaperSnapshot> selectTTestpaperSnapshotList(TTestpaperSnapshot tTestpaperSnapshot);

    /**
     * 新增试卷快照

     *
     * @param tTestpaperSnapshot 试卷快照

     * @return 结果
     */
    public int insertTTestpaperSnapshot(TTestpaperSnapshot tTestpaperSnapshot);

    /**
     * 修改试卷快照

     *
     * @param tTestpaperSnapshot 试卷快照

     * @return 结果
     */
    public int updateTTestpaperSnapshot(TTestpaperSnapshot tTestpaperSnapshot);

    /**
     * 批量删除试卷快照

     *
     * @param ids 需要删除的试卷快照
ID
     * @return 结果
     */
    public int deleteTTestpaperSnapshotByIds(String[] ids);

    /**
     * 删除试卷快照
信息
     *
     * @param id 试卷快照
ID
     * @return 结果
     */
    public int deleteTTestpaperSnapshotById(String id);
}
