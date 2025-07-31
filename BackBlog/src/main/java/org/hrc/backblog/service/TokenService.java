package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.SysUser;

public interface TokenService {
    SysUser checkToken(String token);
}
