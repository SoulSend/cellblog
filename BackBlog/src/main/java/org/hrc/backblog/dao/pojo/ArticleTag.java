package org.hrc.backblog.dao.pojo;

import lombok.Data;

/**
 * 文章-标签实体类
 */
@Data
public class ArticleTag {
    private Long id;
    private Long articleId;
    private  Long tagId;
}
