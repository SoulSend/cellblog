package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.Activity;
import org.hrc.backblog.vo.ActivityVo;
import org.hrc.backblog.vo.Result;
import org.springframework.stereotype.Service;


public interface ActivityService {
    public Result listActivity();

    Result activityDetail(Integer id);

    Result publish(Activity activity);

    Result delete(Integer id);

    Result update(Activity activity);
}
