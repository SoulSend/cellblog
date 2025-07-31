package org.hrc.backblog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 文章分类的vo
 */
@Data
public class CategoryVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String avatar;
    private String categoryName;
    private String description;
}
