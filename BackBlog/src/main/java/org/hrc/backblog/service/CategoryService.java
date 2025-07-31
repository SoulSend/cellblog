package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.Category;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.CategoryParam;

public interface CategoryService {
    Result findAll();

    Result findAllDetail();

    Result findCategoryById(Long id);

    void updateById(Category category);

    void deleteById(String id);

    Result addCategory(CategoryParam categoryParam);

}
