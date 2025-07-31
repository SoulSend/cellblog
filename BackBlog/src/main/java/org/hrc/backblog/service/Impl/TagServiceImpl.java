package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.hrc.backblog.dao.mapper.TagMapper;
import org.hrc.backblog.dao.pojo.Category;
import org.hrc.backblog.dao.pojo.Tag;
import org.hrc.backblog.service.TagService;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     * 根据文章id获取文章下标签列表
     * @param articleId 文章id
     * @return 统一返回值
     */
    @Override
    public List<TagVo> findTagsByArticleId(long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }


    /**
     * 最热标签
     * 标签下文章数量最多的标签就是最热标签
     * @param limit 查询的标签数量
     * @return 返回一个tagVO的列表
     */
    @Override
    public List<TagVo> hot(int limit) {
        List<Long> hotsTagIds=tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(hotsTagIds)){
            return Collections.emptyList();
        }
        List<Tag> hotsTag = tagMapper.findHostsTagByTagIds(hotsTagIds);
        return  copyList(hotsTag);
    }

    /**
     * 获取所有标签
     * @return
     */

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    /**
     * 所有标签导航
     * @return
     */
    @Override
    public Result findAllDetail() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }

    /**
     * 单个标签导航
     * @param id
     * @return
     */
    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    /**
     * 同样是Vo复制方法
     * @param tag 要复制的对象
     * @return 返回一个复制后的VO对象
     */
    //转换tag为tagVo
    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

}
