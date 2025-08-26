package com.sinosoft.etp.up.courseware.service.impl;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.utils.RedisUtil;
import com.sinosoft.etp.up.courseware.domain.TCourseware;
import com.sinosoft.etp.up.courseware.mapper.TCoursewareCompanyMapper;
import com.sinosoft.etp.up.courseware.mapper.TCoursewareMapper;
import com.sinosoft.etp.up.courseware.service.ITCoursewareService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 课件管理Service业务层处理
 *
 *
 * @date 2021-11-08
 */
@Service
public class TCoursewareServiceImpl implements ITCoursewareService
{
    @Resource
    private TCoursewareMapper tCoursewareMapper;

    @Resource
    private TCoursewareCompanyMapper tCoursewareCompanyMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 查询课件管理
     *
     * @param id 课件管理ID
     * @return 课件管理
     */
    @Override
    public TCourseware selectTCoursewareById(Long id)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        TCourseware courseware = tCoursewareMapper.selectTCoursewareById(id,xyid);
        //历史学习时长
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String userId = String.valueOf(xyid);
        String key = "user_act_ing:" + sdf.format(new Date()) + ":" + userId;
        //历史学习时长
        String hKey = "kj:" + id + ":historyonline";
        //今天的 学习总时长记录
        String tKey = "kj:" + id + ":online";
        //判断key是否存在，存在 更新学习时长
        Long todayTime = 0l; //今天学习时长
        if(redisUtil.hasKey(key) && null !=redisUtil.getMapValue(key,tKey)){
            todayTime = Long.parseLong(redisUtil.getMapValue(key,tKey).toString());
        }
        //总学习时长
        Long historyTime = courseware.getLastStudyTime()==null?0L:Long.parseLong(courseware.getLastStudyTime());
        String allTime = String.valueOf((todayTime+historyTime)/60);
        courseware.setLastStudyTime(allTime);
        return courseware;
    }

    /**
     * 查询课件管理列表
     *
     * @param tCourseware 课件管理
     * @return 课件管理
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TCourseware> selectTCoursewareList(TCourseware tCourseware)
    {

        return tCoursewareMapper.selectTCoursewareList(tCourseware);
    }


}
