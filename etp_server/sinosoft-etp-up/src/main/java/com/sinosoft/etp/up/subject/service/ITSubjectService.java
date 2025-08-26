package com.sinosoft.etp.up.subject.service;

import com.sinosoft.etp.up.subject.domain.TSubject;

import java.util.List;

/**
 * 题库Service接口
 *
 *
 * @date 2021-11-08
 */
public interface ITSubjectService
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


}
