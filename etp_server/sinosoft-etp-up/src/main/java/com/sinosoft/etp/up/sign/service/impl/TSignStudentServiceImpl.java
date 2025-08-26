package com.sinosoft.etp.up.sign.service.impl;

import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.sign.domain.TSignStudent;
import com.sinosoft.etp.up.sign.mapper.TSignStudentMapper;
import com.sinosoft.etp.up.sign.service.ITSignStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TSignStudentServiceImpl implements ITSignStudentService {

    @Resource
    private TSignStudentMapper tSignStudentMapper;


    /**
     * 查询学员活动报名
     *
     * @param hdid
     * @return 学员活动报名
     */
    @Override
    public TSignStudent selectTSignStudentById(Long hdid)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        return tSignStudentMapper.selectTSignStudentById(hdid,xyid);
    }

    /**
     * 新增学员活动报名
     *
     * @param tSignStudent 学员活动报名
     * @return 结果
     */
    @Override
    public int insertTSignStudent(TSignStudent tSignStudent)
    {
        int rows = tSignStudentMapper.insertTSignStudent(tSignStudent);
        return rows;
    }
}
