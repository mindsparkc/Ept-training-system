package com.sinosoft.etp.cp.testpaper.mapper;

import com.sinosoft.etp.cp.testpaper.domain.TTestpaperCompanyWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷企业关联Mapper接口
 *
 *
 * @date 2021-11-22
 */
public interface TTestpaperCompanyWithCpMapper
{
    /**
     * 查询试卷企业关联
     *
     * @param id 试卷企业关联ID
     * @return 试卷企业关联
     */
    public TTestpaperCompanyWithCp selectTTestpaperCompanyById(Long id);

    /**
     * 查询试卷企业关联列表
     *
     * @param tTestpaperCompany 试卷企业关联
     * @return 试卷企业关联集合
     */
    public List<TTestpaperCompanyWithCp> selectTTestpaperCompanyList(TTestpaperCompanyWithCp tTestpaperCompany);

    /**
     * 新增试卷企业关联
     *
     * @param tTestpaperCompany 试卷企业关联
     * @return 结果
     */
    public int insertTTestpaperCompany(TTestpaperCompanyWithCp tTestpaperCompany);

    /**
     * 修改试卷企业关联
     *
     * @param tTestpaperCompany 试卷企业关联
     * @return 结果
     */
    public int updateTTestpaperCompany(TTestpaperCompanyWithCp tTestpaperCompany);

    /**
     * 删除试卷企业关联
     *
     * @param id 试卷企业关联ID
     * @return 结果
     */
    public int deleteTTestpaperCompanyById(Long id);

    /**
     * 批量删除试卷企业关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestpaperCompanyByIds(Long[] ids);

    public int deleteTTestpaperCompanyBySjid(@Param("sjid") Long sjid,@Param("qyid")  Long qyid);

    public int deleteTTestpaperCompanyBySjids(@Param("sjids") Long[] sjids,@Param("qyid")  Long qyid);
}
