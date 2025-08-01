<template>
  <div v-if="visible" class="login-modal-overlay" @click="closeModal">
    <div class="login-modal" @click.stop>
      <!-- 左侧海报区域 -->
      <div class="poster-section">
        <div class="poster-content">
          <img src="@/assets/image/logo.png" alt="Logo" class="poster-logo" />
          <h2 class="poster-title">欢迎来到 CELL</h2>
          <p class="poster-subtitle">分享你的技术见解，连接更多开发者</p>
        </div>
      </div>

      <!-- 右侧表单区域 -->
      <div class="form-section">
        <div class="form-container">
          <button class="close-btn" @click="closeModal">×</button>
          <h2 class="form-title">{{ isLogin ? "登录" : "注册" }}</h2>
          <form @submit.prevent="onSubmit" class="login-form">
            <div class="form-group">
              <label for="username">用户名</label>
              <input
                type="text"
                id="username"
                v-model="form.account"
                required
                placeholder="请输入用户名"
                class="input-field"
              />
            </div>
            <div class="form-group">
              <label for="password">密码</label>
              <input
                type="password"
                id="password"
                v-model="form.password"
                required
                placeholder="请输入密码"
                class="input-field"
              />
            </div>
            <div class="form-group" v-if="!isLogin">
              <label for="confirm-password">确认密码</label>
              <input
                type="password"
                id="confirm-password"
                v-model="form.confirmPassword"
                required
                placeholder="请确认密码"
                class="input-field"
              />
            </div>
            <div class="form-group" v-if="!isLogin">
              <label for="nickname">昵称</label>
              <input
                type="text"
                id="nickname"
                v-model="form.nickname"
                required
                placeholder="请输入昵称"
                class="input-field"
              />
            </div>
            
            <button type="submit" class="btn btn-primary">
              {{ isLogin ? "登录" : "注册" }}
            </button>
            <div class="additional-actions">
              <button type="button" class="btn btn-link" @click="forgotPassword">
                忘记密码?
              </button>
              <button type="button" class="btn btn-link" @click="toggleForm">
                {{ isLogin ? "注册" : "登录" }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import { reactive, ref, defineProps, defineEmits } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/index";

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
      alert("账号或者密码错误");
    }
  } catch (error) {
    console.error("Login error:", error);
    alert("登录失败，请重试");
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
      alert("注册失败，请重试");
    }
  } catch (error) {
    console.error("Registration error:", error);
    alert("注册失败，请重试");
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
        alert("您的账号已被封禁，请联系管理员邮箱：hrc_2525@qq.com");
        store.logout();
      } else {
        store.setUser(userData);
      }
    }
  } catch (error: any) {
    console.error("Fetch user info error:", error);
    if (error.response && error.response.status === 401) {
      alert("Session expired. Please log in again.");
      store.logout();
    } else {
      alert("获取用户信息失败，请重试");
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
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  animation: fadeIn 0.3s ease-out;
}

.login-modal {
  display: flex;
  width: 900px;
  height: 600px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  animation: slideUp 0.3s ease-out;
  position: relative;
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
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 左侧海报区域 */
.poster-section {
  flex: 2;
  background: linear-gradient(135deg, #06bac7 0%, #008db2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.poster-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/image/logo.png') center/cover;
  opacity: 0.1;
  z-index: 1;
}

.poster-content {
  text-align: center;
  color: white;
  z-index: 2;
  position: relative;
}

.poster-logo {
  width: 120px;
  height: auto;
  margin-bottom: 30px;
  filter: brightness(0) invert(1);
}

.poster-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 15px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.poster-subtitle {
  font-size: 16px;
  opacity: 0.9;
  line-height: 1.6;
  max-width: 250px;
  margin: 0 auto;
}

/* 右侧表单区域 */
.form-section {
  flex: 3;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
}

.close-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background-color: #f0f0f0;
  color: #333;
}

.form-container {
  width: 100%;
  max-width: 400px;
}

.form-title {
  text-align: center;
  color: #333;
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
}

.login-form {
  width: 100%;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
  font-size: 14px;
}

.input-field {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  box-sizing: border-box;
  background-color: #fafbfc;
}

.input-field:focus {
  outline: none;
  border-color: #06bac7;
  background-color: white;
  box-shadow: 0 0 0 3px rgba(6, 186, 199, 0.1);
}

.btn {
  display: block;
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #06bac7 0%, #008db2 100%);
  color: white;
  margin-top: 10px;
  margin-bottom: 20px;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(6, 186, 199, 0.3);
}

.btn-primary:active {
  transform: translateY(0);
}

.additional-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.btn-link {
  background: none;
  color: #06bac7;
  text-decoration: none;
  cursor: pointer;
  font-size: 14px;
  padding: 0;
  border: none;
  transition: color 0.3s ease;
}

.btn-link:hover {
  color: #008db2;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-modal {
    width: 95%;
    height: auto;
    flex-direction: column;
    min-height: 600px;
  }
  
  .poster-section {
    flex: none;
    height: 200px;
  }
  
  .form-section {
    flex: none;
    padding: 30px 20px;
  }
  
  .poster-title {
    font-size: 24px;
  }
  
  .form-title {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .login-modal-overlay {
    padding: 20px;
  }
  
  .login-modal {
    width: 100%;
    border-radius: 15px;
  }
  
  .poster-section {
    height: 150px;
  }
  
  .poster-logo {
    width: 80px;
  }
  
  .poster-title {
    font-size: 20px;
  }
  
  .form-section {
    padding: 20px 15px;
  }
}
</style> 