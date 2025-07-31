import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login/admin',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/admin/userinfo',
    method: 'get',
    params: { token }
  })
}

// export function logout() {
//   return request({
//     url: '/vue-admin-template/user/logout',
//     method: 'post'
//   })
// }

// 获取用户信息，根据用户名搜索
export function fetchUsers(username) {
  return request({
    url: `/search/user`,
    method: 'get',
    params: { username }
  });
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/admin/delete/${id}`,
    method: 'delete'
  });
}

// 封禁用户
export function banUser(id) {
  return request({
    url: `/admin/ban/${id}`,
    method: 'put'
  });
}

// 修改用户信息
export function updateUser(user) {
  return request({
    url: '/admin/changeUserInfo',
    method: 'post',
    data: user
  });
}
// 修改用户密码
export function changePassword(user) {
  return request({
    url: '/admin/changePassword',
    method: 'post',
    data: user
  });
}


