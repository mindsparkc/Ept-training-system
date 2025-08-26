package com.sinosoft.etp.up.subject.mapper;

import com.sinosoft.etp.up.subject.domain.TSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 题库Mapper接口
 *
 *
 * @date 2021-11-08
 */
public interface TSubjectMapper
{
    /**
     * 查询题库
     *
     * @param id 题库ID
     * @return 题库
     */
    public TSubject selectTSubjectById(Long id);

    /**
     * 查询题库列表
     *
     * @param tSubject 题库
     * @return 题库集合
     */
    public List<TSubject> selectTSubjectList(TSubject tSubject);

    /**
     * 新增题库
     *
     * @param tSubject 题库
     * @return 结果
     */
    public int insertTSubject(TSubject tSubject);

    /**
     * 修改题库
     *
     * @param tSubject 题库
     * @return 结果
     */
    public int updateTSubject(TSubject tSubject);

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

    public List<TSubject> getSubjectRand(@Param("ids") String ids, @Param("num")int num);

}
