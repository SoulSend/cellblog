package org.hrc.backblog.service;


import org.hrc.backblog.vo.CategoryVo;

import java.util.List;

public interface ArticleCategory {

    CategoryVo getArticleCategoryById(Long categoryId);
}
