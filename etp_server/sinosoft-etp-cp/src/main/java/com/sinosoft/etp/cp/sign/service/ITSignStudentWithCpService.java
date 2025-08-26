package com.sinosoft.etp.cp.sign.service;

import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCp;
import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCpExport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITSignStudentWithCpService {
    /**
     * 批量插入活动报名与学员记录表记录
     * @param sysUsers  学员
     * @param sid      活动id
     * @return Long
     */
    Long insert(List<TSignStudentWithCp> sysUsers, Long sid);

    /**
     * 根据活动id获取已报名的学员信息
     * @param sid
     * @return
     */
    List<TSignStudentWithCpExport> selectBysId(@Param("sid") Long sid);

}
