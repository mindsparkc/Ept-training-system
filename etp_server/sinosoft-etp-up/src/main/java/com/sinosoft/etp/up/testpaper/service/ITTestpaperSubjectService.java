package com.sinosoft.etp.up.testpaper.service;

import com.sinosoft.etp.up.testpaper.domain.TTestPaperSnapshotJson;
import com.sinosoft.etp.up.testpaper.dto.PaperSnapshotDTO;

import java.util.List;
import java.util.Map;

/**
 * 试卷关联题目Service接口
 *
 *
 * @date 2021-12-17
 */
public interface ITTestpaperSubjectService
{
    /**
     * 查询试卷关联题目
     *
     * @param sjid 试卷关联题目ID
     * @return 试卷关联题目
     */

    public List<Map<String,Object>> getSubjectByPaper(Long sjid);

    /**
     * 开始考试
     * @param ksid
     * @return
     */
    PaperSnapshotDTO start(Long ksid);

    void markThePapers(TTestPaperSnapshotJson tTestPaperSnapshotJson);
}
