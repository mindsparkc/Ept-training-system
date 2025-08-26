package com.sinosoft.etp.up.errorbook.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.errorbook.domain.TErrorbook;
import com.sinosoft.etp.up.errorbook.mapper.TErrorbookMapper;
import com.sinosoft.etp.up.errorbook.service.ITErrorbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 错题集Service业务层处理
 *
 *
 * @date 2022-05-11
 */
@Service
public class TErrorbookServiceImpl implements ITErrorbookService
{
    @Autowired
    private TErrorbookMapper tErrorbookMapper;

    /**
     * 查询错题集
     *
     * @param id 错题集ID
     * @return 错题集
     */
    @Override
    public TErrorbook selectTErrorbookById(Long id)
    {
        return tErrorbookMapper.selectTErrorbookById(id);
    }

    /**
     * 查询错题集列表
     *
     * @param tErrorbook 错题集
     * @return 错题集
     */
    @Override
    public List<TErrorbook> selectTErrorbookList(TErrorbook tErrorbook)
    {
        return tErrorbookMapper.selectTErrorbookList(tErrorbook);
    }

    /**
     * 新增错题集
     *
     * @param tErrorbook 错题集
     * @return 结果
     */
    @Override
    public int insertTErrorbook(TErrorbook tErrorbook)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String date = sdf.format(now);
        int count = 0;
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        Long id = tErrorbookMapper.selectTErrorbookByKs(null,tErrorbook.getKcid(),tErrorbook.getStid(),xyid);
        if(id == null){
            tErrorbook.setCreateTime(DateUtils.getNowDate());
            tErrorbook.setUpdateTime(DateUtils.getNowDate());
            tErrorbook.setXyid(xyid);
            tErrorbook.setRemake("在"+date+"的"+tErrorbook.getRemake());
            count = tErrorbookMapper.insertTErrorbook(tErrorbook);
        }else{
            tErrorbook.setId(id);
            tErrorbook.setXyid(xyid);
            tErrorbook.setUpdateTime(DateUtils.getNowDate());
            tErrorbook.setRemake("在"+date+"的"+tErrorbook.getRemake());
            count = tErrorbookMapper.updateTErrorbook(tErrorbook);
        }
        return count;
    }

    /**
     * 修改错题集
     *
     * @param tErrorbook 错题集
     * @return 结果
     */
    @Override
    public int updateTErrorbook(TErrorbook tErrorbook)
    {
        tErrorbook.setUpdateTime(DateUtils.getNowDate());
        return tErrorbookMapper.updateTErrorbook(tErrorbook);
    }

    /**
     * 批量删除错题集
     *
     * @param ids 需要删除的错题集ID
     * @return 结果
     */
    @Override
    public int deleteTErrorbookByIds(Long[] ids)
    {
        return tErrorbookMapper.deleteTErrorbookByIds(ids);
    }

    /**
     * 删除错题集信息
     *
     * @param id 错题集ID
     * @return 结果
     */
    @Override
    public int deleteTErrorbookById(Long id)
    {
        return tErrorbookMapper.deleteTErrorbookById(id);
    }
}
