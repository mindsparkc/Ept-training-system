package com.sinosoft.etp.cp.testpaper.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperWithCp;
import com.sinosoft.etp.cp.testpaper.mapper.TTestpaperCompanyWithCpMapper;
import com.sinosoft.etp.cp.testpaper.mapper.TTestpaperWithCpMapper;
import com.sinosoft.etp.cp.testpaper.service.ITTestpaperWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 试卷Service业务层处理
 *
 *
 * @date 2021-11-19
 */
@Service
public class TTestpaperWithCpServiceImpl implements ITTestpaperWithCpService
{
    @Autowired
    private TTestpaperWithCpMapper tTestpaperMapper;
    @Autowired
    private TTestpaperCompanyWithCpMapper tTestpaperCompanyMapper;

    @Autowired
    private ITTagWithCpService iTTagService;

    /**
     * 查询试卷
     *
     * @param id 试卷ID
     * @return 试卷
     */
    @Override
    public TTestpaperWithCp selectTTestpaperById(Long id)
    {
        return tTestpaperMapper.selectTTestpaperById(id);
    }

    /**
     * 查询试卷列表
     *
     * @param tTestpaper 试卷
     * @return 试卷
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TTestpaperWithCp> selectTTestpaperList(TTestpaperWithCp tTestpaper)
    {
        return tTestpaperMapper.selectTTestpaperList(tTestpaper);
    }

    /**
     * 新增试卷
     *
     * @param tTestpaper 试卷
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertTTestpaper(TTestpaperWithCp tTestpaper)
    {
        if(null ==tTestpaper.getId()){
            //新增试卷
            tTestpaper.setCreateBy(SecurityUtils.getUsername());
            tTestpaper.setCreateTime(DateUtils.getNowDate());
            tTestpaper.setUpdateTime(DateUtils.getNowDate());
            tTestpaperMapper.insertTTestpaper(tTestpaper);
            //插入试卷企业关联表
            //TTestpaperCompanyWithCp testpaperCompany = new TTestpaperCompanyWithCp();
            //testpaperCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
            //testpaperCompany.setSjid(tTestpaper.getId());
            //testpaperCompany.setCreateTime(DateUtils.getNowDate());
            //testpaperCompany.setCreateBy(SecurityUtils.getUsername());
            //testpaperCompany.setUpdateTime(DateUtils.getNowDate());
            //tTestpaperCompanyMapper.insertTTestpaperCompany(testpaperCompany);
            //插入标签表
            if(null != tTestpaper.getParams().get("tagStr")){
                //企业插入标签
                iTTagService.commpanyInsertTag(tTestpaper.getParams().get("tagStr").toString());
                //插入题库标签关联表
                iTTagService.insertRelateTag(tTestpaper.getId(),tTestpaper.getParams().get("tagStr").toString(),"SJ");
            };
        }else{
            //更新试卷
            updateTTestpaper(tTestpaper);
        }
        return tTestpaper.getId();
    }

    /**
     * 修改试卷
     *
     * @param tTestpaper 试卷
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTTestpaper(TTestpaperWithCp tTestpaper)
    {
        tTestpaper.setUpdateTime(DateUtils.getNowDate());
        tTestpaper.setUpdateBy(SecurityUtils.getUsername());
        int i = tTestpaperMapper.updateTTestpaper(tTestpaper);

        if("1".equals(tTestpaper.getTagFlag())){
            //删除关联标签
            iTTagService.deleteRelateTag(tTestpaper.getId(),"SJ");
            //插入标签表
            if(null != tTestpaper.getParams().get("tagStr")){
                //插入t_tag表（插入时忽略已有的）
                iTTagService.commpanyInsertTag(tTestpaper.getParams().get("tagStr").toString());
                //插入题库标签关联表
                iTTagService.insertRelateTag(tTestpaper.getId(),tTestpaper.getParams().get("tagStr").toString(),"SJ");
            };

        }

        return i;
    }

    /**
     * 批量删除试卷
     *
     * @param ids 需要删除的试卷ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTTestpaperByIds(Long[] ids)
    {
        Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();
        //tTestpaperCompanyMapper.deleteTTestpaperCompanyBySjids(ids,qyid);
        return tTestpaperMapper.deleteTTestpaperByIds(ids);
    }

    /**
     * 删除试卷信息
     *
     * @param id 试卷ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTTestpaperById(Long id)
    {
        //删除企业试卷信息
        Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();
        //tTestpaperCompanyMapper.deleteTTestpaperCompanyBySjid(id,qyid);
        return tTestpaperMapper.deleteTTestpaperById(id);
    }
    /**
     * 撤回已发布试卷
     * @param id 试卷ID
     */
    @Override
    public void pushBack(Long id) {
        tTestpaperMapper.pushBack(id);
    }

    @Override
    public boolean checkExistSJ(TTestpaperWithCp tTestpaper) {
        Long id = StringUtils.isNull(tTestpaper.getId()) ? -1L : tTestpaper.getId();
        TTestpaperWithCp result = tTestpaperMapper.checkExistSJ(tTestpaper.getSjmc());
        if(StringUtils.isNotNull(result) && result.getId().longValue()!=id.longValue() ){
            return true;
        }
        return false;
    }
}
