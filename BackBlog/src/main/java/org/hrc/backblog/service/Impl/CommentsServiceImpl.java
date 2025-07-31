package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.hrc.backblog.dao.mapper.CommentsMapper;
import org.hrc.backblog.dao.pojo.Comment;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.CommentsService;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.service.ThreadService;
import org.hrc.backblog.utils.UserThreadLocal;
import org.hrc.backblog.vo.CommentVo;
import org.hrc.backblog.vo.ErrorCode;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.UserVo;
import org.hrc.backblog.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ThreadService threadService;

    /**
     * 根据文章id获取评论列表
     * @param articleId
     * @return
     */
    @Override
    public Result getCommentsByArticleId(Long articleId) {
        /**
         * 1、根据文章id获取品论列表
         * 2、根据作者id查询评论者信息
         * 3、如果评论的level是1的话，还要去查询它的子评论，
         * 使用它的id作为parentId查询其他字段parentId是否一致，
         * 一致则是子评论，封装到children
         */
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    /**
     * 添加评论
     * @param commentParam 评论参数
     * @return 返回一个Result
     */
    @Override
    public Result comment(CommentParam commentParam) {
        /**
         * 1、添加创建日期,赋值名字相同的属性
         * 2、ThreadLocal获取当前用户的用户id，赋值给authorId
         * 3、根据Parent判断level是属于第几层（1、2）
         * 4、赋值parent->parentId
         * 5、赋值toUserId->toUid
         * 6、增加评论数量
         */
        Comment comment=new Comment();
        //赋值名字相同的属性
        BeanUtils.copyProperties(commentParam,comment);

        //设置当前时间
        comment.setCreateDate(System.currentTimeMillis());

        //ThreadLocal获取当前用户的用户id,赋值给authorId
        System.out.println(UserThreadLocal.get());
        comment.setAuthorId(UserThreadLocal.get().getId());

        //根据Parent判断level是属于第几层（1、2）
        Long parentId = commentParam.getParent();
        if (parentId==null||parentId==0)
            comment.setLevel(1);
        else
            comment.setLevel(2);

        //赋值parent->parentId
        comment.setParentId(parentId==null?0:parentId);

        //赋值toUserId->toUid
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId==null?0:toUserId);

        commentsMapper.insert(comment);
        //文章的评论数加1
        threadService.updateArticleCommentCount(comment.getArticleId());
        return Result.success(null);
    }

    /**
     * 获取所有评论
     * @return
     */
    @Override
    public Result list() {
        List<Comment> comments = commentsMapper.selectList(null);
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    /**
     * 根据id删除评论
     * 如果它有子评论还要连带删除子评论
     * @param id
     * @return
     */

    @Override
    public Result delete(Long id) {
        //根据当前评论id获取评论
        Comment comment = commentsMapper.selectById(id);
        System.out.println("评论id："+id);
        //为空直接返回报错
        if (comment==null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //找到这个评论了
        //判断level是不是为1
        //如果为1则表名不是子评论则判断是否还有子评论，有就删除

        if(comment.getLevel()==1){
            //判断是否有子评论，有就删除
            LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getParentId,id);
            List<Comment> childrenComments = commentsMapper.selectList(queryWrapper);
            if (!childrenComments.isEmpty()){
                //如果有子评论，就删除
                for (Comment childrenComment : childrenComments) {
                    commentsMapper.deleteById(childrenComment.getId());
                    System.out.println("子删除成功");
                }
            }
        }
        commentsMapper.deleteById(id);

        return Result.success(null);
    }

    /**
     * 根据用户的id获取该用户的评论列表
     * @return
     */
    @Override
    public Result userCommentList(Long id) {
        if(id==null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getAuthorId,id);
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    /**
     *  admin 获取所有被举报的评论
     * @return
     */
    @Override
    public Result reported() {
        List<Comment> comments = commentsMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::isReported, true));
        return Result.success( copyList(comments));
    }

    /**
     * 根据用户id删除该用户的所有评论
     * @param id
     */
    @Override
    public void deleteCommentsIdByUserId(String id) {
        //查询该用户的所有评论id
        List<Comment> comments = commentsMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getAuthorId, id));
        for (Comment comment : comments) {
            commentsMapper.deleteById(comment.getId());
        }
    }

    /**
     * 根据文章id删除所有对应的评论
     * @param aid
     */
    @Override
    public void deleteCommentsIdByArticleId(String aid) {
        List<Comment> comments = commentsMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, aid));
        for (Comment comment : comments) {
            commentsMapper.deleteById(comment.getId());
        }
    }

    /**
     *
     * @param
     * @return
     */
    @Override
    public Result report(String id) {
        //使用id查询对应的评论，然后修改其reported字段为true，然后更新到数据库
        Comment comment = commentsMapper.selectById(id);
        comment.setReported(true);
        commentsMapper.updateById(comment);
        return Result.success(null);
    }


    /**
     * 根据父评论ParentId获取对应子评论
     * @param id ParentId
     * @return 返回一个包含所有子评论的集合
     */
    public List<CommentVo> findChildrenComments(Long id){
        LambdaQueryWrapper<Comment>  queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> childrenComments = commentsMapper.selectList(queryWrapper);
        return copyList(childrenComments);
    }

    /**
     * 调用copy方法将Comment集合复制成为CommentVo集合
     * @param comments
     * @return
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> CommentVos=new ArrayList<>();
        for (Comment comment : comments) {
            CommentVos.add(copy(comment));
        }
        return CommentVos;
    }

    /**
     * 传入一个comment，手动将对应的字段转换成为CommentVo
     * 由于存在嵌套评论，所以里面会有一个判断，如果是子评论
     * 那么不会调用查询子评论的方法findChildrenComments可以减少查询操作
     * 如果当前评论是一个子评论，那么会根据comment的toUid
     * 查询对应的userId的UserVo类返回赋值
     * @param comment
     * @return
     */
    public CommentVo copy (Comment comment)  {
        CommentVo commentVo=new CommentVo();
        //复制名字相同的属性
        BeanUtils.copyProperties(comment,commentVo);
        //格式化时间
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //根据authorId获取评论者用户信息
        UserVo userVo=sysUserService.getUserVoById(comment.getAuthorId());
        commentVo.setAuthor(userVo);
        //如果有子评论，就查询子评论
        if(comment.getLevel()==1){
            List<CommentVo> childrenComments = findChildrenComments(comment.getId());
            commentVo.setChildrens(childrenComments);
        }
        //如果现在是子评论，就根据toUid为vo设置toUser
        if(comment.getLevel()==2){
            UserVo parentUserVo=sysUserService.getUserVoById(comment.getToUid());
            commentVo.setToUser(parentUserVo);
        }
        return commentVo;
    }
}
