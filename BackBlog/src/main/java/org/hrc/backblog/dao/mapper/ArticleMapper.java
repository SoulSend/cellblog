package org.hrc.backblog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hrc.backblog.dao.dos.Archives;
import org.hrc.backblog.dao.pojo.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章mapper
 */

public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 文章归档 根据时间戳 解析出来具体的年月日 然后统计日期内发布的文章数
     * @return
     */
    List<Archives> listArchives();
}
