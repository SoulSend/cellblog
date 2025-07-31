package org.hrc.backblog.controller;

import org.hrc.backblog.dao.pojo.ArticleBody;
import org.hrc.backblog.service.AdminService;
import org.hrc.backblog.service.ArticleBodyService;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 获取管理员用户的详情，主要是头像和用户名
     * @param token
     * @return
     */
    @GetMapping("/userinfo")
    public Result userInfo(@RequestParam String token){
        return adminService.userInfo(token);
    }

    /****************************************用户管理**************************************/

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id){
        return adminService.delete(id);
    }
    /**
     * 获取所有用户的所有信息
     */
    @GetMapping("/all")
    public Result all(){
        return sysUserService.findAll();
    }

    /**
     * 根据id封禁/解禁用户
     */
    @PutMapping("/ban/{id}")
    public Result ban(@PathVariable Long id){
        return sysUserService.ban(id);
    }

    /**
     * 修改用户信息
     */
    @PostMapping("/changeUserInfo")
    public Result changeUserInfo(@RequestBody ModifyUserParam modifyUserParam){
        return sysUserService.changeUserInfo(modifyUserParam);
    }
    /**
     * 修改用户密码
     */
    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody ModifyUserParam modifyUserParam){
        return adminService.changePassword(modifyUserParam);
    }

    /***************************************分类管理***************************************/

    /**
     * 根据文章分类的id修改对应id分类的数据信息
     */
    @PostMapping("/updateCategory")
    public Result updateCategory(@RequestBody CategoryParam categoryParam){
        return adminService.updateCategory(categoryParam);
    }
    /**
     * 根据分类的id删除对应的分类，和分类关联的文章
     */
    @DeleteMapping("/deleteCategory/{id}")
    public Result deleteCategory(@PathVariable String id){
        return adminService.deleteCategory(id);
    }

    /*****************************************文章管理*************************************/

    /**
     * 根据文章id置顶文章
     */
    @PutMapping("/top/{id}")
    public Result top(@PathVariable String id){
        return adminService.top(id);
    }
    /**
     * 根据文章id删除文章
     */
    @DeleteMapping("/deleteArticle/{id}")
    public Result deleteArticle(@PathVariable String id){
        return adminService.deleteArticle(id);
    }
    /**
     * 修改文章的基本信息
     */
    @PostMapping("/updateArticle")
    public Result updateArticle(@RequestBody AdminArticleParam adminArticleParam){
        return adminService.updateArticle(adminArticleParam);
    }
    /**
     * 根据文章id获取文章的body
     */
    @GetMapping("/getArticleBody/{id}")
    public Result updateArticleBody(@PathVariable String id){
        return adminService.getArticleBodyById(id);
    }
    /**
     * 根据文章id修改文章的body
     */
    @PostMapping("/updateArticleBodyById")
    public Result updateArticleBodyById(@RequestBody ArticleBodyParam articleBodyParamParam){
        return adminService.updateArticleBodyById(articleBodyParamParam);
    }

}
