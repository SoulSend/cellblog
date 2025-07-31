package org.hrc.backblog.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.hrc.backblog.dao.mapper.SysUserMapper;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.TokenService;
import org.hrc.backblog.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 对token进行合法检验，是否为空，是否有错
     * @param token 参数token
     * @return 返回token对应的用户
     */
    @Override
    public SysUser checkToken(String token) {

        if (StringUtils.isBlank(token)){
            return null;
        }

        String userId = JwtUtil.parseToken(token);

        if (userId==null){
            return null;
        }
        return sysUserMapper.selectById(userId);
    }
}
