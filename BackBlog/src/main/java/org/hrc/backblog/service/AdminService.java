package org.hrc.backblog.service;


import org.hrc.backblog.dao.pojo.Admin;
import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.AdminArticleParam;
import org.hrc.backblog.vo.params.ArticleBodyParam;
import org.hrc.backblog.vo.params.CategoryParam;
import org.hrc.backblog.vo.params.ModifyUserParam;

public interface AdminService {
    Admin checkAdmin(String username,String password);

    Result userInfo(String token);

    Result delete(String id);


    Result changePassword(ModifyUserParam modifyUserParam);

    Result updateCategory(CategoryParam categoryParam);

    Result deleteCategory(String id);

    Result top(String id);

    Result deleteArticle(String id);

    Result updateArticle(AdminArticleParam adminArticleParam);

    Result getArticleBodyById(String id);

    Result updateArticleBodyById(ArticleBodyParam articleBodyParamParam);
}
