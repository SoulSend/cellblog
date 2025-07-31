package org.hrc.backblog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hrc.backblog.dao.pojo.Tag;

import java.util.List;

/**
 * 标签mapper
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据id查询tag列表
     * @param articleId
     * @return
     */
     List<Tag> findTagsByArticleId(long articleId);

    /**
     *根据文章标签的id集合查询对应标签
     * @param tagIds 文章标签集合
     * @return 返回一个文章标签
     */
    List<Tag> findHostsTagByTagIds(List<Long> tagIds);
    /**
     * 根据标签id查询最热的标签id，就是文章数最多的标签的id
     * @param limit 要查询的最热tag数
     * @return 返回一个tag的id集合
     */
    List<Long> findHotsTagIds(int limit);
}
