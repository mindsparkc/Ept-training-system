package com.sinosoft.etp.up.sign.mapper;

import com.sinosoft.etp.up.sign.domain.TSignStudent;
import org.apache.ibatis.annotations.Param;

public interface TSignStudentMapper {

    /**
     * 查询学员活动报名
     *
     * @param hdid
     * @param xyid
     * @return 学员活动报名
     */
    public TSignStudent selectTSignStudentById(@Param("hdid") Long hdid,@Param("xyid") Long xyid);

    /**
     * 新增学员活动报名
     *
     * @param tSignStudent 学员活动报名
     * @return 结果
     */
    public int insertTSignStudent(TSignStudent tSignStudent);


}
