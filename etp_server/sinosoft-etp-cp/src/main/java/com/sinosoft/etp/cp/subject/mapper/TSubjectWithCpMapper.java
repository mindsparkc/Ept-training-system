package com.sinosoft.etp.cp.subject.mapper;

import com.sinosoft.etp.cp.subject.domain.TSubjectWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 题库Mapper接口
 *
 *
 * @date 2021-11-08
 */
public interface TSubjectWithCpMapper
{
    /**
     * 查询题库
     *
     * @param id 题库ID
     * @return 题库
     */
    public TSubjectWithCp selectTSubjectById(Long id);

    /**
     * 查询题库列表
     *
     * @param tSubject 题库
     * @return 题库集合
     */
    public List<TSubjectWithCp> selectTSubjectList(TSubjectWithCp tSubject);

    /**
     * 新增题库
     *
     * @param tSubject 题库
     * @return 结果
     */
    public int insertTSubject(TSubjectWithCp tSubject);

    /**
     * 修改题库
     *
     * @param tSubject 题库
     * @return 结果
     */
    public int updateTSubject(TSubjectWithCp tSubject);

    /**
     * 删除题库
     *
     * @param id 题库ID
     * @return 结果
     */
    public int deleteTSubjectById(Long id);

    /**
     * 批量删除题库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTSubjectByIds(Long[] ids);

    public List<TSubjectWithCp> getSubjectRand(@Param("ids") String ids, @Param("num")int num);

    public List<TSubjectWithCp> getSelectedStByKc(Long kcid);

}
