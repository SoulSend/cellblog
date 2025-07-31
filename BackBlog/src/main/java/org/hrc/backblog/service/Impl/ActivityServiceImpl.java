package org.hrc.backblog.service.Impl;

import org.hrc.backblog.dao.mapper.ActivityMapper;
import org.hrc.backblog.dao.pojo.Activity;
import org.hrc.backblog.service.ActivityService;
import org.hrc.backblog.vo.ActivityVo;
import org.hrc.backblog.vo.ErrorCode;
import org.hrc.backblog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 获取活动列表
     * @return
     */
    @Override
    public Result listActivity() {
        List<Activity> activities = activityMapper.selectList(null);
        if(activities==null){
            return Result.fail(ErrorCode.ACTIVITY_ABSENT.getCode(), ErrorCode.ACTIVITY_ABSENT.getMsg());
        }
        return Result.success(copyList(activities,true));
    }

    /**
     * 根据id获取活动详情
     * @param id
     * @return
     */
    @Override
    public Result activityDetail(Integer id) {
        Activity activity = activityMapper.selectById(id);
        return Result.success(copy(activity,false));
    }

    /**
     * 发布活动
     * @param activity
     * @return
     */
    @Override
    public Result publish(Activity activity) {
        activity.setParticipants(1);
        activity.setViews(1);
        activityMapper.insert(activity);
        return Result.success(null);
    }

    /**
     * 根据id删除活动
     * @param id
     * @return
     */
    @Override
    public Result delete(Integer id) {
        activityMapper.deleteById(id);
        return Result.success(null);
    }

    /**
     * 更新活动
     * @param activity
     * @return
     */
    @Override
    public Result update(Activity activity) {
        System.out.println(activity);
        activityMapper.updateById(activity);
        return Result.success(null);
    }

    private ActivityVo copy(Activity activity ,boolean isList){
        ActivityVo activityVo=new ActivityVo();
        BeanUtils.copyProperties(activity,activityVo);
        if(isList){
            activityVo.setDetailIntroduction(null);
            activityVo.setParticipationUrl(null);
        }
        return activityVo;
    }
    private List<ActivityVo> copyList(List<Activity> activities,boolean isList){
        List<ActivityVo> activityVoList=new ArrayList<>();
        for (Activity activity : activities) {
            activityVoList.add(copy(activity,isList));
        }
        return activityVoList;
    }
}
