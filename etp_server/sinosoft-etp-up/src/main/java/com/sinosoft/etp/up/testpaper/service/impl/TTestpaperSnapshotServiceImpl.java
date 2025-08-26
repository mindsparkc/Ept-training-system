package com.sinosoft.etp.up.testpaper.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperSnapshot;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperSnapshotMapper;
import com.sinosoft.etp.up.testpaper.service.ITTestpaperSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试卷快照
Service业务层处理
 *
 *
 * @date 2022-02-28
 */
@Service
public class TTestpaperSnapshotServiceImpl implements ITTestpaperSnapshotService
{
    @Autowired
    private TTestpaperSnapshotMapper tTestpaperSnapshotMapper;

    /**
     * 查询试卷快照

     *
     * @param id 试卷快照
ID
     * @return 试卷快照

     */
    @Override
    public TTestpaperSnapshot selectTTestpaperSnapshotById(Integer id)
    {
        return tTestpaperSnapshotMapper.selectTTestpaperSnapshotById(id);
    }

    /**
     * 查询试卷快照列表
     *
     * @param tTestpaperSnapshot 试卷快照

     * @return 试卷快照

     */
    @Override
    public List<TTestpaperSnapshot> selectTTestpaperSnapshotList(TTestpaperSnapshot tTestpaperSnapshot)
    {
        return tTestpaperSnapshotMapper.selectTTestpaperSnapshotList(tTestpaperSnapshot);
    }

    /**
     * 新增试卷快照

     *
     * @param tTestpaperSnapshot 试卷快照

     * @return 结果
     */
    @Override
    public int insertTTestpaperSnapshot(TTestpaperSnapshot tTestpaperSnapshot)
    {
        tTestpaperSnapshot.setCreateTime(DateUtils.getNowDate());
        return tTestpaperSnapshotMapper.insertTTestpaperSnapshot(tTestpaperSnapshot);
    }

    /**
     * 修改试卷快照

     *
     * @param tTestpaperSnapshot 试卷快照

     * @return 结果
     */
    @Override
    public int updateTTestpaperSnapshot(TTestpaperSnapshot tTestpaperSnapshot)
    {
        tTestpaperSnapshot.setUpdateTime(DateUtils.getNowDate());
        return tTestpaperSnapshotMapper.updateTTestpaperSnapshot(tTestpaperSnapshot);
    }

    /**
     * 批量删除试卷快照

     *
     * @param ids 需要删除的试卷快照
ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperSnapshotByIds(String[] ids)
    {
        return tTestpaperSnapshotMapper.deleteTTestpaperSnapshotByIds(ids);
    }

    /**
     * 删除试卷快照
信息
     *
     * @param id 试卷快照
ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperSnapshotById(String id)
    {
        return tTestpaperSnapshotMapper.deleteTTestpaperSnapshotById(id);
    }
}
