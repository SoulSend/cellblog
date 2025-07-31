package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.hrc.backblog.dao.mapper.ArticleMapper;
import org.hrc.backblog.dao.pojo.Article;
import org.hrc.backblog.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThreadServiceImpl implements ThreadService {
    @Autowired
    private ArticleMapper articleMapper;
    /**
     * 使用默认的线程池来实现多线程
     * 更新文章浏览量
     * @param article
     */
    @Override
    @Async
    public void updateArticleViewCountThread(Article article) {
        //更新后的实体类
        Article articleUpdate=new Article();
        articleUpdate.setViewCounts(article.getViewCounts()+1);
        //更新条件
        LambdaUpdateWrapper<Article> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //防止多线程数据脏读，这里手动添加一个乐观锁，也就是，只在当前阅读量和数据库中
        //的阅读量一样的时候再做更新，否则不更新
        updateWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(articleUpdate,updateWrapper);
        log.info("用户浏览量更新完毕");
    }

    /**
     * 多线程更新文章评论数
     */
    @Override
    public void updateArticleCommentCount(Long articleId) {
        //根据文章id获取文章，然后获取文章评论数
        Article article = articleMapper.selectById(articleId);
        //更新后的实体类
        Article articleUpdate=new Article();
        articleUpdate.setCommentCounts(article.getCommentCounts()+1);
        //更新条件
        LambdaUpdateWrapper<Article> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //防止多线程数据脏读，这里手动添加一个乐观锁，也就是，只在当前评论数量和数据库中
        //的评论数量一样的时候再做更新，否则不更新
        updateWrapper.eq(Article::getCommentCounts,article.getCommentCounts());
        articleMapper.update(articleUpdate,updateWrapper);
        log.info("用户评论量更新完毕");
    }
}
