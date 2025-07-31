package org.hrc.backblog.service;

import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.UserVo;
import org.hrc.backblog.vo.params.ModifyUserParam;

public interface SysUserService {
    public SysUser findUserById(Long authorId);

    SysUser findUser(String account, String password);

    Result getUserInfoByToken(String token,boolean isDetail);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo getUserVoById(Long toUid);

    Result changeUserInfo(ModifyUserParam modifyUserParam);

    Result findAll();

    Result delete(String id);

    Result ban(Long id);


    Result findUserByUsername(String username);
}
