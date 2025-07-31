package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.Article;
import org.hrc.backblog.vo.ArticleBodyVo;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.ArticleParam;
import org.hrc.backblog.vo.params.PageParams;

public interface ArticleService {

    Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticle(int limit);

    Result listArchives();

    Result findArticleById(Long id);

    Result articlePublish(ArticleParam articleParam);

    Result findArticlesByTitle(String title);

    Result userInfoArticleList();


    void deleteArticleByUserId(String id);

    void deleteArticleBodyById(String id);

    void deleteArticleByCategoryId(String id);

    void updateWeightById(String id);

    void deleteArticleById(String id);

    void updateArticle(Article article);




}
