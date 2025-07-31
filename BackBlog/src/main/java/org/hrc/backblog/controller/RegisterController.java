package org.hrc.backblog.controller;

import org.hrc.backblog.service.LoginService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private LoginService loginService;

    /**
     * 注册
     * @param loginParam
     * @return
     */
    @RequestMapping
    public Result register(@RequestBody LoginParam loginParam){
        return loginService.register(loginParam);
    }
}
