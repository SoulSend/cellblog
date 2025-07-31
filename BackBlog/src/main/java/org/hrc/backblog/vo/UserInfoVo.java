package org.hrc.backblog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserInfoVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String nickname;
    private String avatar;
    private String account;
    private Long createDate;
    private String email;
    private String mobilePhone;
}
