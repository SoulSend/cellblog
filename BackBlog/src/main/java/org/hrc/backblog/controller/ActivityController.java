package org.hrc.backblog.controller;

import org.hrc.backblog.dao.pojo.Activity;
import org.hrc.backblog.service.ActivityService;
import org.hrc.backblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    /**
     * 处理获取活动列表的请求 activities
     * @return
     */
    @GetMapping
    public Result listActivity(){
        return activityService.listActivity();
    }

    /**
     * 处理活动详情的请求 activities/detail/{id}
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result activityDetail(@PathVariable Integer id){
        return activityService.activityDetail(id);
    }
    /**
     * 发布活动
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody Activity Activity){
        return activityService.publish(Activity);
    }
    /**
     * 删除活动
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        return activityService.delete(id);
    }
    /**
     * 更新活动
     */
    @PutMapping("/update")
    public Result update(@RequestBody Activity activity){
        return activityService.update(activity);
    }
}
