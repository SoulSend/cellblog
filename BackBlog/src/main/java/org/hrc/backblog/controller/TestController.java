package org.hrc.backblog.controller;

import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.utils.UserThreadLocal;
import org.hrc.backblog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    /**
     * 接口测试
     * @return
     */
    @RequestMapping
    public Result test(){
        /**
         * 测试使用ThreadLocal里面的数据
         */
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);

        return Result.success(null);
    }
}
