package org.hrc.backblog.vo.params;

import lombok.Data;

@Data
public class AdminArticleParam {
    private Long id;

    private String title;

    private String summary;
    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     *类别id
     */
    private Long categoryId;

}
