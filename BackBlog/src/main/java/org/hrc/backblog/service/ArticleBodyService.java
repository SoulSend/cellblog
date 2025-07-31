package org.hrc.backblog.service;

import org.hrc.backblog.vo.ArticleBodyVo;
import org.hrc.backblog.vo.params.ArticleBodyParam;

public interface ArticleBodyService {
    ArticleBodyVo getArticleBodyById(Long id);

    void updateArticleBodyById(ArticleBodyParam articleBodyParamParam);

    ArticleBodyVo getArticleBodyByArticleId(Long aLong);
}
