package org.hrc.backblog.vo.params;

import lombok.Data;
import org.hrc.backblog.vo.CategoryVo;
import org.hrc.backblog.vo.TagVo;

import java.util.List;
/**
 * 文章请求实体类
 */
@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
