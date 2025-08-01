package org.hrc.backblog.utils.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hrc.backblog.dao.pojo.Article;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class HotArticleCacheManager {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper  objectMapper;
    private final String HOT_ARTICLE_KEY="hot_article";

    public HotArticleCacheManager(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }
    public Article fetchHotArticle(Long id) throws Exception {
        String s = stringRedisTemplate.opsForValue().get(HOT_ARTICLE_KEY + id);
        log.debug("测试从redis获取的返回值："+s);
        if (s==null) {
            return null;
        }
        //缓存命中 刷新时间
        stringRedisTemplate.expire(HOT_ARTICLE_KEY + id, 30 + new Random().nextInt(30), TimeUnit.MINUTES);
        return objectMapper.readValue(s, Article.class);
    }
    public void cacheHotArticle(Article article) throws Exception {
        String s = objectMapper.writeValueAsString(article);
        stringRedisTemplate.opsForValue().set(HOT_ARTICLE_KEY + article.getId(), s, 30 + new Random().nextInt(30), TimeUnit.MINUTES);
    }
}
