package org.hrc.backblog.controller;

import org.hrc.backblog.service.ArticleService;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SysUserService sysUserService;
    @GetMapping
    public Result search(@RequestParam("title") String title){
        return articleService.findArticlesByTitle(title);
    }
    /**
     * 根据用户名字查询用户
     */
    @GetMapping("/user")
    public Result searchUser(@RequestParam("username") String username){
        return sysUserService.findUserByUsername(username);
    }
}
