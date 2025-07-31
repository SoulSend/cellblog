package org.hrc.backblog.dao.pojo;

import lombok.Data;
import java.util.Date;

/**
 * 活动实体类
 */
@Data
public class Activity {


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