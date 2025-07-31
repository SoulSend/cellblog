package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.hrc.backblog.dao.mapper.SysUserMapper;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.ArticleService;
import org.hrc.backblog.service.CommentsService;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.service.TokenService;
import org.hrc.backblog.vo.*;
import org.hrc.backblog.vo.params.ModifyUserParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 对于数据库的操作，要加上事务，
 * 保证操作的原子性，也就是要么成功添加，
 * 要么失败，数据库里什么都没有
 * 这里将@Transactional事务注解放到实现类这里，
 * 一般都放到接口，那样通用一点
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 根据作者id查询用户
     * 关联文章表的作者id，通过作者id查询到这个用户·信息
     * @param authorId 文章作者的id
     * @return 返回这个id对应的用户对象
     */
    @Override
    public SysUser findUserById(Long authorId) {
        return sysUserMapper.selectById(authorId);
    }

    /**
     * 根据用户账户和密码，获取用户信息
     * @param account 账户
     * @param password 密码
     * @return sysUser用户
     */
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getNickname,SysUser::getAvatar);
        //添加一个限制，查到一条记录就结束查询，提高效率
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 根据token获取用户基本信息
     * @param token token参数
     * @return 包含了用户基本信息的result
     */
    @Override
    public Result getUserInfoByToken(String token ,boolean isDetail) {
        //解析token，根据token负载的用户id查询用户的信息
        SysUser sysUser = tokenService.checkToken(token);
        if (sysUser==null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        if (isDetail){
            UserInfoVo userInfoVo=new UserInfoVo();
            BeanUtils.copyProperties(sysUser,userInfoVo);
            return Result.success((userInfoVo));
        }
        LoginUserVo loginUserVo=new LoginUserVo(sysUser.getId(),sysUser.getAccount(),sysUser.getNickname(),sysUser.getAvatar(),sysUser.isBan());
        return Result.success(loginUserVo);
    }

    /**
     * 根据用户账户查询用户
     * @param account 用户账户
     * @return 返回一个用户或者null
     */

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * 将传入的用户添加到数据库中
     * 默认生成的id 是分布式id 采用了雪花算法
     * @param sysUser  用户
     */
    @Override
    public void save(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }

    /**
     * 根据用户id查询用户，返回一个userVo，
     * 用于评论列表展示用户头像和昵称
     * @param toUid
     * @return
     */
    @Override
    public UserVo getUserVoById(Long toUid) {
        UserVo userVo=new UserVo();
        SysUser sysUser = sysUserMapper.selectById(toUid);
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    /**
     * 修改用户的个人信息
     * @param modifyUserParam
     * @return
     */
    @Override
    public Result changeUserInfo(ModifyUserParam modifyUserParam) {
        /**
         * 根据传入的信息转换成SysUser
         * 更新 user
         */

        SysUser user=new SysUser();
        BeanUtils.copyProperties(modifyUserParam,user);

        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getId,user.getId());
        sysUserMapper.update(user,queryWrapper);
        return Result.success(null);
    }

    /**
     * 获取所有用户的信息
     * @return
     */
    @Override
    public Result findAll() {
        List<SysUser> sysUsers = sysUserMapper.selectList(null);
        return Result.success(sysUsers);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @Override
    public Result delete(String id) {
        sysUserMapper.deleteById(id);
        return Result.success(null);
    }

    /**
     * 根据id禁用用户
     * @param id
     * @return
     */
    @Override
    public Result ban(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        sysUser.setBan(!sysUser.isBan());
        sysUserMapper.updateById(sysUser);
        return Result.success(null);
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public Result findUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(SysUser::getNickname,username);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        return Result.success(sysUsers);
    }
}
