package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hrc.backblog.dao.dos.Archives;
import org.hrc.backblog.dao.mapper.ArticleBodyMapper;
import org.hrc.backblog.dao.mapper.ArticleMapper;
import org.hrc.backblog.dao.mapper.ArticleTagMapper;
import org.hrc.backblog.dao.pojo.Article;
import org.hrc.backblog.dao.pojo.ArticleBody;
import org.hrc.backblog.dao.pojo.ArticleTag;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.*;

import org.hrc.backblog.utils.UserThreadLocal;
import org.hrc.backblog.vo.*;
import org.hrc.backblog.vo.params.ArticleParam;
import org.hrc.backblog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyService articleBodyService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //文章类型服务接口，忘记打Service 有空再改
    @Autowired
    private ArticleCategory articleCategory;
    //线程服务注入
    @Autowired
    private ThreadService threadService;


    private final LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();


    /**
     * 文章列表方法
     * @param pageParams 接收前端数据的Param对象，属性一个是页数，一个是页面大小，参数在调用mapper时传入
     * @return 统一响应一个Result类
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         * 分页查询数据库
         */
        //分页的属性
        Page<Article> page=new Page<>(pageParams.getPage(),pageParams.getPageSize());
        //查询条件，比如根据更新时间和是否指定排序 oder by
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //根据传来的分页参数，查看是否是只查看同一类型的所有文章
        if(pageParams.getCategoryId()!=null){
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        //根据传来的分页参数，查看是否是只查看同一标签的的所有文章，根据标签id，查表获取所有文章id，然后查询所有文章
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();

            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());

            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (!articleIdList.isEmpty()){
                queryWrapper.in(Article::getId,articleIdList);
            }
        }
        //是否置顶，同时按照创建日期倒序
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        //开始分页查询
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        //获取查询返回的文章列表
        List<Article> records = articlePage.getRecords();
        //将查询返回的数据做一些适配，做一些类型转换，方便前端页面展示比如将long换成string
        List<ArticleVo> articleVos=copyList(records,true,true,true);

        return Result.success(articleVos);


    }

    /**
     * 获取最热文章
     * @param limit 文章数量限制
     * @return 返回一个Result
     */
    @Override
    public Result hotArticle(int limit) {
        //根据浏览量排序，
        queryWrapper.orderByDesc(Article::getViewCounts);
        //只获取文章id和title
        queryWrapper.select(Article::getId,Article::getTitle);
        //在sql语句结尾拼接limit限制
        queryWrapper.last("limit "+limit);
        //select id,title from ms_article oder by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        // 3. 批量缓存到Redis（一次性操作）
        if (!articles.isEmpty()) {
            // 3.1 创建键值对映射
            Map<String, String> articleMap = new HashMap<>(articles.size());

            // 3.2 使用Jackson序列化
            ObjectMapper objectMapper = new ObjectMapper();

            for (Article article : articles) {
                try {
                    String json = objectMapper.writeValueAsString(article);
                    articleMap.put("hotArticle:" + article.getId(), json);
                } catch (JsonProcessingException e) {
                    log.error("文章序列化失败: {}", article.getId(), e);
                }
            }
            // 3.3 一次性设置多个键值对
            stringRedisTemplate.opsForValue().multiSet(articleMap);

            // 3.4 设置过期时间（可选）
            Duration expireTime = Duration.ofHours(24);
            for (String key : articleMap.keySet()) {
                stringRedisTemplate.expire(key, expireTime);
            }
        }
        return Result.success(copyList(articles,false,false,false));
    }
    /**
     * 获取最新文章
     * @param limit 文章数量限制
     * @return 返回一个Result
     */
    @Override
    public Result newArticle(int limit) {
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,true,true,false));
    }

    /**
     * 文章归档
     * @return 返回一个Result
     */

    @Override
    public Result listArchives() {
        List<Archives> archives=articleMapper.listArchives();
        return Result.success(archives);
    }

    /**
     * 根据文章id获取文章详情
     * @param id
     * @return
     */
    @Override
    public Result findArticleById(Long id) {
        /**
         * 先获取文章article
         * 然后通过bodyId字段获取body
         * 通过categoryId字段获取category
         * 转换成对应的vo类，封装到响应的articleVo类里面
         * 返回封装为Result类的articleVo
         */
        // todo 先查缓存

        //缓存不存在 查数据库
        Article article = articleMapper.selectById(id);
        if(article==null){
            return Result.fail(ErrorCode.ARTICLE_ABSENT.getCode(), ErrorCode.ARTICLE_ABSENT.getMsg());
        }
        /**
         * 查看文章后，这个文章的viewCount要+1,但是直接在这里对数据库写操作，会增加耗时
         * 本来点击一个文章，应该快速响应回去，但是这里有了一个更新操作后，会导致响应速度降低，
         * 所以，这里使用线程池-多线程来优化，将更新操作交给一个线程来处理，主线程继续返回文章
         */
        threadService.updateArticleViewCountThread(article);
        return Result.success(copy(article,true,true,true,true));
    }

    /**
     * 写文章服务
     * @param articleParam 前端写文章页面传回来的文章参数
     * @return 返回Result
     */

    @Override
    public Result articlePublish(ArticleParam articleParam) {
        /**
         * 核心就是根据参数生成Article类，插入到表中，当然还要维护好另外两个关联表的关系
         * 1、将名字相同的属性赋值，然后手动赋值一些值
         * 2、将new好的article插入表中，生成的id值返回，获取这个id返回给前端
         * 3、在表article_body中插入一个记录，这个记录关联了文章id和body
         * 4、在表article_tag中插入articleId和tagId
         */
        Article article=new Article();
        //赋值名字相同的属性
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCommentCounts(0);
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        //设置作者id为当前用户
        article.setAuthorId(UserThreadLocal.get().getId());
        //设置创建时间
        article.setCreateDate(System.currentTimeMillis());
        //先把这个article插入表中，获取自动参数返回的articleId好在后面使用

        articleMapper.insert(article);
        //根据参数的tagVo集合的id,结合文章id,生成tag_article记录插入表中
        List<TagVo> tags = articleParam.getTags();
        Long articleId = article.getId();
        if(tags!=null){
            for (TagVo tag : tags) {
                ArticleTag articleTag=new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        //把ArticleBodyParam的值赋值给ArticleBody,把articleBody插入body表里
        ArticleBody articleBody=new ArticleBody();
        BeanUtils.copyProperties(articleParam.getBody(),articleBody);
        articleBody.setArticleId(articleId);
        articleBodyMapper.insert(articleBody);
        //把前面插入的article的bodyId值更新一下，因为原本的bodyId是null,在插入mapper执行后会自动set这个类的id
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        //返回一个包含了这篇文章Id的Vo对象
        ArticleVo articleVo=new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }

    /**
     * 根据标题模糊匹配文章列表，返回列表
     * @return
     */
    @Override
    public Result findArticlesByTitle(String title) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Article::getTitle,title);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,true,true));
    }

    /**
     * 根据用户id获取对应用户发布的文章
     *
     * @return
     */

    @Override
    public Result userInfoArticleList() {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //获取当前用户id
        Long id = UserThreadLocal.get().getId();
        queryWrapper.eq(Article::getAuthorId,id);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false,false));
    }

    /**
     * 删除用户id对应的所有文章
     * @param id
     */
    @Override
    public void deleteArticleByUserId(String id) {
        //根据id查询所有文章的id
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>().eq(Article::getAuthorId, id));
        //遍历文章id集合，删除文章id对应的文章，body，和所有评论
        for (Article article : articles) {
            //删除文章评论
            commentsService.deleteCommentsIdByArticleId(article.getId().toString());
            //删除文章body
            deleteArticleBodyById(article.getId().toString());
            articleMapper.deleteById(article.getId());
        }
    }

    /**
     * 根据文章id删除对应的文章body
     * @param id
     */
    @Override
    public void deleteArticleBodyById(String id) {
        LambdaQueryWrapper<ArticleBody> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,id);
        articleBodyMapper.delete(queryWrapper);
    }

    /**
     * 根据分类删除文章，同时删除对应的body
     * @param id
     */
    @Override
    public void deleteArticleByCategoryId(String id) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCategoryId,id);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        for (Article article : articles) {
            //删除对应的文章体
            deleteArticleBodyById(article.getId().toString());
            //根据文章id删除文章对应的评论
            commentsService.deleteCommentsIdByArticleId(article.getId().toString());
        }
    }

    /**
     * 根据文章id置顶文章
     * @param id
     */
    @Override
    public void updateWeightById(String id) {
        Article article=articleMapper.selectById(id);
        //获取当前权重
        int top_down=article.getWeight();
        if(top_down==Article.Article_TOP){
            top_down=Article.Article_Common;
        }else{
            top_down=Article.Article_TOP;
        }
        article.setWeight(top_down);
        articleMapper.updateById(article);
    }

    /**
     * 根据文章id删除文章，和body
     * @param id
     */
    @Override
    public void deleteArticleById(String id) {
        deleteArticleBodyById(id);
        articleMapper.deleteById(id);
    }

    /**
     * 更新文章信息
     * @param article
     */
    @Override
    public void updateArticle(Article article) {
        articleMapper.updateById(article);
    }




    /**
     * 调用单个copy方法，循环复制，将Article复制转换为ArticleVo，返回一个list集合
     * @param records 要复制的原本的文章集合
     * @return 复制后的ArticleVo集合
     */
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor ,boolean isCategory) {
        List<ArticleVo> voList=new ArrayList<>();
        for (Article record : records) {
            voList.add(copy(record,isTag,isAuthor,false,isCategory));
        }
        return voList;
    }

    /**
     * 单个复制方法，将Article复制转换为ArticleVo
     * @param article  要复制的article文章对象
     * @param isTag     是否需要复制tag
     * @param isAuthor  是否需要复制作者
     * @return 返回一个复制后的ArticleVo对象
     */
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo=new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //需要tag
        if(isTag){
            //根据article的id，查询tag id 然后通过copyList转换成对应的tagVo
           articleVo.setTags(tagService.findTagsByArticleId(article.getId()));
        }
        //需要作者
        if(isAuthor){
            Long authorId = article.getAuthorId();
            SysUser user = sysUserService.findUserById(authorId);
            if(user==null){
                //保证健壮性，即使没有查询到作者，我们可以设置一个默认的作者，这里默认为重生细胞
                //最近在玩这游戏，这游戏挺好玩
                user=new SysUser();
                user.setNickname("重生细胞");
            }
            articleVo.setAuthor(user.getNickname());
        }
        //需要文章体
        if(isBody){
            articleVo.setBody(articleBodyService.getArticleBodyById(article.getBodyId()));
        }
        //需要文章类型
        if(isCategory){
            articleVo.setCategory(articleCategory.getArticleCategoryById(article.getCategoryId()));
        }
        return articleVo;
    }
}
