package org.hrc.backblog.vo;

import lombok.Data;

import java.util.Date;

/**
 * 活动实体类对应vo
 */
@Data
public class ActivityVo {
    private Integer id;


    private String title;


    private String description;


    private String poster;


    private String publisher;


    private Date startTime;


    private Date endTime;

    private Integer participants;


    private Integer views;


    private String detailIntroduction;

    private String participationUrl;
}
