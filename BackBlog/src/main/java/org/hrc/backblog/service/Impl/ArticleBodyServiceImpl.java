package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.hrc.backblog.dao.mapper.ArticleBodyMapper;
import org.hrc.backblog.dao.pojo.ArticleBody;
import org.hrc.backblog.service.ArticleBodyService;
import org.hrc.backblog.vo.ArticleBodyVo;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.ArticleBodyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {
    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    /**
     * 根据文章体的id获取文章体，
     * 然后转换成文章体Vo类返回
     * @param id 文章题id
     * @return 返回一个文章提VO
     */
    @Override
    public ArticleBodyVo getArticleBodyById(Long id) {
        if(id==null){
            return null;
        }
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        return copy(articleBody);
    }

    /**
     * 根据文章id修改文章体
     * @param articleBodyParamParam
     */
    @Override
    public void updateArticleBodyById(ArticleBodyParam articleBodyParamParam) {
        Long articleId = articleBodyParamParam.getArticleId();
        LambdaQueryWrapper<ArticleBody> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        articleBody.setContent(articleBodyParamParam.getContent());
        articleBody.setContentHtml(articleBodyParamParam.getContentHtml());
        articleBodyMapper.updateById(articleBody);
    }

    /**
     * 根据文章id获取文章体
     * @param aLong
     * @return
     */
    @Override
    public ArticleBodyVo getArticleBodyByArticleId(Long aLong) {
        LambdaQueryWrapper<ArticleBody> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,aLong);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);

        return copy(articleBody);
    }

    /**
     * 复制转换方法
     * @param articleBody 要复制的文章题对象
     * @return 返回赋值后的Vo实体
     */
    public ArticleBodyVo copy(ArticleBody articleBody){
        ArticleBodyVo vo=new ArticleBodyVo();
        vo.setContent(articleBody.getContent());
        vo.setContentHtml(articleBody.getContentHtml());
        return vo;
    }
}
