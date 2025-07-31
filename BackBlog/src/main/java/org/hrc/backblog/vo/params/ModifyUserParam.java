package org.hrc.backblog.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ModifyUserParam {
    private Long id;
    private String nickname;
    private String avatar;
    private String account;
    private Long createDate;
    private String email;
    private String password;
    private String newPassword;
    private String mobilePhone;
}
