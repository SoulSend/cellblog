package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.Article;

public interface ThreadService {
    void updateArticleViewCountThread(Article article);

    void updateArticleCommentCount(Long articleId);
}
