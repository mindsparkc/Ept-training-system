package com.sinosoft.etp.cp.testpaper.service;


import com.sinosoft.etp.cp.testpaper.domain.TTestpaperWithCp;

import java.util.List;

/**
 * 试卷Service接口
 *
 *
 * @date 2021-11-19
 */
public interface ITTestpaperWithCpService
{
    /**
     * 查询试卷
     *
     * @param id 试卷ID
     * @return 试卷
     */
    public TTestpaperWithCp selectTTestpaperById(Long id);

    /**
     * 查询试卷列表
     *
     * @param tTestpaper 试卷
     * @return 试卷集合
     */
    public List<TTestpaperWithCp> selectTTestpaperList(TTestpaperWithCp tTestpaper);

    /**
     * 新增试卷
     *
     * @param tTestpaper 试卷
     * @return 结果
     */
    public Long insertTTestpaper(TTestpaperWithCp tTestpaper);

    /**
     * 修改试卷
     *
     * @param tTestpaper 试卷
     * @return 结果
     */
    public int updateTTestpaper(TTestpaperWithCp tTestpaper);

    /**
     * 批量删除试卷
     *
     * @param ids 需要删除的试卷ID
     * @return 结果
     */
    public int deleteTTestpaperByIds(Long[] ids);

    /**
     * 删除试卷信息
     *
     * @param id 试卷ID
     * @return 结果
     */
    public int deleteTTestpaperById(Long id);

    /**
     * 撤回已发布的试卷
     * @param id 试卷ID
     */
    void pushBack(Long id);

    public boolean checkExistSJ(TTestpaperWithCp tTestpaper);
}
