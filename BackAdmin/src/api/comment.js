// src/api/comment.js
import request from '@/utils/request';

// 获取评论列表
export function fetchComments(params) {
  return request({
    url: '/comments/list',
    method: 'get',
    params
  });
}

// 删除评论
export function deleteComment(id) {
  return request({
    url: `/comments/delete/${id}`,
    method: 'delete'
  });
}


// 根据文章ID获取评论
export function fetchCommentsByArticleId(articleId) {
    return request({
      url: `/comments/article/${articleId}`,
      method: 'get'
    });
  }
  
  // 根据用户ID获取评论
  export function fetchCommentsByUserId(userId) {
    return request({
      url: `/comments/user/commentList/${userId}`,
      method: 'get'
    });
  }
  
  // 获取被举报的评论
  export function fetchReportedComments() {
    return request({
      url: '/comments/reported',
      method: 'get'
    });
  }
  