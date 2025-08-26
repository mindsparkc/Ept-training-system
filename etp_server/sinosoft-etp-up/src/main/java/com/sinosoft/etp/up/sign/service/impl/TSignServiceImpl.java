package com.sinosoft.etp.up.sign.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.sign.domain.TSign;
import com.sinosoft.etp.up.sign.domain.TSignStudent;
import com.sinosoft.etp.up.sign.mapper.TSignCompanyMapper;
import com.sinosoft.etp.up.sign.mapper.TSignMapper;
import com.sinosoft.etp.up.sign.mapper.TSignStudentMapper;
import com.sinosoft.etp.up.sign.service.ITSignService;
import com.sinosoft.etp.up.tag.service.ITTagService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * signService业务层处理
 *
 *
 * @date 2022-02-09
 */
@Service
public class TSignServiceImpl implements ITSignService
{
    @Resource
    private TSignMapper tSignMapper;

    @Resource
    private TSignCompanyMapper tSignCompanyMapper;

    @Resource
    private ITTagService iTTagService;
    @Resource
    private TSignStudentMapper tSignStudentMapper;
    /**
     * 查询sign
     *
     * @param id signID
     * @return sign
     */
    @Override
    public TSign selectTSignById(Long id)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        TSign tSign = tSignMapper.selectTSignById(id,xyid);
        tSign.setNum(tSignMapper.countJoinSignById(id));
        return tSign;
    }

    /**
     * 查询sign列表
     *
     * @param tSign sign
     * @return sign
     */
    @Override
    @DataScope(deptAlias = "d") // bug
    @EtpDataScope(alias = "ts")
    public List<TSign> selectTSignList(TSign tSign)
    {
        //更改活动状态
        checkSignStatus(tSign);
        return tSignMapper.selectTSignList(tSign);
    }

    /**
     * 活动表全表扫描-数量级本企业
     */
    @DataScope(deptAlias = "d")
    private void checkSignStatus(TSign tSign){
        List<TSign> tSigns1 = tSignMapper.selectTSignList(tSign);
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
    public int insertTSign(TSign tSign)
    {
        EtpDataScopeUtils.setDataScope(tSign);// 20250520 lyd etp数据权限相关字段

        tSign.setCreateTime(DateUtils.getNowDate());
        tSign.setUpdateTime(DateUtils.getNowDate());
        tSign.setCreateBy(SecurityUtils.getUsername());
        tSign.setUpdateBy(SecurityUtils.getUsername());
        int res = tSignMapper.insertTSign(tSign);
        //操作 t_sign_company关联表
        //TSignCompany tSignCompany = new TSignCompany();
        //tSignCompany.setSignId(tSign.getId());
        //tSignCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //res += tSignCompanyMapper.insertTSignCompany(tSignCompany);

        //插入活动标签关联表
        if (tSign.getParams().get("tagStr") != null){
            String tagStr = tSign.getParams().get("tagStr").toString();
            iTTagService.commpanyInsertTag(tagStr);
            iTTagService.insertRelateTag(tSign.getId(),tagStr,"HD");
        }

        return res;
    }

    /**
     * 修改sign
     *
     * @param tSign sign
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTSign(TSign tSign)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        if("0".equals(tSign.getKfcd())){
            //公开的活动，先判断student表中有没有这个人员的活动数据，没有的话插入
            TSignStudent data = tSignStudentMapper.selectTSignStudentById(tSign.getId(),xyid);
            if(null==data){
                TSignStudent student = new TSignStudent();
                student.setHdid(tSign.getId());
                student.setXyid(xyid);
                student.setSfbm("0");
                tSignStudentMapper.insertTSignStudent(student);
            }
        }
        tSign.setXyid(xyid);
        int res = tSignMapper.updateTSign(tSign);

        return res;
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

    @Override
    public Map<String, Object> getHomeActivite() {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        return tSignMapper.getHomeActivite(xyid);
    }
}
