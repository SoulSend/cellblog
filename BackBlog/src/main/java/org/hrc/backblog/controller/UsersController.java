package org.hrc.backblog.controller;

import org.hrc.backblog.common.aop.LogAnnotation;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.ModifyUserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsersController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 处理当前用户的请求，前端登陆后湖区token跳转首页时，会发起一次请求获取用户的基本信息，用来渲染首页
     * @param token 登陆时获取的token
     * @return 返回一个Result，包含了用户的基本信息
     */
    @GetMapping("/currentUser")
    @LogAnnotation(module = "用户模块",operation = "当前用户")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.getUserInfoByToken(token.substring(7),false);
    }

    /**
     * 根据请求头里面的token负载的当前用户的id，查询用户详情：
     * 账户，头像，注册日，邮箱（可添加），电话，网名
     * 该账户编写的文章
     * @param token
     * @return
     */
    @GetMapping("/userInfo")
    public Result userInfo(@RequestHeader("Authorization") String token){
        return sysUserService.getUserInfoByToken(token.substring(7),true);
    }

    /**
     * 修改当前用户信息
     * @param modifyUserParam
     * @return
     */
    @PostMapping("/changeUserInfo")
    public Result changeUserInfo(@RequestBody ModifyUserParam modifyUserParam){
        return sysUserService.changeUserInfo(modifyUserParam);

    }


}
