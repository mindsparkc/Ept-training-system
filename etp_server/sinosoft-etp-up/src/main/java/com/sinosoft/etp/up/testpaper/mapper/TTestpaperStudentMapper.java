package com.sinosoft.etp.up.testpaper.mapper;

import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试卷学员关联Mapper接口
 *
 *
 * @date 2022-02-28
 */
public interface TTestpaperStudentMapper
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
     * 考试信息
     * @return
     */
    public TTestpaperStudent selectTTestpaperStudentListByIds(@Param("ksid") Long ksid, @Param("sjid") Long sjid, @Param("xyid") Long xyid);
    public List<TTestpaperStudent> selectTTestpaperStudentInfoByIds(@Param("ksid") Long ksid, @Param("sjid") Long sjid, @Param("xyid") Long xyid);
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
     * 删除试卷学员关联
     *
     * @param id 试卷学员关联ID
     * @return 结果
     */
    public int deleteTTestpaperStudentById(String id);

    /**
     * 批量删除试卷学员关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestpaperStudentByIds(String[] ids);

    List<TTestpaperStudent> selectTTestpaperStudentHistoryExamList(TTestpaperStudent tTestpaperStudent);

    List<Map<String,Object>> selectAnswerInfo(Long id);

    TTestpaperStudent selectIsSubmit(@Param("ksid")Long ksid);
}
