package com.sinosoft.etp.cp.testpaper.mapper;

import com.sinosoft.etp.cp.testpaper.domain.TTestpaperStudentWithCp;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷Mapper接口
 *
 *
 * @date 2021-11-19
 */
public interface TTestpaperWithCpMapper
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
    public int insertTTestpaper(TTestpaperWithCp tTestpaper);

    /**
     * 修改试卷
     *
     * @param tTestpaper 试卷
     * @return 结果
     */
    public int updateTTestpaper(TTestpaperWithCp tTestpaper);

    /**
     * 删除试卷
     *
     * @param id 试卷ID
     * @return 结果
     */
    public int deleteTTestpaperById(Long id);

    /**
     * 批量删除试卷
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestpaperByIds(Long[] ids);
    /**
     * 退回已发布的课程
     * @param id
     */
    void pushBack(@Param("id") Long id);

    /**
     * 考试统计用于查询学员分数详情
     * @param ksid
     */
    public List<TTestpaperStudentWithCp> getExamOfStudentInfo(@Param("ksid") Long ksid,@Param("deptId")Long deptId);

    public TTestpaperWithCp checkExistSJ(@Param("sjmc") String sjmc);
}
