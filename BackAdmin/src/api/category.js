import request from '@/utils/request'

// 获取所有分类
export function fetchCategories() {
  return request({
    url: '/categorys/detail',
    method: 'get'
  });
}

// 更新分类
export function updateCategory(category) {
  return request({
    url: '/admin/updateCategory',
    method: 'post',
    data: category
  });
}

// 删除分类
export function deleteCategory(id) {
  return request({
    url: `/admin/deleteCategory/${id}`,
    method: 'delete'
  });
}

// 添加分类
export function addCategory(category) {
  return request({
    url: '/categorys/addCategory',
    method: 'post',
    data: category
  });
}