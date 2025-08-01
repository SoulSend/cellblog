package org.hrc.backblog.controller;

import org.hrc.backblog.common.aop.LogAnnotation;
import org.hrc.backblog.service.Impl.LoginServiceImpl;
import org.hrc.backblog.service.LoginService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.AdminLoginParam;
import org.hrc.backblog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 用户登录接口 处理登录请求
     * @param loginParam post请求参数，包含账户和密码
     * @return 返回一个包含token的Result
     */
    @PostMapping
    @LogAnnotation(module = "登录模块",operation = "用户登录")
    public Result login(@RequestBody LoginParam loginParam){
       return  loginService.login(loginParam);
    }
    /**
     * 管理员登录
     */
    @PostMapping("admin")
    @LogAnnotation(module = "登录模块",operation = "管理员登录")
    public Result adminLogin(@RequestBody AdminLoginParam adminLoginParam){
        return  loginService.adminLogin(adminLoginParam);
    }
}
