package com.sinosoft.etp.up.courseware.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.courseware.domain.TCoursewareStudent;
import com.sinosoft.etp.up.courseware.mapper.TCoursewareStudentMapper;
import com.sinosoft.etp.up.courseware.service.ITCoursewareStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 【请填写功能名称】Service业务层处理
 *
 *
 * @date 2022-03-31
 */
@Service
public class TCoursewareStudentServiceImpl implements ITCoursewareStudentService
{
    @Autowired
    private TCoursewareStudentMapper tCoursewareStudentMapper;

    /**
     * 查询
     *
     * @param kjid
     * @return
     */
    @Override
    public TCoursewareStudent selectTCoursewareStudentById(Long kjid)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        return tCoursewareStudentMapper.selectTCoursewareStudentById(kjid,xyid);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tCoursewareStudent 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TCoursewareStudent> selectTCoursewareStudentList(TCoursewareStudent tCoursewareStudent)
    {
        return tCoursewareStudentMapper.selectTCoursewareStudentList(tCoursewareStudent);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param tCoursewareStudent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTCoursewareStudent(TCoursewareStudent tCoursewareStudent)
    {
        tCoursewareStudent.setCreateTime(DateUtils.getNowDate());
        return tCoursewareStudentMapper.insertTCoursewareStudent(tCoursewareStudent);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param tCoursewareStudent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTCoursewareStudent(TCoursewareStudent tCoursewareStudent)
    {
        tCoursewareStudent.setUpdateTime(DateUtils.getNowDate());
        return tCoursewareStudentMapper.updateTCoursewareStudent(tCoursewareStudent);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTCoursewareStudentByIds(Long[] ids)
    {
        return tCoursewareStudentMapper.deleteTCoursewareStudentByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTCoursewareStudentById(Long id)
    {
        return tCoursewareStudentMapper.deleteTCoursewareStudentById(id);
    }
}
