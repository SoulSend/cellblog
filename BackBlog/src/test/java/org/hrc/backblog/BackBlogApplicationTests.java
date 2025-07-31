package org.hrc.backblog;

import org.apache.commons.codec.digest.DigestUtils;
import org.hrc.backblog.service.ArticleService;
import org.hrc.backblog.service.CommentsService;
import org.hrc.backblog.service.Impl.CommentsServiceImpl;
import org.hrc.backblog.service.Impl.SysUserServiceImpl;
import org.hrc.backblog.service.SysUserService;
import org.hrc.backblog.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackBlogApplicationTests {
    @Autowired
    CommentsService commentsService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    ArticleService articleService;
    @Test
    void jetUtilTest(){
        String token = JwtUtil.createToken("123");
        System.out.println(token);
        System.out.println(JwtUtil.parseToken(token));
    }
    @Test
    void md5HexTest(){
        String s = DigestUtils.md5Hex("huge666"+"CellBlog!@#");
        System.out.println(s);
    }
    @Test
    void articleTest(){
        String s="1861021653528293378";
        articleService.deleteArticleByUserId(s);
    }
}
