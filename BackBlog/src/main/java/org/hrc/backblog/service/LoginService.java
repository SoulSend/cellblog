package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.AdminLoginParam;
import org.hrc.backblog.vo.params.LoginParam;

public interface LoginService {
    Result login(LoginParam loginParam);


    Result register(LoginParam loginParam);

    Result adminLogin(AdminLoginParam loginParam);
}
