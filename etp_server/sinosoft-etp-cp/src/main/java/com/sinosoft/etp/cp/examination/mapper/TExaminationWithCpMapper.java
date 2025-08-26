package com.sinosoft.etp.cp.examination.mapper;

import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试Mapper接口
 *
 * 
 * @date 2021-12-24
 */
public interface TExaminationWithCpMapper
{
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExaminationWithCp selectTExaminationById(Long id);

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试集合
     */
    public List<TExaminationWithCp> selectTExaminationList(TExaminationWithCp tExamination);

    /**
     * 新增考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    public int insertTExamination(TExaminationWithCp tExamination);

    /**
     * 修改考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    public int updateTExamination(TExaminationWithCp tExamination);
    public int updateTExaminationFb(TExaminationWithCp tExamination);

    /**
     * 删除考试
     *
     * @param id 考试ID
     * @return 结果
     */
    public int deleteTExaminationById(Long id);

    /**
     * 批量删除考试
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTExaminationByIds(Long[] ids);

    public List<Long> checkExaminations(Long[] ids);

    public int updateExamStatus();

    public TExaminationWithCp checkExistKS(@Param("mc") String mc);
}
