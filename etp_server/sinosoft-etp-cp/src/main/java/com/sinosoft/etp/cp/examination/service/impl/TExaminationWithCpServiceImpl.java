package com.sinosoft.etp.cp.examination.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.constant.ETPConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.bean.BeanUtils;
import com.sinosoft.etp.cp.common.service.CommonServiceWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationInfoWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationStudentWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.examination.mapper.TExaminationStudentWithCpMapper;
import com.sinosoft.etp.cp.examination.mapper.TExaminationWithCpMapper;
import com.sinosoft.etp.cp.examination.service.ITExaminationStudentWithCpService;
import com.sinosoft.etp.cp.examination.service.ITExaminationWithCpService;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考试Service业务层处理
 *
 *
 * @date 2021-12-24
 */
@Service
public class TExaminationWithCpServiceImpl implements ITExaminationWithCpService
{
    @Autowired
    private TExaminationWithCpMapper tExaminationMapper;
    @Autowired
    private ITTagWithCpService iTTagService;


    /** @type update 2021-12-31 考试限定用户相关 */
    @Resource
    private TExaminationStudentWithCpMapper tExaminationStudentMapper;
    @Resource
    private ITExaminationStudentWithCpService itExaminationStudentService;
    @Resource
    private CommonServiceWithCp commonService;
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    @Override
    public TExaminationWithCp selectTExaminationById(Long id)
    {
        TExaminationWithCp tExamination = tExaminationMapper.selectTExaminationById(id);
        /** @type update 2021-12-31 查询用户 */
        tExamination.setSysUsers(tExaminationStudentMapper.selectByKsId(id));
        return tExamination;
    }

    /**
     * 查询缺考人员
     *
     * @param id 考试ID
     * @return 考试
     */
    @Override
    public TExaminationWithCp selectMissExamById(Long id)
    {
        TExaminationWithCp tExamination = tExaminationMapper.selectTExaminationById(id);
        /** @type update 2021-12-31 查询缺考用户 */
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        tExamination.setSysUsers(tExaminationStudentMapper.selectMissExamByKsId(id,deptId));
        return tExamination;
    }
    @Override
    public TExaminationWithCp selectTExaminationInfo(Long id,String pjzt){
        TExaminationWithCp tExamination  = tExaminationMapper.selectTExaminationById(id);
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();

        List<TExaminationInfoWithCp> examinationInfoWithCps = tExaminationStudentMapper.selectInfo(id,pjzt);

        for (TExaminationInfoWithCp t:examinationInfoWithCps) {
            JSONObject jsonObject = JSON.parseObject(t.getDa());
            JSONObject da = (JSONObject) jsonObject.get("detail");
            Double score = Double.valueOf(da.get("score").toString());
            t.setScore(score);
            DecimalFormat df = new DecimalFormat("#.00");
            t.setRate(t.getScore()<0?0.0:Double.valueOf(df.format(t.getScore()/tExamination.getZfs())));
        }
        tExamination.setExaminationInfoWithCps(examinationInfoWithCps);
        return tExamination;
    }

    /**
     * 考试成绩导出
     * @param id
     * @return
     */
    @Override
    public List<TExaminationInfoWithCp> selectTExaminationList(Long id){
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        List<TExaminationInfoWithCp> examinationInfoWithCps = tExaminationStudentMapper.selectExamInfo(id,deptId);
        DecimalFormat df = new DecimalFormat("#0.00");
        for (TExaminationInfoWithCp tExaminationInfoWithCp:examinationInfoWithCps) {
            tExaminationInfoWithCp.setStartTime(tExaminationInfoWithCp.getCreateTime());
            Long startTime = tExaminationInfoWithCp.getCreateTime().getTime();
            Long endTime = tExaminationInfoWithCp.getHandTime().getTime();
            String kssc = String.valueOf((Math.ceil((double)(endTime-startTime)/1000)/60));
            tExaminationInfoWithCp.setKssc(df.format(Double.parseDouble(kssc)));
            tExaminationInfoWithCp.setPjzt("1".equals(tExaminationInfoWithCp.getPjzt())?"已判卷":"未判卷");
            tExaminationInfoWithCp.setHandType(tExaminationInfoWithCp.getHandType()=="1"?"人工交卷":"强制交卷");
        }
        return examinationInfoWithCps;
    }

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TExaminationWithCp> selectTExaminationList(TExaminationWithCp tExamination)
    {
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        List<TExaminationWithCp> list = tExaminationMapper.selectTExaminationList(tExamination);
        for (int i = 0; i <list.size(); i++) {
            Long id = list.get(i).getId();
            List<TExaminationInfoWithCp> info =  tExaminationStudentMapper.selectInfo(id,"1");
            Integer sum = tExaminationStudentMapper.selectKsCount(id);
            String infoNum = String.valueOf(info.size());
            list.get(i).setKszt(infoNum+"/"+(sum==null?0:sum.toString()));
        }
        return list;
    }

    /**
     * 新增考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertTExamination(TExaminationWithCp tExamination)
    {
        int res = 0;
        Date date= DateUtils.getNowDate();
        String userName = SecurityUtils.getUsername();
        tExamination.setCreateTime(date);
        tExamination.setCreateBy(userName);
        tExamination.setUpdateTime(date);
        tExamination.setUpdateBy(userName);
        res = tExaminationMapper.insertTExamination(tExamination);

        //插入考试企业关联表
        //TExaminationCompanyWithCp tExaminationCompany = new TExaminationCompanyWithCp();
        //tExaminationCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tExaminationCompany.setKsid(tExamination.getId());
        //tExaminationCompany.setCreateTime(date);
        //tExaminationCompany.setCreateBy(userName);
        //tExaminationCompany.setUpdateTime(date);
        //tExaminationCompanyMapper.insertTExaminationCompany(tExaminationCompany);

        //插入标签表
        if(null != tExamination.getParams().get("tagStr")){
            //企业插入标签
            iTTagService.commpanyInsertTag(tExamination.getParams().get("tagStr").toString());
            //插入考试标签关联表
            iTTagService.insertRelateTag(tExamination.getId(),tExamination.getParams().get("tagStr").toString(),"KS");
        };

        /** @type update 2021-12-31 插入考试限定用户 */
        // 插入课程与学员关联关系 1表示指定用户
        if (ETPConstants.OPENNESS_POINTER.equals(tExamination.getKfcd())){
            itExaminationStudentService.insert(tExamination.getSysUsers(),tExamination.getId());
        }
        /*if (ETPConstants.OPENNESS_FULLY.equals(tExamination.getKfcd())){
            List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentCompanyUsers,tExamination);
        }*/

        return tExamination.getId();
    }

    /**
     * 修改考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTExamination(TExaminationWithCp tExamination)
    {
        int res = 0;
        //删除关联关系
        iTTagService.deleteRelateTag(tExamination.getId(),"KS");

        //插入标签表
        if(null != tExamination.getParams().get("tagStr")){
            iTTagService.commpanyInsertTag(tExamination.getParams().get("tagStr").toString());
            //插入题库标签关联表
            iTTagService.insertRelateTag(tExamination.getId(),tExamination.getParams().get("tagStr").toString(),"KS");
        };
        tExamination.setUpdateTime(DateUtils.getNowDate());
        tExamination.setUpdateBy(SecurityUtils.getUsername());
        res = tExaminationMapper.updateTExamination(tExamination);

        /** @type update 2021-12-31 插入考试限定用户 */
        /*if (ETPConstants.OPENNESS_FULLY.equals(tExamination.getKfcd())){
            List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentCompanyUsers,tExamination);
        }else {

        }*/
        this.delExaminationPointStudents(tExamination.getSysUsers(),tExamination.getId());
        return res;
    }

    /**
     * 修改发布状态
     * @param tExamination
     * @return
     */
    @Override
    public int updateTExaminationFb(TExaminationWithCp tExamination){
        return tExaminationMapper.updateTExaminationFb(tExamination);
    }


    /**
     * 将 SysUser 转变成 TExaminationStudent,并插入关联表
     */
    @Transactional
    protected void transformObjectType(List<SysUser> sysUsers, TExaminationWithCp tExamination){
        List<TExaminationStudentWithCp> list = tExaminationStudentMapper.selectByKsId(tExamination.getId());
        tExaminationStudentMapper.delRealtionByKsId(tExamination.getId());
        if (ETPConstants.OPENNESS_FULLY.equals(tExamination.getKfcd())){ // 指定全部用户
            if (sysUsers.size() > 0){

                List<TExaminationStudentWithCp> collect = sysUsers.stream().map(sysUser -> {
                    TExaminationStudentWithCp student = new TExaminationStudentWithCp();
                    student.setKsid(tExamination.getId());
                    student.setXyid(sysUser.getUserId());
                    BeanUtils.copyBeanProp(student, sysUser);
                    return student;
                }).collect(Collectors.toList());

                collect.forEach(item -> {
                    list.forEach( i -> {
                        System.out.println(i);
                        if (i.getXyid().equals(item.getXyid()) && i.getKsid().equals(item.getKsid())){
                            item.setJd(i.getJd());
                        }
                    });
                });
                tExaminationStudentMapper.insert(collect);
            }
        }
    }

    /**
     * @type create
     * 2021-12-31
     * 处理考试指定用户更新操作
     *   1. 先删除（逻辑删除）
     *   2. 保存进度
     *   3. 在插入
     * @param users 前端新提交的对应的指定学员
     * @param ksid  考试ID
     */
    @Transactional
    protected void delExaminationPointStudents(List<TExaminationStudentWithCp> users, Long ksid){
        List<TExaminationStudentWithCp> list = tExaminationStudentMapper.selectByKsId(ksid);
        tExaminationStudentMapper.delRealtionByKsId(ksid);
        if (null != users && users.size() > 0){
            List<TExaminationStudentWithCp> collect = users.stream().map(tCurriculumStudent -> {
                tCurriculumStudent.setCreateBy(SecurityUtils.getUsername());
                tCurriculumStudent.setUpdateBy(SecurityUtils.getUsername());
                tCurriculumStudent.setKsid(ksid);
                tCurriculumStudent.setXyid(tCurriculumStudent.getUserId());
                return tCurriculumStudent;
            }).collect(Collectors.toList());
            collect.forEach(item -> {
                list.forEach(i -> {
                    if (i.getUserId().equals(item.getXyid()) && i.getKsid().equals(item.getKsid())){
                        item.setJd(i.getJd());
                    }
                });
            });
            tExaminationStudentMapper.insert(collect);
        }
    }

    /**
     * 批量删除考试
     *
     * @param ids 需要删除的考试ID
     * @return 结果
     */
    @Override
    public int deleteTExaminationByIds(Long[] ids)
    {
        return tExaminationMapper.deleteTExaminationByIds(ids);
    }

    /**
     * 删除考试信息
     *
     * @param id 考试ID
     * @return 结果
     */
    @Override
    public int deleteTExaminationById(Long id)
    {
        return tExaminationMapper.deleteTExaminationById(id);
    }

    @Override
    public List<Long> checkExaminations(Long[] ids) {
        return tExaminationMapper.checkExaminations(ids);
    }

    @Override
    public boolean checkExistKS(TExaminationWithCp tExamination) {
        Long id = StringUtils.isNull(tExamination.getId()) ? -1L : tExamination.getId();
        TExaminationWithCp result = tExaminationMapper.checkExistKS(tExamination.getMc());
        if(StringUtils.isNotNull(result) && result.getId().longValue()!=id.longValue() ){
            return true;
        }
        return false;
    }
}
