package com.sinosoft.etp.cp.courseware.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.courseware.domain.TCoursewareCompanyWithCp;
import com.sinosoft.etp.cp.courseware.mapper.TCoursewareCompanyWithCpMapper;
import com.sinosoft.etp.cp.courseware.service.ITCoursewareCompanyServiceWithCp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课件企业关联Service业务层处理
 *
 *
 * @date 2021-11-12
 */
@Service
public class TCoursewareCompanyServiceWithCpImpl implements ITCoursewareCompanyServiceWithCp
{
    @Autowired
    private TCoursewareCompanyWithCpMapper tCoursewareCompanyMapper;

    /**
     * 查询课件企业关联
     *
     * @param id 课件企业关联ID
     * @return 课件企业关联
     */
    @Override
    public TCoursewareCompanyWithCp selectTCoursewareCompanyById(Long id)
    {
        return tCoursewareCompanyMapper.selectTCoursewareCompanyById(id);
    }

    /**
     * 查询课件企业关联列表
     *
     * @param tCoursewareCompany 课件企业关联
     * @return 课件企业关联
     */
    @Override
    public List<TCoursewareCompanyWithCp> selectTCoursewareCompanyList(TCoursewareCompanyWithCp tCoursewareCompany)
    {
        return tCoursewareCompanyMapper.selectTCoursewareCompanyList(tCoursewareCompany);
    }

    /**
     * 新增课件企业关联
     *
     * @param tCoursewareCompany 课件企业关联
     * @return 结果
     */
    @Override
    public int insertTCoursewareCompany(TCoursewareCompanyWithCp tCoursewareCompany)
    {
        tCoursewareCompany.setCreateTime(DateUtils.getNowDate());
        return tCoursewareCompanyMapper.insertTCoursewareCompany(tCoursewareCompany);
    }

    /**
     * 修改课件企业关联
     *
     * @param tCoursewareCompany 课件企业关联
     * @return 结果
     */
    @Override
    public int updateTCoursewareCompany(TCoursewareCompanyWithCp tCoursewareCompany)
    {
        tCoursewareCompany.setUpdateTime(DateUtils.getNowDate());
        return tCoursewareCompanyMapper.updateTCoursewareCompany(tCoursewareCompany);
    }

    /**
     * 批量删除课件企业关联
     *
     * @param ids 需要删除的课件企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTCoursewareCompanyByIds(Long[] ids)
    {
        return tCoursewareCompanyMapper.deleteTCoursewareCompanyByIds(ids);
    }

    /**
     * 删除课件企业关联信息
     *
     * @param id 课件企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTCoursewareCompanyById(Long id)
    {
        return tCoursewareCompanyMapper.deleteTCoursewareCompanyById(id);
    }
}
