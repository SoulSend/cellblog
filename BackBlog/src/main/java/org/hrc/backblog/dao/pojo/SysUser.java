package org.hrc.backblog.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class SysUser {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String account;

    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhone;

    private String nickname;

    private String password;

    private String salt;

    private String status;
    private boolean isBan;
}
