package org.hrc.backblog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录用户的Vo类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String account;

    private String nickname;

    private String avatar;
    private boolean isBan;

}
