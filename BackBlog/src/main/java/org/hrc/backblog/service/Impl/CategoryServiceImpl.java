package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.hrc.backblog.dao.mapper.CategoryMapper;
import org.hrc.backblog.dao.pojo.Category;
import org.hrc.backblog.service.CategoryService;
import org.hrc.backblog.vo.CategoryVo;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.CategoryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询所有分类，但只查询id和name字段
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }

    /**
     * 查询所有类型的具体属性
     * @return
     */

    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(null);
        return Result.success(copyList(categories));
    }

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    @Override
    public Result findCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    /**
     * 根据id更新分类的信息
     * @param category
     */
    @Override
    public void updateById(Category category) {
        categoryMapper.updateById(category);
    }

    /**
     * 根据id删除对应的分类
     * @param id
     */
    @Override
    public void deleteById(String id) {
        categoryMapper.deleteById(id);
    }
    /**
     * 添加一个分类
     */
    @Override
    public Result addCategory(CategoryParam categoryParam) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryParam,category);
        categoryMapper.insert(category);
        return Result.success(null);
    }


    public List<CategoryVo> copyList(List<Category> categories){
        List<CategoryVo> categoryVos=new ArrayList<>();
        for (Category category : categories) {
            categoryVos.add(copy(category));
        }
        return categoryVos;
    }
    public CategoryVo copy(Category category){
        CategoryVo categoryVo=new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
