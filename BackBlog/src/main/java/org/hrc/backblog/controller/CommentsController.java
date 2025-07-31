package org.hrc.backblog.controller;

import org.hrc.backblog.service.CommentsService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    /**
     * 根据文章id获取获取评论列表
     * @return
     */
    @RequestMapping("/article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentsService.getCommentsByArticleId(articleId);
    }
    /**
     * 根据用户id获取该用户的所有评论
     */
    @GetMapping("/user/commentList/{id}")
    public Result userCommentList(@PathVariable("id") Long id){
        return commentsService.userCommentList(id);
    }
    /**
     * 获取所有评论
     */
    @GetMapping("/list")
    public Result list(){
        return commentsService.list();
    }
    /**
     * 获取所有被举报的评论
     */
    @GetMapping("/reported")
    public Result reported(){
        return commentsService.reported();
    }
    /**
     *根据id举报评论
     */
    @PutMapping("/report/{id}")
    public Result report(@PathVariable String id){
        return commentsService.report(id);
    }
    /**
     * 根据id删除评论
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        return commentsService.delete(id);
    }
    /**
     * 添加评论
     * @param commentParam
     * @return
     */
    @PostMapping("/create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }

}
