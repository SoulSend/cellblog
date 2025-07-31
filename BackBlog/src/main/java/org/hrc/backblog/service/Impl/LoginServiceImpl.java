package org.hrc.backblog.service.Impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hrc.backblog.dao.pojo.Admin;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.AdminService;
import org.hrc.backblog.service.LoginService;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.utils.JwtUtil;
import org.hrc.backblog.vo.ErrorCode;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.AdminLoginParam;
import org.hrc.backblog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService userService;
    @Autowired
    private AdminService adminService;
    /**
     * 加密盐
     */
    private final String slat="CellBlog!@#";
    /**
     * 登录服务
     * @param loginParam 请求参数
     * @return 统一Result
     */
    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1、检查参数是否合法
         * 2、根据账户和密码去user中查询是否存在用户，查询时使用MD5+加密盐，对密码进行加密，因为注册时数据库中的密码就是加密后的
         * 3、如果没有用户，登录失败
         * 4、如果用户存在且密码正确，生成token并返回给前端
         * 5、token放入redis中，存储user:token，减少数据库访问，直接获取对应user信息，设置过期时间(暂时不实现)
         * 6、登录认证的时候，先认证token是否合法，再却redis中确认token是否存在
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        //密码存在，对密码加密后，再去和数据库比对
        password= DigestUtils.md5Hex(password+slat);
        //比对
        SysUser sysUser=userService.findUser(account,password);
        //找到没有，没找到报错，找到了返回token
        if(sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JwtUtil.createToken(sysUser.getId().toString());
        //放入redis(暂时不实现)

        return Result.success(token);
    }

    /**
     * 注册服务
     * @param loginParam 请求参数
     * @return 统一Result
     */
    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1、先判断参数合法性
         * 2、判断用户是否已经存在
         * 3、不存在，注册用户
         * 4、生成token并返回
         * 5、注册服务添加事务，保证原子性
         */
        String account = loginParam.getAccount();
        String nickname = loginParam.getNickname();
        String password = loginParam.getPassword();

        if(account==null||nickname==null||password==null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser=userService.findUserByAccount(account);
        //账户存在 返回
        if(sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //账户不存在，将这些属性放入用户表中
        sysUser=new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAdmin(1);
        sysUser.setAvatar("http://localhost:8888/img/userAvatar.png");
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        userService.save(sysUser);
        //保存好了，生成token返回客户端
        String token = JwtUtil.createToken(sysUser.getId().toString());

        return Result.success(token);
    }

    /**
     * 管理员登录
     * @param adminLoginParam
     * @return
     */
    @Override
    public Result adminLogin(AdminLoginParam adminLoginParam) {

        String username = adminLoginParam.getUsername();
        String password = adminLoginParam.getPassword();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        //根据密码和账户查找用户，然后比对
        Admin admin=adminService.checkAdmin(username,password);
        //找到没有，没找到报错，找到了返回token
        if(admin==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JwtUtil.createToken(admin.getUsername());
        //放入redis(暂时不实现)

        return Result.success(token);
    }

}
