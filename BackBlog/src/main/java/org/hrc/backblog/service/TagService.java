package org.hrc.backblog.service;

import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.TagVo;

import java.util.List;

public interface TagService {
    public List<TagVo> findTagsByArticleId(long articleId);


    List<TagVo> hot(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);

}
