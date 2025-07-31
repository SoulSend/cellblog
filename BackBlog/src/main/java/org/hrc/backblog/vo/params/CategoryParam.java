package org.hrc.backblog.vo.params;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CategoryParam {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String avatar;
    private String categoryName;
    private String description;
}
