package com.sinosoft.etp.up.examination.mapper;

import com.sinosoft.etp.up.examination.domain.TExamination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试Mapper接口
 *
 *
 * @date 2021-12-24
 */
public interface TExaminationMapper
{
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExamination selectTExaminationById(@Param("id") Long id);

    public TExamination selectTExaminationByKSID(Long id);

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试集合
     */
    public List<TExamination> selectTExaminationList(TExamination tExamination);

    public int getExaminationNum(@Param("xyid")Long xyid);

    public int getGkExam(@Param("id")Long id);

    public int getExamByStudent(@Param("id")Long id,@Param("xyid")Long xyid);

    /**
     * 获取未完成的课程个数
     * @param xyid
     * @return
     */
    public int getExamJksNum(@Param("xyid") Long xyid);

    /**
     * 已领取课程个数
     * @param xyid
     * @return
     */
    public int getExamJjsNum(@Param("xyid") Long xyid);
}
