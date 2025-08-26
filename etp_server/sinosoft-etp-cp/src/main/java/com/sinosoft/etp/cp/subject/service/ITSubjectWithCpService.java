package com.sinosoft.etp.cp.subject.service;

import com.sinosoft.etp.cp.subject.domain.TSubjectImportResponse;
import com.sinosoft.etp.cp.subject.domain.TSubjectWithCp;

import java.io.IOException;
import java.util.List;

/**
 * 题库Service接口
 *
 * 
 * @date 2021-11-08
 */
public interface ITSubjectWithCpService
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
     * 批量删除题库
     *
     * @param ids 需要删除的题库ID
     * @return 结果
     */
    public int deleteTSubjectByIds(Long[] ids);

    /**
     * 删除题库信息
     *
     * @param id 题库ID
     * @return 结果
     */
    public int deleteTSubjectById(Long id);

    /**
     * 随机选择试题
     * @param ids
     * @param num
     * @return
     */
    public List<TSubjectWithCp> getSubjectRand(String ids, int num);

    public TSubjectImportResponse batchSubjects(String filePath) throws IOException;

    /**
     * 获取课程中选中的试题
     * @param kcid
     * @return
     */
    public List<TSubjectWithCp> getSelectedStByKc(Long kcid);
}
