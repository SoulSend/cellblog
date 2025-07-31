package org.hrc.backblog.dao.pojo;
import lombok.Data;

/**
 * 文章体实体类
 */
@Data
public class ArticleBody {
    private Long id;
    private String content;
    private String contentHtml;
    private Long articleId;
}
