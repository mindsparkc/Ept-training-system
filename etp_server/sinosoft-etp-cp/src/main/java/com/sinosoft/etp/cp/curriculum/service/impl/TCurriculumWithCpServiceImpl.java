package com.sinosoft.etp.cp.curriculum.service.impl;


import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.annotation.VipCheck;
import com.sinosoft.common.constant.ETPConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.bean.BeanUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.etp.cp.common.service.CommonServiceWithCp;
import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumContentWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumStudentWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumCompanyWithCpMapper;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumContentWithCpMapper;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumStudentWithCpMapper;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumWithCpMapper;
import com.sinosoft.etp.cp.curriculum.service.ITCurriculumStudentWithCpService;
import com.sinosoft.etp.cp.curriculum.service.ITCurriculumWithCpService;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * curriculumService业务层处理
 *
 *
 * @date 2021-11-15
 */
@Service
public class TCurriculumWithCpServiceImpl implements ITCurriculumWithCpService {
    @Resource
    private TCurriculumWithCpMapper tCurriculumMapper;

    @Resource
    private TCurriculumCompanyWithCpMapper tCurriculumCompanyMapper;

    @Resource
    private ITTagWithCpService iTTagService;

    @Resource
    private TCurriculumContentWithCpMapper tCurriculumContentMapper;

    @Resource
    private ITCurriculumStudentWithCpService itCurriculumStudentService;

    @Resource
    private TCurriculumStudentWithCpMapper tCurriculumStudentMapper;

    @Resource
    private CommonServiceWithCp commonService;

    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.CURRICULUM_CATEGORY;

    @Resource
    private IObjectInterCategoryService objectInterCategoryService;

    @Resource
    private ICategoryService categoryService;
    /**
     * 查询curriculum
     * @param id curriculumID
     * @return curriculum
     */
    @Override
    public TCurriculumWithCp selectTCurriculumById(Long id) {
        TCurriculumWithCp tCurriculum = tCurriculumMapper.selectTCurriculumById(id);
        List<TCurriculumContentWithCp> curriculumContents = tCurriculumContentMapper.selectTurriculumContentByCid(tCurriculum.getId());
        tCurriculum.settCurriculumContents(curriculumContents);
        tCurriculum.setSysUsers(tCurriculumStudentMapper.selectByKcId(id));
        // 补充分类信息
        List<ObjectInterCategory> interCategories = objectInterCategoryService.selectObjectInterCategory(categoryEnum, Arrays.asList(id));
        List<List<Long>> categories = new ArrayList<>();
        for (ObjectInterCategory interCategory : interCategories) {
            String[] split = interCategory.getAncestors().split(",");
            categories.add(Arrays.stream(split).filter(s->{
                if (s.equals("0")) {
                    return false;
                }
                return true;
            }).map(s -> Long.valueOf(s)).collect(Collectors.toList()));
        }
        tCurriculum.setCategoryIds(categories);
        return tCurriculum;
    }

    /**
     * 查询curriculum列表
     * @param tCurriculum curriculum
     * @return curriculum
     */
    @DataScope(deptAlias = "d") //错误用法 别名是t
    @EtpDataScope(alias = "t")
    @Override
    public List<TCurriculumWithCp> selectTCurriculumList(TCurriculumWithCp tCurriculum) {
        return tCurriculumMapper.selectTCurriculumList(tCurriculum);
    }

    /**
     * 新增curriculum
     * @param tCurriculum curriculum
     * @return 结果
     */
    @VipCheck(resource="curriculum")
    @Transactional
    @Override
    public Long insertTCurriculum(TCurriculumWithCp tCurriculum) {
        // 修改
        if (tCurriculum.getId() != null)
        {
            tCurriculum.setUpdateTime(DateUtils.getNowDate());
            tCurriculum.setStates("0");
            this.updateTCurriculum(tCurriculum);
        }
        else // 新增课程
        {
            EtpDataScopeUtils.setDataScope(tCurriculum);// 20250520 lyd etp数据权限相关字段

            tCurriculum.setCreateTime(DateUtils.getNowDate());
            tCurriculum.setUpdateTime(DateUtils.getNowDate());
            tCurriculum.setCreateBy(SecurityUtils.getUsername());
            tCurriculum.setStates("0");
            tCurriculumMapper.insertTCurriculum(tCurriculum);

            // 课程企业关联
            //TCurriculumCompanyWithCp curriculumCompany = new TCurriculumCompanyWithCp();
            //curriculumCompany.setCid(tCurriculum.getId());
            //curriculumCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
            //tCurriculumCompanyMapper.insertTCurriculumCompany(curriculumCompany);

            //插入标签表插入标签关联表
            if (tCurriculum.getParams().get("tagStr") != null) {
                String tagStr = tCurriculum.getParams().get("tagStr").toString();
                iTTagService.commpanyInsertTag(tagStr);
                iTTagService.insertRelateTag(tCurriculum.getId(),tagStr,"KC");
            }

            // 插入关联分类
            if (tCurriculum.hasCategoryIds()) {
                List<ObjectInterCategory> categories = getCurriculmInterCategory(tCurriculum, tCurriculum);
                objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories);
            }
            // kfcd = 0 代表完全公开 将该企业下所有用户插入表中
            /*if (ETPConstants.OPENNESS_FULLY.equals(tCurriculum.getKfcd())){
                List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
                transformObjectType(currentCompanyUsers,tCurriculum);
            }*/
            // 插入课程与学员关联关系 1表示指定用户
            if (ETPConstants.OPENNESS_POINTER.equals(tCurriculum.getKfcd())){
                itCurriculumStudentService.insert(tCurriculum.getSysUsers(),tCurriculum.getId());
            }
        }
        return tCurriculum.getId();
    }

    /**
     * 修改curriculum
     * @param tCurriculum curriculum
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTCurriculum(TCurriculumWithCp tCurriculum) {

        //删除关联关系
        iTTagService.deleteRelateTag(tCurriculum.getId(),"KC");
        tCurriculum.setUpdateTime(DateUtils.getNowDate());
        int res = tCurriculumMapper.updateTCurriculum(tCurriculum);

        //插入课程标签关联表
        if(null != tCurriculum.getParams().get("tagStr")){
            iTTagService.commpanyInsertTag(tCurriculum.getParams().get("tagStr").toString());
            iTTagService.insertRelateTag(tCurriculum.getId(),tCurriculum.getParams().get("tagStr").toString(),"KC");
        }


        this.delCurriculumPointStudents(tCurriculum.getSysUsers(),tCurriculum.getId());
        // 删除关联分类 && 插入关联分类
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(tCurriculum.getId()));
        if (tCurriculum.hasCategoryIds()) {
            List<ObjectInterCategory> interCategory = getCurriculmInterCategory(tCurriculum, tCurriculum);
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, interCategory);
        }

       /* if (ETPConstants.OPENNESS_FULLY.equals(tCurriculum.getKfcd())){
            List<SysUser> currentCompanyUsers = commonService.getCurrentCompanyUsers(SecurityUtils.getLoginUser().getUser());
            transformObjectType(currentCompanyUsers,tCurriculum);
        }*/
        // 插入课程与学员关联关系 1表示指定用户
        if (ETPConstants.OPENNESS_POINTER.equals(tCurriculum.getKfcd())){
            itCurriculumStudentService.insert(tCurriculum.getSysUsers(),tCurriculum.getId());
        }
        return res;
    }

    @Override
    public int updateTCurriculumTjzt(TCurriculumWithCp tCurriculum) {
        return tCurriculumMapper.updateTCurriculumTjzt(tCurriculum);
    }


    /**
     * 将 SysUser 转变成 TCurriculumStudent,并插入关联表
     */
    @Transactional
    protected void transformObjectType(List<SysUser> sysUsers, TCurriculumWithCp tCurriculum){
        List<TCurriculumStudentWithCp> list = tCurriculumStudentMapper.selectByKcId(tCurriculum.getId());
        tCurriculumStudentMapper.delRealtionByKcId(tCurriculum.getId());
        if (ETPConstants.OPENNESS_FULLY.equals(tCurriculum.getKfcd())){ // 指定全部用户
            if (sysUsers.size() > 0){

                List<TCurriculumStudentWithCp> collect = sysUsers.stream().map(sysUser -> {
                    TCurriculumStudentWithCp student = new TCurriculumStudentWithCp();
                    student.setKcid(tCurriculum.getId());
                    student.setXyid(sysUser.getUserId());
                    BeanUtils.copyBeanProp(student, sysUser);
                    return student;
                }).collect(Collectors.toList());

                collect.forEach(item -> {
                    list.forEach( i -> {
                        if (i.getXyid().equals(item.getXyid()) && i.getKcid().equals(item.getKcid())){
                            item.setJd(i.getJd());
                        }
                    });
                });
                tCurriculumStudentMapper.insert(collect);
            }
        }
    }


    /**
     * 处理课程指定用户更新操作
     *   1. 先删除（逻辑删除）
     *   2. 保存进度
     *   3. 在插入
     * @param users 前端新提交的对应的指定学员
     * @param kcid  课程ID
     */
    @Transactional
    protected void delCurriculumPointStudents(List<TCurriculumStudentWithCp> users, Long kcid){
        List<TCurriculumStudentWithCp> list = tCurriculumStudentMapper.selectByKcId(kcid);
        tCurriculumStudentMapper.delRealtionByKcId(kcid);
        if (users.size() > 0){
            List<TCurriculumStudentWithCp> collect = users.stream().map(tCurriculumStudent -> {
                tCurriculumStudent.setCreateBy(SecurityUtils.getUsername());
                tCurriculumStudent.setUpdateBy(SecurityUtils.getUsername());
                tCurriculumStudent.setKcid(kcid);
                tCurriculumStudent.setXyid(tCurriculumStudent.getUserId());
                return tCurriculumStudent;
            }).collect(Collectors.toList());
            collect.forEach(item -> {
                list.forEach(i -> {
                    if (i.getUserId().equals(item.getXyid()) && i.getKcid().equals(item.getKcid())){
                        item.setJd(i.getJd());
                    }
                });
            });
            tCurriculumStudentMapper.insert(collect);
        }
    }


    /**
     * 批量删除curriculum
     * @param ids 需要删除的curriculumID
     * @return 结果
     */
    @Override
    public int deleteTCurriculumByIds(Long[] ids) {
        return tCurriculumMapper.deleteTCurriculumByIds(ids);
    }

    /**
     * 删除curriculum信息
     * @param id curriculumID
     * @return 结果
     */
    @Override
    public int deleteTCurriculumById(Long id) {
        return tCurriculumMapper.deleteTCurriculumById(id);
    }

    /**
     * 分步骤设置课程
     * @return
     */
    @Transactional
    @Override
    public Long completeTCurriculum(TCurriculumWithCp tCurriculum) {
        Long res = 0L;
        this.delCurriculumPointStudents(tCurriculum.getSysUsers(),tCurriculum.getId());
        tCurriculum.setUpdateTime(DateUtils.getNowDate());
        this.updateTCurriculum(tCurriculum);
        //tCurriculumMapper.updateTCurriculum(tCurriculum);
        // this.delCurriculumPointStudents(tCurriculum.getSysUsers(),tCurriculum.getId());
        if ("99".equals(tCurriculum.getStates())){
            this.push(tCurriculum.getId());
        }
        // 把之前的课程内容删除
        tCurriculumContentMapper.deleteTCurriculumContentByCid(tCurriculum.getId());

        // 插入 t_curriculum_content 信息
        List<TCurriculumContentWithCp> collect = tCurriculum.gettCurriculumContents().stream().peek(tCurriculumContent -> {
            tCurriculumContent.setCid(tCurriculum.getId());
            tCurriculumContent.setSid(tCurriculumContent.getId());
            tCurriculumContent.setId(null);
        }).collect(Collectors.toList());
        for (TCurriculumContentWithCp tCurriculumContent : collect) {
            //res += tCurriculumContentMapper.insertTCurriculumContent(tCurriculumContent);
            tCurriculumContentMapper.insertTCurriculumContent(tCurriculumContent);
        }
        res = tCurriculum.getId();
        return res;
    }

    /**
     * 根据课程ID获取课程内容
     */
    @Override
    public List<TCurriculumContentWithCp> getTCurriculumContentDetailByCid(Long id) {
        return tCurriculumContentMapper.selectTurriculumContentByCid(id);
    }

    /**
     * 撤回已发布课程
     * @param id 课程ID
     */
    @Override
    public void pushBack(Long id) {
        tCurriculumMapper.pushBack(id);
    }

    /**
     * 发布课程
     * @param id 课程ID
     */
    @Override
    public void push(Long id) {
        tCurriculumMapper.push(id);
    }


    @Override
    public Integer getAllCompletedKcUser(Long kcid) {
        return tCurriculumStudentMapper.getAllCompletedKcUser(kcid);
    }

    @Override
    public List<Map<String, Object>> getCompletedKcUserByDate(Long kcid, Long startTime, Long endTime) {
        return tCurriculumStudentMapper.getCompletedKcUserByDate(kcid,startTime,endTime);
    }

    @Override
    public Integer getCompletedCourse(Long kcid) {
        return tCurriculumStudentMapper.getCompletedCourse(kcid);
    }

    @Override
    public Integer getReceivedKcUser(Long kcid) {
        return tCurriculumStudentMapper.getReceivedKcUser(kcid);
    }

    @Override
    public Integer getYesterdayStudyKc(Long kcid) {
        return tCurriculumStudentMapper.getYesterdayStudyKc(kcid);
    }

    @Override
    public Integer getCourseNum(Long kcid) {
        return tCurriculumStudentMapper.getCourseNum(kcid);
    }

    @Override
    public List<Map<String, Object>> getCompletedKjByDate(Long kcid, String startTime, String endTime,String[] dates) {
        return tCurriculumStudentMapper.getCompletedKjByDate(kcid,startTime,endTime, dates);
    }

    @Override
    public List<Map<String, Object>> getCompletedKcKjDate(String startTime, String endTime, String[] dates) {
        return tCurriculumStudentMapper.getCompletedKcKjDate(startTime,endTime,dates);
    }

    @Override
    public Integer getFbCurriculum() {
        return tCurriculumStudentMapper.getFbCurriculum();
    }

    @Override
    public boolean checkExistKC(TCurriculumWithCp curriculum) {
        Long id = StringUtils.isNull(curriculum.getId()) ? -1L : curriculum.getId();
        TCurriculumWithCp result = tCurriculumMapper.checkExistKC(curriculum.getMc());
        if(StringUtils.isNotNull(result) && result.getId().longValue()!=id.longValue() ){
            return true;
        }
        return false;
    }

    @Override
    public int getKcLearnTime(Long kcid) {
        return tCurriculumMapper.getKcLearnTime(kcid);
    }
    @Override
    public List<Map<String,Object>> getKcListByKjTime(Long kcid){
        return tCurriculumMapper.getKcListByKjTime(kcid);
    }

    @Override
    public List<Map<String, Object>> getEveKcLearnTime() {
        return tCurriculumMapper.getEveKcLearnTime();
    }

    /**
     * 获取課程的关联分类
     *
     * @param curriculum
     * @param master
     * @return
     */
    private List<ObjectInterCategory> getCurriculmInterCategory(TCurriculumWithCp curriculum, TCurriculumWithCp master) {
        return curriculum.getCategoryIds().stream().map(ancestors -> {
            ObjectInterCategory interCategory = new ObjectInterCategory();
            Long catId = ancestors.get(ancestors.size()-1);
            interCategory.setCatId(catId);
            interCategory.setObjId(master.getId());
            // 设置祖级列表
            Category category = categoryService.selectById(categoryEnum, catId);
            interCategory.setAncestors(category.getAncestors()==null||category.getAncestors().equals("0")||category.getAncestors().length()==0?
                    String.valueOf(catId):category.getAncestors()+","+catId);
            return interCategory;
        }).collect(Collectors.toList());
    }
}
