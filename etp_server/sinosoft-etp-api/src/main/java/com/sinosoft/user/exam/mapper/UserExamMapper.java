package com.sinosoft.user.exam.mapper;

import com.sinosoft.user.exam.domain.UserSimpleExam;
import com.sinosoft.user.exam.domain.UserExamReqQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserExamMapper {
    /**
     * 查询考试列表
     * 待新增权限控制，仅查询自己有权 限查看的考试
     *
     * @param examQuery
     * @return
     */
    public List<UserSimpleExam> selectUserExamList(UserExamReqQuery examQuery);

    /**
     * 查询考试详情
     * 待新增权限控制，仅查询自己有权限查看的考试
     *
     * @param examQuery
     * @return
     */
    public UserSimpleExam selectUserExamInfo(UserExamReqQuery examQuery);

    /**
     * 查询即将结束的考试
     * 待新增权限控制，仅查询自己有权限查看的考试
     *
     * @param examQuery
     * @return
     */
    public UserSimpleExam selectComingEndExam(UserExamReqQuery examQuery);

    /**
     * 统计即将结束的考试数量和即将开始的考试数量
     *
     * @param type   查询考试的类型：将开始和将结束
     * @param userId 用户id
     * @param deptId 用户部门
     * @param postId 用户分组
     * @return
     */
    public int selectCountNum(@Param("type") String type
            , @Param("userId") Long userId
            , @Param("deptId") Long deptId, @Param("postIds") Long[] postIds
            , @Param("etpDataScope") String etpDataScope);
}
