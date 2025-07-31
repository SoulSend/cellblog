// src/axios.ts
import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8888', 
  timeout: 10000, // 增加超时时间
});

// 添加请求拦截器
instance.interceptors.request.use(
  config => {
    // 检查请求的URL是否是特定的登录路径
    if (config.url !== 'http://localhost:8888/login') {
      const token = localStorage.getItem('token_CellBlog');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
    }
    return config;
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 添加响应拦截器
instance.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response;
  },
  error => {
    // 对响应错误做点什么
    if (error.response && error.response.status === 401) {
      // 如果状态码是 401，表示用户未认证，可能需要重新登录
      console.log('Unauthorized: Please login again');
      // 这里可以添加重定向到登录页面的逻辑
    }
    return Promise.reject(error);
  }
);

export default instance;