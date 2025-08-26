package com.sinosoft.etp.up.sign.service;

import com.sinosoft.etp.up.sign.domain.TSignStudent;

public interface ITSignStudentService {

    /**
     * 查询学员活动报名
     *
     * @param hdid 学员活动报名hdid
     * @return 学员活动报名
     */
    public TSignStudent selectTSignStudentById(Long hdid);

    /**
     * 新增学员活动报名
     *
     * @param tSignStudent 学员活动报名
     * @return 结果
     */
    public int insertTSignStudent(TSignStudent tSignStudent);
}
