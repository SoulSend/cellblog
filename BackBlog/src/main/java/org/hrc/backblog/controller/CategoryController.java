package org.hrc.backblog.controller;

import org.hrc.backblog.service.CategoryService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.CategoryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表
     * @return
     */
    @GetMapping
    public Result listCategory(){
       return categoryService.findAll();
    }
    /**
     * 添加分类
     */
    @PostMapping("/addCategory")
    public Result addCategory(@RequestBody CategoryParam categoryParam){
        return categoryService.addCategory(categoryParam);
    }
    /**
     * 分类导航
     * @return
     */
    @GetMapping("/detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }
    /**
     *单个分类导航
     */
    @GetMapping("/detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.findCategoryById(id);
    }

}
