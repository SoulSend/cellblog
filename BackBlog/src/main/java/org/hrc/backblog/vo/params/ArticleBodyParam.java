package org.hrc.backblog.vo.params;

import lombok.Data;

/**
 * 前端的文章体请求参数实体类
 */
@Data
public class ArticleBodyParam {

    private Long articleId;
    private String content;

    private String contentHtml;

}
