package org.hrc.backblog.dao.pojo;

import lombok.Data;

/**
 * 评论实体类
 */
@Data
public class Comment {

    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;
    /**
     * 当前评论者的用户id
     */
    private Long authorId;
    /**
     * 它的父评论id
     */
    private Long parentId;
    /**
     * 它的父评论的用户id
     */
    private Long toUid;
    /**
     * 它是第几层评论搭配前面两个属性来做比较，从而可以定位是父评论还是子评论
     */
    private Integer level;
    /**
     * 是否被举报 0 未举报 1 已举报
     */
    private boolean reported;

}
