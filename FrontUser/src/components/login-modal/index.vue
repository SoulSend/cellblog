<template>
  <Teleport to="body">
    <div v-if="visible" class="login-modal-overlay" @click="closeModal">
      <div class="login-modal" @click.stop>
        <!-- 关闭按钮 -->
        <button class="close-btn" @click="closeModal">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>

        <!-- 左侧海报区域 -->
        <div class="poster-section">
          <div class="poster-content">
            <div class="logo-container">
              <img src="@/assets/image/logo.png" alt="CELL" class="poster-logo" />
            </div>
            <h2 class="poster-title">CELL Blog</h2>
            <p class="poster-subtitle">分享技术，连接世界</p>
            <div class="poster-decoration">
              <div class="decoration-line"></div>
              <div class="decoration-dot"></div>
              <div class="decoration-line"></div>
            </div>
          </div>
        </div>

        <!-- 右侧表单区域 -->
        <div class="form-section">
          <div class="form-container">
            <h2 class="form-title">{{ isLogin ? "欢迎回来" : "加入我们" }}</h2>
            <p class="form-subtitle">{{ isLogin ? "登录您的账户" : "创建您的账户" }}</p>
            
            <form @submit.prevent="onSubmit" class="login-form">
              <div class="form-group">
                <label for="username">用户名</label>
                <div class="input-wrapper">
                  <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  <input
                    type="text"
                    id="username"
                    v-model="form.account"
                    required
                    placeholder="请输入用户名"
                    class="input-field"
                  />
                </div>
              </div>
              
              <div class="form-group">
                <label for="password">密码</label>
                <div class="input-wrapper">
                  <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                    <circle cx="12" cy="16" r="1"></circle>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                  </svg>
                  <input
                    type="password"
                    id="password"
                    v-model="form.password"
                    required
                    placeholder="请输入密码"
                    class="input-field"
                  />
                </div>
              </div>
              
              <div class="form-group" v-if="!isLogin">
                <label for="confirm-password">确认密码</label>
                <div class="input-wrapper">
                  <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                    <circle cx="12" cy="16" r="1"></circle>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                  </svg>
                  <input
                    type="password"
                    id="confirm-password"
                    v-model="form.confirmPassword"
                    required
                    placeholder="请确认密码"
                    class="input-field"
                  />
                </div>
              </div>
              
              <div class="form-group" v-if="!isLogin">
                <label for="nickname">昵称</label>
                <div class="input-wrapper">
                  <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  <input
                    type="text"
                    id="nickname"
                    v-model="form.nickname"
                    required
                    placeholder="请输入昵称"
                    class="input-field"
                  />
                </div>
              </div>
              
              <button type="submit" class="btn btn-primary">
                <span class="btn-text">{{ isLogin ? "登录" : "注册" }}</span>
                <svg class="btn-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                  <polyline points="12,5 19,12 12,19"></polyline>
                </svg>
              </button>
              
              <div class="form-actions">
                <button type="button" class="btn btn-link" @click="forgotPassword" v-if="isLogin">
                  忘记密码?
                </button>
                <button type="button" class="btn btn-link" @click="toggleForm">
                  {{ isLogin ? "没有账户? 注册" : "已有账户? 登录" }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import axios from "axios";
import { reactive, ref, defineProps, defineEmits } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/index";
import { toast } from '@/utils/toast';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['close', 'login-success']);

const router = useRouter();
const isLogin = ref(true);
const form = reactive({
  account: "",
  password: "",
  nickname: "",
  confirmPassword: "",
});

const store = useAuthStore();

function closeModal() {
  emit('close');
}

function onSubmit() {
  if (isLogin.value) {
    login();
  } else {
    register();
  }
}

async function login() {
  try {
    const response = await axios.post("http://localhost:8888/login", {
      account: form.account,
      password: form.password,
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (response.data.code === 200) {
      let token = response.data.data;
      localStorage.setItem("token_CellBlog", token);
      store.login(token);
      store.setUser(response.data.data);
      await fetchUserInfo();
      emit('login-success');
      closeModal();
    } else {
      toast.error("账号或者密码错误");
    }
  } catch (error) {
    console.error("Login error:", error);
    toast.error("登录失败，请重试");
  }
}

async function register() {
  try {
    const response = await axios.post("http://localhost:8888/register", {
      account: form.account,
      password: form.password,
      nickname: form.nickname,
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (response.data.code === 200) {
      let token = response.data.data;
      localStorage.setItem("token_CellBlog", token);
      store.login(token);
      store.setUser(response.data.data);
      await fetchUserInfo();
      emit('login-success');
      closeModal();
    } else {
      toast.error("注册失败，请重试");
    }
  } catch (error) {
    console.error("Registration error:", error);
    toast.error("注册失败，请重试");
  }
}

async function fetchUserInfo() {
  try {
    const token = localStorage.getItem("token_CellBlog");
    if (!token) {
      throw new Error("Token not found. Please log in again.");
    }
    const response = await axios.get("http://localhost:8888/users/currentUser", {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    if (response.data.code === 200) {
      const userData = response.data.data;
      if (userData.ban) {
        toast.error("您的账号已被封禁，请联系管理员邮箱：hrc_2525@qq.com");
        store.logout();
      } else {
        store.setUser(userData);
      }
    }
  } catch (error: any) {
    console.error("Fetch user info error:", error);
    if (error.response && error.response.status === 401) {
      toast.warning("Session expired. Please log in again.");
      store.logout();
    } else {
      toast.error("获取用户信息失败，请重试");
    }
  }
}

function forgotPassword() {
  // Implement forgot password logic here
}

function toggleForm() {
  isLogin.value = !isLogin.value;
}
</script>

<style scoped>
.login-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  animation: fadeIn 0.3s ease-out;
}

.login-modal {
  display: flex;
  width: 900px;
  height: 600px;
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-xl);
  animation: slideUp 0.3s ease-out;
  position: relative;
  border: 1px solid var(--border-light);
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 关闭按钮 */
.close-btn {
  position: absolute;
  top: var(--space-4);
  right: var(--space-4);
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  color: var(--text-secondary);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-fast);
  z-index: 10;
}

.close-btn:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
  transform: scale(1.1);
}

/* 左侧海报区域 */
.poster-section {
  flex: 1;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-hover) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.poster-content {
  text-align: center;
  color: white;
  z-index: 2;
  position: relative;
  padding: var(--space-6);
}

.logo-container {
  margin-bottom: var(--space-6);
}

.poster-logo {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-lg);
  background: rgba(255, 255, 255, 0.1);
  padding: var(--space-3);
  backdrop-filter: blur(10px);
}

.poster-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: var(--space-3);
  letter-spacing: -0.5px;
}

.poster-subtitle {
  font-size: 1rem;
  opacity: 0.9;
  line-height: 1.6;
  margin-bottom: var(--space-6);
}

.poster-decoration {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
}

.decoration-line {
  width: 40px;
  height: 1px;
  background: rgba(255, 255, 255, 0.3);
}

.decoration-dot {
  width: 4px;
  height: 4px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
}

/* 右侧表单区域 */
.form-section {
  flex: 1.5;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-8);
  background: var(--bg-primary);
}

.form-container {
  width: 100%;
  max-width: 400px;
}

.form-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--space-2);
  text-align: center;
}

.form-subtitle {
  font-size: 0.875rem;
  color: var(--text-secondary);
  text-align: center;
  margin-bottom: var(--space-8);
}

.login-form {
  width: 100%;
}

.form-group {
  margin-bottom: var(--space-4);
}

.form-group label {
  display: block;
  margin-bottom: var(--space-2);
  color: var(--text-primary);
  font-weight: 500;
  font-size: 0.875rem;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: var(--space-3);
  color: var(--text-tertiary);
  z-index: 1;
}

.input-field {
  width: 100%;
  padding: var(--space-3) var(--space-3) var(--space-3) calc(var(--space-3) + 20px + var(--space-2));
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  transition: all var(--transition-fast);
  background: var(--bg-primary);
  color: var(--text-primary);
}

.input-field:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(6, 186, 199, 0.1);
}

.input-field::placeholder {
  color: var(--text-tertiary);
}

.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  width: 100%;
  padding: var(--space-3) var(--space-4);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 600;
  transition: all var(--transition-fast);
  text-decoration: none;
}

.btn-primary {
  background: var(--primary-color);
  color: white;
  margin-top: var(--space-6);
  margin-bottom: var(--space-4);
}

.btn-primary:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.btn-primary:active {
  transform: translateY(0);
}

.btn-icon {
  transition: transform var(--transition-fast);
}

.btn-primary:hover .btn-icon {
  transform: translateX(2px);
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--space-4);
}

.btn-link {
  background: none;
  color: var(--primary-color);
  text-decoration: none;
  cursor: pointer;
  font-size: 0.875rem;
  padding: 0;
  border: none;
  transition: color var(--transition-fast);
}

.btn-link:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-modal {
    width: 95%;
    height: auto;
    flex-direction: column;
    min-height: 600px;
    margin: var(--space-4);
  }
  
  .poster-section {
    flex: none;
    height: 200px;
  }
  
  .form-section {
    flex: none;
    padding: var(--space-6) var(--space-4);
  }
  
  .poster-title {
    font-size: 1.5rem;
  }
  
  .form-title {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .login-modal-overlay {
    padding: var(--space-4);
  }
  
  .login-modal {
    width: 100%;
    border-radius: var(--radius-lg);
  }
  
  .poster-section {
    height: 150px;
  }
  
  .poster-logo {
    width: 60px;
    height: 60px;
  }
  
  .poster-title {
    font-size: 1.25rem;
  }
  
  .form-section {
    padding: var(--space-4) var(--space-3);
  }
}
</style> 