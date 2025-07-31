import request from '@/utils/request'

// 获取文章列表
export function fetchArticles(title) {
  return request({
    url: '/search',
    method: 'get',
    params: { title }
  });
}

// 更新文章信息
export function updateArticleInfo(article) {
  return request({
    url: '/admin/updateArticle',
    method: 'post',
    data: article
  });
}

// 获取文章内容
export function getArticleContent(id) {
  return request({
    url: `/admin/getArticleBody/${id}`,
    method: 'get'
  });
}

// 更新文章内容
export function updateArticleContent(articleContent) {
  return request({
    url: '/admin/updateArticleBodyById',
    method: 'post',
    data: articleContent
  });
}

// 置顶或取消置顶文章
export function toggleTopArticle(id) {
  return request({
    url: `/admin/top/${id}`,
    method: 'put'
  });
}

// 删除文章
export function deleteArticle(id) {
  return request({
    url: `/admin/deleteArticle/${id}`,
    method: 'delete'
  });
}