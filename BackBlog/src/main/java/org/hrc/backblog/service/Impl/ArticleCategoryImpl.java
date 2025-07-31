package org.hrc.backblog.service.Impl;

import org.hrc.backblog.dao.mapper.ArticleCategoryMapper;
import org.hrc.backblog.dao.pojo.Category;
import org.hrc.backblog.service.ArticleCategory;
import org.hrc.backblog.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryImpl implements ArticleCategory {
    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    /**
     * 根据文章分类的id，获取文章分类的实体
     * @param categoryId
     * @return
     */
    @Override
    public CategoryVo getArticleCategoryById(Long categoryId) {
        CategoryVo categoryVo=new CategoryVo();
        Category category = articleCategoryMapper.selectById(categoryId);
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
