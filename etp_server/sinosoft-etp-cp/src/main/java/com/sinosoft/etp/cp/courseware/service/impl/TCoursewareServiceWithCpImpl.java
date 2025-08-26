package com.sinosoft.etp.cp.courseware.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.admin.question.domain.QuestionReqDTO;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.annotation.VipCheck;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.courseware.mapper.TCoursewareCompanyWithCpMapper;
import com.sinosoft.etp.cp.courseware.mapper.TCoursewareWithCpMapper;
import com.sinosoft.etp.cp.courseware.service.ITCoursewareServiceWithCp;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 课件管理Service业务层处理
 *
 *
 * @date 2021-11-08
 */
@Service
public class TCoursewareServiceWithCpImpl implements ITCoursewareServiceWithCp
{
    @Resource
    private TCoursewareWithCpMapper tCoursewareMapper;

    @Resource
    private TCoursewareCompanyWithCpMapper tCoursewareCompanyMapper;

    @Resource
    private ITTagWithCpService iTTagService;

    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.COURSEWARE_CATEGORY;

    @Resource
    private IObjectInterCategoryService objectInterCategoryService;

    @Resource
    private ICategoryService categoryService;
    /**
     * 查询课件管理
     *
     * @param id 课件管理ID
     * @return 课件管理
     */
    @Override
    public TCoursewareWithCp selectTCoursewareById(Long id)
    {
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
        TCoursewareWithCp courseware =  tCoursewareMapper.selectTCoursewareById(id);
        courseware.setCategoryIds(categories);
        return courseware;
    }

    /**
     * 查询课件管理列表
     *
     * @param tCourseware 课件管理
     * @return 课件管理
     */
    @Override
    @DataScope(deptAlias = "d") //错误用法 别名是t
    @EtpDataScope(alias = "t")
    public List<TCoursewareWithCp> selectTCoursewareList(TCoursewareWithCp tCourseware)
    {
        return tCoursewareMapper.selectTCoursewareList(tCourseware);
    }

    /**
     * 新增课件管理
     *
     * @param tCourseware 课件管理
     * @return 结果
     */
    @VipCheck(resource="courseware")
    @Override
    @Transactional
    public int insertTCourseware(TCoursewareWithCp tCourseware)
    {
        EtpDataScopeUtils.setDataScope(tCourseware);// 20250517 lyd etp数据权限相关字段

        // 新增加课件
        tCourseware.setCreateTime(DateUtils.getNowDate());
        tCourseware.setUpdateTime(DateUtils.getNowDate());
        tCourseware.setCreateBy(SecurityUtils.getUsername());
        int res = tCoursewareMapper.insertTCourseware(tCourseware);

        // 增加课件-企业的关联关系
        //TCoursewareCompanyWithCp coursewareWithCompany = new TCoursewareCompanyWithCp();
        //coursewareWithCompany.setKjid(tCourseware.getId());
        //coursewareWithCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tCoursewareCompanyMapper.insertTCoursewareCompany(coursewareWithCompany);

        // 插入关联分类
        if (tCourseware.hasCategoryIds()) {
            List<ObjectInterCategory> categories = getCoursewareInterCategory(tCourseware, tCourseware);
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories);
        }

        //插入题库标签关联表
        if (tCourseware.getParams().get("tagStr") != null) {
            String tagStr = tCourseware.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tCourseware.getId(),tagStr,"KJ");
        }

        return res;
    }

    /**
     * 修改课件管理
     *
     * @param tCourseware 课件管理
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTCourseware(TCoursewareWithCp tCourseware)
    {
        tCourseware.setUpdateTime(DateUtils.getNowDate());
        // 删除课件
//        tCoursewareMapper.deleteTCoursewareById(tCourseware.getId());
        //删除标签关联关系
        iTTagService.deleteRelateTag(tCourseware.getId(),"KJ");
        // 删除之前的课件企业关联
        //tCoursewareCompanyMapper.deleteTCoursewareCompanyByKjId(tCourseware.getId());
        // 插入新课件
//        tCourseware.setCreateTime(DateUtils.getNowDate());
//        tCourseware.setUpdateTime(DateUtils.getNowDate());
//        tCourseware.setCreateBy(SecurityUtils.getUsername());
//        int res = tCoursewareMapper.insertTCourseware(tCourseware);

        //更新 课件
        int res = tCoursewareMapper.updateTCourseware(tCourseware);

        //插入课件标签关联表
        if(null != tCourseware.getParams().get("tagStr")){
            String tagStr = tCourseware.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tCourseware.getId(),tagStr,"KJ");
        }
        // 删除关联分类 && 插入关联分类
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(tCourseware.getId()));
        if (tCourseware.hasCategoryIds()) {
            List<ObjectInterCategory> interCategory = getCoursewareInterCategory(tCourseware, tCourseware);
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, interCategory);
        }
        // 创建课件与企业的关联关系
        //TCoursewareCompanyWithCp coursewareWithCompany = new TCoursewareCompanyWithCp();
        //coursewareWithCompany.setKjid(tCourseware.getId());
        //coursewareWithCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tCoursewareCompanyMapper.insertTCoursewareCompany(coursewareWithCompany);
        return res;
    }

    /**
     * 批量删除课件管理
     *
     * @param ids 需要删除的课件管理ID
     * @return 结果
     */
    @Override
    public int deleteTCoursewareByIds(Long[] ids)
    {
        return tCoursewareMapper.deleteTCoursewareByIds(ids);
    }

    /**
     * 删除课件管理信息
     *
     * @param id 课件管理ID
     * @return 结果
     */
    @Override
    public int deleteTCoursewareById(Long id)
    {
        return tCoursewareMapper.deleteTCoursewareById(id);
    }

    @Override
    public List<TCoursewareWithCp> getSelectedKjByKc(Long kcid) {
        return tCoursewareMapper.getSelectedKjByKc(kcid);
    }

    @Override
    public boolean checkExistKJ(TCoursewareWithCp coursewareWithCp) {

        Long id = StringUtils.isNull(coursewareWithCp.getId()) ? -1L : coursewareWithCp.getId();
        TCoursewareWithCp result = tCoursewareMapper.checkExistKJ(coursewareWithCp.getMc());
        if(StringUtils.isNotNull(result) && result.getId().longValue()!=id.longValue() ){
            return true;
        }
        return false;
    }


    /**
     * 获取课件的关联分类
     *
     * @param courseware
     * @param master
     * @return
     */
    private List<ObjectInterCategory> getCoursewareInterCategory(TCoursewareWithCp courseware, TCoursewareWithCp master) {
        return courseware.getCategoryIds().stream().map(ancestors -> {
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
