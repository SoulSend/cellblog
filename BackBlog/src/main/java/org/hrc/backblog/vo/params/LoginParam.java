package org.hrc.backblog.vo.params;

import lombok.Data;

/**
 * 登录和注册参数实体类
 */
@Data
public class LoginParam {
    private String account;
    private String password;
    private String nickname;
}
