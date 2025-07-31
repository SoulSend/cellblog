package org.hrc.backblog.vo.params;

import lombok.Data;

/**
 * 文章列表请求参数实体类
 */
@Data
public class PageParams {
    private int page=1;
    private int pageSize=10;

    private Long categoryId;

    private Long tagId;
}
