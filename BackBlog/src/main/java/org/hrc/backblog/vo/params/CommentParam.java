package org.hrc.backblog.vo.params;

import lombok.Data;

/**
 * 评论请求参数实体类
 */
@Data
public class CommentParam {

    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
