package org.hrc.backblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.hrc.backblog.dao.mapper.AdminMapper;
import org.hrc.backblog.dao.pojo.Admin;
import org.hrc.backblog.dao.pojo.Article;
import org.hrc.backblog.dao.pojo.ArticleBody;
import org.hrc.backblog.dao.pojo.Category;
import org.hrc.backblog.service.*;
import org.hrc.backblog.utils.JwtUtil;
import org.hrc.backblog.vo.AdminVo;
import org.hrc.backblog.vo.ArticleBodyVo;
import org.hrc.backblog.vo.ErrorCode;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.AdminArticleParam;
import org.hrc.backblog.vo.params.ArticleBodyParam;
import org.hrc.backblog.vo.params.CategoryParam;
import org.hrc.backblog.vo.params.ModifyUserParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员服务
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyService articleBodyService;

    /**
     * 加密盐
     */
    private final String slat="CellBlog!@#";

    /**
     * 登录校验调用，检查数据库中账户密码一致的管理员
     * @param username
     * @param password
     * @return
     */
    @Override
    public Admin checkAdmin(String username,String password) {
        LambdaQueryWrapper<Admin> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        queryWrapper.eq(Admin::getPassword,password);
        queryWrapper.last("limit 1");
        return adminMapper.selectOne(queryWrapper);
    }

    /**
     * 根据token获取用户的信息，token负载用户的username
     * @param token
     * @return
     */
    @Override
    public Result userInfo(String token) {
        /**
         * 解析token，获取token负载的username
         * 根据username获取info
         */
        String username = JwtUtil.parseToken(token);
        LambdaQueryWrapper<Admin> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        Admin admin = adminMapper.selectOne(queryWrapper);
        return Result.success(copy(admin));
    }

    /**
     * 根据用户id删除用户对应的评论和文章还有用户的数据
     * @param id
     * @return
     */
    @Override
    public Result delete(String id) {
        //先删除用户id对应的所有文章
        articleService.deleteArticleByUserId(id);
        //再删除用户id对应的所有评论
        commentsService.deleteCommentsIdByUserId(id);
        //最后删除该用户的信息
        return sysUserService.delete(id);
    }

    /**
     * 管理员修改用户的密码
     * @param modifyUserParam
     * @return
     */
    @Override
    public Result changePassword(ModifyUserParam modifyUserParam) {
        //获取新密码
        String newPassword = modifyUserParam.getNewPassword();
        //加密
        modifyUserParam.setPassword(DigestUtils.md5Hex(newPassword+slat));
        //覆盖
        sysUserService.changeUserInfo(modifyUserParam);
        return Result.success(null);
    }

    /**
     * 更新文章分类的信息
     * @param categoryParam
     * @return
     */
    @Override
    public Result updateCategory(CategoryParam categoryParam) {
        Long id = categoryParam.getId();
        if(id==null){
            return  Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //将参数类转换成实体类，然后更新信息
        Category category=new Category();
        BeanUtils.copyProperties(categoryParam,category);
        categoryService.updateById(category);
        return Result.success(null);
    }

    /**
     * 删除id对应的分类和分类关联的所有文章
     * @param id
     * @return
     */
    @Override
    public Result deleteCategory(String id) {
        //删除分类id对应的所有文章,在删除文章时还要删除对应的body
        articleService.deleteArticleByCategoryId(id);
        //然后再删除文章分类
        categoryService.deleteById(id);
        return Result.success(null);
    }

    /**
     * 根据文章id置顶文章
     * @param id
     * @return
     */
    @Override
    public Result top(String id) {
        /**
         * 根据文章id 获取文章，然后修改置顶字段，再保存
         */
        articleService.updateWeightById(id);
        return Result.success(null);
    }

    /**
     * 根据文章id删除文章 同时删除对应评论
     * @param id
     * @return
     */
    @Override
    public Result deleteArticle(String id) {
        //先根据文章id删除对应文章评论
        commentsService.deleteCommentsIdByArticleId(id);
        //然后删除文章和对应id的body
        articleService.deleteArticleById(id);
        return Result.success(null);
    }
    /**
     * 更新文章信息
     * @param adminArticleParam
     * @return
     */
    @Override
    public Result updateArticle(AdminArticleParam adminArticleParam) {
        Article article=new Article();
        BeanUtils.copyProperties(adminArticleParam,article);
        articleService.updateArticle(article);
        return Result.success(null);
    }

    /**
     * 根据文章id获取文章体
     * @param id
     * @return
     */
    @Override
    public Result getArticleBodyById(String id) {
        return Result.success(articleBodyService.getArticleBodyByArticleId(Long.valueOf(id)));
    }

    /**
     * 根据文章id修改文章body
     * @param articleBodyParamParam
     * @return
     */
    @Override
    public Result updateArticleBodyById(ArticleBodyParam articleBodyParamParam) {
        articleBodyService.updateArticleBodyById(articleBodyParamParam);
        return Result.success(null);
    }

    private AdminVo copy(Admin admin){
        AdminVo adminVo=new AdminVo();
        //复制相同字段
        BeanUtils.copyProperties(admin,adminVo);
        //不同字段暂时没有，暂时没有复制的逻辑
        return adminVo;
    }
}
