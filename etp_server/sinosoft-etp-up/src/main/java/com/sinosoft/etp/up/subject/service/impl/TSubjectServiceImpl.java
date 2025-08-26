package com.sinosoft.etp.up.subject.service.impl;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.etp.up.subject.domain.TSubject;
import com.sinosoft.etp.up.subject.mapper.TSubjectMapper;
import com.sinosoft.etp.up.subject.service.ITSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 题库Service业务层处理
 *
 *
 * @date 2021-11-08
 */
@Service
public class TSubjectServiceImpl implements ITSubjectService
{
    @Autowired
    private TSubjectMapper tSubjectMapper;

    /**
     * 查询题库
     *
     * @param id 题库ID
     * @return 题库
     */
    @Override
    public TSubject selectTSubjectById(Long id)
    {
        return tSubjectMapper.selectTSubjectById(id);
    }

    /**
     * 查询题库列表
     *
     * @param tSubject 题库
     * @return 题库
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TSubject> selectTSubjectList(TSubject tSubject)
    {
        return tSubjectMapper.selectTSubjectList(tSubject);
    }

}
