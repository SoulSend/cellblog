package org.hrc.backblog.dao.dos;

import lombok.Data;

/**
 * 文章归档用到的实体类
 */
@Data
public class Archives {
    private Integer year;
    private Integer month;
    private Integer count;
}
