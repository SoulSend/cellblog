package org.hrc.backblog.service;

import org.hrc.backblog.vo.Result;
import org.hrc.backblog.vo.params.CommentParam;

import java.util.List;

public interface CommentsService {
    Result getCommentsByArticleId(Long articleId);

    Result comment(CommentParam commentParam);

    Result list();

    Result delete(Long id);

    Result userCommentList(Long id);

    Result reported();



    void deleteCommentsIdByUserId(String id);

    void deleteCommentsIdByArticleId(String aid);


    Result report(String id);
}
