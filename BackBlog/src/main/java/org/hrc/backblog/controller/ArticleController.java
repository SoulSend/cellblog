package org.hrc.backblog.controller;

import org.hrc.backblog.service.ArticleService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.ArticleParam;
import org.hrc.backblog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * use
     * 获取首页文章列表
     * @param pageParams 前端参数，获取查询的页数，和页面的大小
     * @return 返回一个封装了响应结果的统一结果类
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * use
     * 获取首页最热文章
     * @return 返回一个Result
     */
    @GetMapping("/hot")
    public Result hotArticle(){
        int limit =5;
        return articleService.hotArticle(limit);
    }

    /**
     * 获取首页最新文章
     * @return 返回一个Result
     */
    @PostMapping("/new")
    public Result newArticle(){
        int limit =5;
        return articleService.newArticle(limit);
    }

    /**
     * 文章归档
     * @return 返回一个Result
     */
    @PostMapping("/listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * use
     * 获取文章详情
     * @param id
     * @return
     */
    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id")Long id){
        return articleService.findArticleById(id);
    }
    /**
     * use
     * 写文章
     */
    @PostMapping("/publish")
    public Result articlePublish(@RequestBody ArticleParam articleParam){
        return articleService.articlePublish(articleParam);
    }
    /** use
     * 根据用户id获取用户发布的文章
     */
    @GetMapping("/userInfo/articleList")
    public Result userInfoArticleList(){
        return articleService.userInfoArticleList();
    }


}
