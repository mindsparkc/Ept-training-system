package com.sinosoft.etp.cp.sign.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.constant.ETPConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.bean.BeanUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.etp.cp.common.service.CommonServiceWithCp;
import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCp;
import com.sinosoft.etp.cp.sign.domain.TSignWithCp;
import com.sinosoft.etp.cp.sign.mapper.TSignCompanyWithCpMapper;
import com.sinosoft.etp.cp.sign.mapper.TSignStudentWithCpMapper;
import com.sinosoft.etp.cp.sign.mapper.TSignWithCpMapper;
import com.sinosoft.etp.cp.sign.service.ITSignStudentWithCpService;
import com.sinosoft.etp.cp.sign.service.ITSignWithCpService;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * signService业务层处理
 *
 *
 * @date 2022-02-09
 */
@Service
public class TSignWithCpServiceImpl implements ITSignWithCpService
{
    @Resource
    private TSignWithCpMapper tSignMapper;

    @Resource
    private TSignCompanyWithCpMapper tSignCompanyMapper;

    @Resource
    private ITTagWithCpService iTTagService;

    @Resource
    private ITSignStudentWithCpService itSignStudentService;

    @Resource
    private CommonServiceWithCp commonService;
    @Resource
    private TSignStudentWithCpMapper tSignStudentMapper;
    /**
     * 查询sign
     *
     * @param id signID
     * @return sign
     */
    @Override
    public TSignWithCp selectTSignById(Long id)
    {
        TSignWithCp tSign = tSignMapper.selectTSignById(id);
        tSign.setSysUsers(tSignStudentMapper.selectBysId(id));
        return tSign;
    }

    /**
     * 查询sign列表
     *
     * @param tSign sign
     * @return sign
     */
    @Override
    @DataScope(deptAlias = "d") //bug 部门表关联已删除
    @EtpDataScope(alias = "ts")
    public List<TSignWithCp> selectTSignList(TSignWithCp tSign)
    {
        //更改活动状态
        checkSignStatus(tSign);
        return tSignMapper.selectTSignList(tSign);
    }

    /**
     * 活动表全表扫描-数量级本企业
     */
    @DataScope(deptAlias = "d")
    private void checkSignStatus(TSignWithCp tSign){
        List<TSignWithCp> tSigns1 = tSignMapper.selectTSignList(tSign);
        Date nowDate = DateUtils.getNowDate();
        tSigns1.forEach(item ->{
            Date startDate = item.getKssj();
            Date endDate = item.getJssj();
            String status = DateUtils.calculateDate(startDate, endDate, nowDate);
            item.setStatus(status);
            tSignMapper.updateSignStatus(item);
        });
    }

    /**
     * 新增sign
     *
     * @param tSign sign
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTSign(TSignWithCp tSign)
    {
        EtpDataScopeUtils.setDataScope(tSign);// 20250520 lyd etp数据权限相关字段

        tSign.setCreateTime(DateUtils.getNowDate());
        tSign.setUpdateTime(DateUtils.getNowDate());
        tSign.setCreateBy(SecurityUtils.getUsername());
        tSign.setUpdateBy(SecurityUtils.getUsername());
        int res = tSignMapper.insertTSign(tSign);
        //操作 t_sign_company关联表
        //TSignCompanyWithCp tSignCompany = new TSignCompanyWithCp();
        //tSignCompany.setSignId(tSign.getId());
        //tSignCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //res += tSignCompanyMapper.insertTSignCompany(tSignCompany);

        //插入活动标签关联表
        if (tSign.getParams().get("tagStr") != null){
            String tagStr = tSign.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tSign.getId(),tagStr,"HD");
        }
        // 指定用户
        if (ETPConstants.OPENNESS_POINTER.equals(tSign.getKfcd())){
            itSignStudentService.insert(tSign.getSysUsers(),tSign.getId());
        }
        /*if (ETPConstants.OPENNESS_FULLY.equals(tSign.getKfcd())){
            List<SysUser> currentUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentUsers,tSign);
        }*/

        return res;
    }


    /**
     * 将 SysUser 转变成 TSignStudent,并插入关联表
     */
    @Transactional
    protected void transformObjectType(List<SysUser> sysUsers, TSignWithCp tSign){
        List<TSignStudentWithCp> list = tSignStudentMapper.selectBysId(tSign.getId());
        tSignStudentMapper.delRealtionBysId(tSign.getId());
        if (ETPConstants.OPENNESS_FULLY.equals(tSign.getKfcd())){ // 指定全部用户
            if (sysUsers.size() > 0){

                List<TSignStudentWithCp> collect = sysUsers.stream().map(sysUser -> {
                    TSignStudentWithCp student = new TSignStudentWithCp();
                    student.setHdid(tSign.getId());
                    student.setXyid(sysUser.getUserId());
                    BeanUtils.copyBeanProp(student, sysUser);
                    return student;
                }).collect(Collectors.toList());

                collect.forEach(item -> {
                    list.forEach( i -> {
                        System.out.println(i);
                        if (i.getXyid().equals(item.getXyid()) && i.getHdid().equals(item.getHdid())){
                            item.setSfbm(i.getSfbm());
                        }
                    });
                });
                tSignStudentMapper.insert(collect);
            }
        }
    }

    /**
     * 修改sign
     *
     * @param tSign sign
     * @return 结果
     */
    @Override
    public int updateTSign(TSignWithCp tSign)
    {
        tSign.setUpdateTime(DateUtils.getNowDate());
        tSign.setUpdateBy(SecurityUtils.getUsername());
        //删除标签关联关系
        iTTagService.deleteRelateTag(tSign.getId(),"HD");
        int res = tSignMapper.updateTSign(tSign);
        //插入活动标签关联表
        if(null != tSign.getParams().get("tagStr")){
            String tagStr = tSign.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tSign.getId(),tagStr,"HD");
        }

        /*if (ETPConstants.OPENNESS_FULLY.equals(tSign.getKfcd())){
            List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentCompanyUsers,tSign);
        }else {

        }*/
        this.delExaminationPointStudents(tSign.getSysUsers(),tSign.getId());

        return res;
    }

    /**
     * @type create
     * 2021-12-31
     * 处理活动报名指定用户更新操作
     *   1. 先删除（逻辑删除）
     *   2. 保存进度
     *   3. 在插入
     * @param users 前端新提交的对应的指定学员
     * @param sid  活动
     */
    @Transactional
    protected void delExaminationPointStudents(List<TSignStudentWithCp> users, Long sid){
        List<TSignStudentWithCp> list = tSignStudentMapper.selectBysId(sid);
        tSignStudentMapper.delRealtionBysId(sid);
        if (null != users && users.size() > 0){
            List<TSignStudentWithCp> collect = users.stream().map(tSignStudent -> {
                tSignStudent.setCreateBy(SecurityUtils.getUsername());
                tSignStudent.setUpdateBy(SecurityUtils.getUsername());
                tSignStudent.setHdid(sid);
                tSignStudent.setXyid(tSignStudent.getUserId());
                return tSignStudent;
            }).collect(Collectors.toList());
            collect.forEach(item -> {
                list.forEach(i -> {
                    if (i.getUserId().equals(item.getXyid()) && i.getHdid().equals(item.getHdid())){
                        item.setSfbm(i.getSfbm());
                    }
                });
            });
            tSignStudentMapper.insert(collect);
        }
    }

    /**
     * 批量删除sign
     *
     * @param ids 需要删除的signID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTSignByIds(Long[] ids)
    {
        // tSignCompanyMapper.deleteTSignCompanyByIds(ids);
        return tSignMapper.deleteTSignByIds(ids);
    }

    /**
     * 删除sign信息
     *
     * @param id signID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTSignById(Long id)
    {
        // tSignCompanyMapper.deleteTSignCompanyById(id);
        return tSignMapper.deleteTSignById(id);
    }
}
