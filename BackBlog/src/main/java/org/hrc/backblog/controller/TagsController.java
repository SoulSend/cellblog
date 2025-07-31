package org.hrc.backblog.controller;

import org.hrc.backblog.service.TagService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private TagService tagService;
    /**
     * 获取所有标签
     */
    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }
    /**
     * 查询最热门的标签
     * @return 返回一个Result
     */
    @GetMapping("/hot")
    public Result hot(){
        int limit=6;
        List<TagVo> tagVoList = tagService.hot(limit);
        return Result.success(tagVoList);
    }
    /**
     * 标签列表
     */
    @GetMapping("/detail")
    public Result tagsDetail(){
        return tagService.findAllDetail();
    }
    /**
     * 标签导航
     */
    @GetMapping("/detail/{id}")
    public  Result findDetailById(@PathVariable("id")  Long id){
        return tagService.findDetailById(id);
    }
}
