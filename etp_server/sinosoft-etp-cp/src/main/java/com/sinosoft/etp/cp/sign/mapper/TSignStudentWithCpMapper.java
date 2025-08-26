package com.sinosoft.etp.cp.sign.mapper;

import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TSignStudentWithCpMapper {

    /**
     * 插入活动与学员关联关系
     * @param sysUsers 学员
     */
    Long insert(@Param("list") List<TSignStudentWithCp> sysUsers);

    /**
     * 根据活动ID查询用户
     * @param sid 活动ID
     * @return  List<TSignStudent>
     */
    List<TSignStudentWithCp> selectBysId(@Param("sid") Long sid);


    /**
     * 根据活动ID删除活动与学员记录表记录
     * @param sid 活动ID
     */
    void delRealtionBysId(@Param("sid") Long sid);
}
