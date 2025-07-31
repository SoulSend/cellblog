<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo">
        <img src="@/assets/image/logo.png" alt="Logo" />
      </div>
      <form @submit.prevent="onSubmit">
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
</template>

<script setup lang="ts">
import axios from "axios";
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/index";

const router = useRouter();
const isLogin = ref(true); // Track whether we are on login or register
const form = reactive({
  account: "",
  password: "",
  nickname: "", // Only needed for registration
  confirmPassword: "", // Only needed for registration
});

const store = useAuthStore();

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
      store.login( token);
      store.setUser(response.data.data)
      router.replace("/home/index");
      await fetchUserInfo(); // 移动到登录成功后调用
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
      store.login( token);
      store.setUser(response.data.data) 
      router.replace("/home/index");
      await fetchUserInfo(); // 移动到注册成功后调用
    } else {
      alert("注册失败，请重试");
    }
  } catch (error) {
    console.error("Registration error:", error);
    alert("注册失败，请重试");
  }
}

// 修改 fetchUserInfo 方法
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
      // 检查账号是否被封禁
      if (userData.ban) {
        alert("您的账号已被封禁，请联系管理员邮箱：hrc_2525@qq.com");
        store.logout();
      } else {
        store.setUser(userData);
      }
    }
  } catch (error:any) {
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
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(#e6f7ff, #ffffff);
}

.login-card {
  width: 350px; /* Adjusted width to create a 1:2 rectangle */
  padding: 2rem;
  background: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

.logo {
  text-align: center;
  margin-bottom: 2rem;
}

.logo img {
  width: 100px;
  height: auto;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.input-field {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  transition: all 0.3s ease;
}

.btn {
  display: block;
  width: 100%;
  padding: 0.75rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.btn-primary {
  background-color: #36b4f3;
  color: white;
  margin-top: 1rem;
}

.btn-primary:hover {
  background-color: #2ca9f7;
}

.additional-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
}

.btn-link {
  background: none;
  color: #0056b3;
  text-decoration: none;
  cursor: pointer;
  flex-grow: 1;
  text-align: center;
}

.btn-link:first-child {
  text-align: left;
}

.btn-link:last-child {
  text-align: right;
}

.btn-link:hover {
  text-decoration: underline;
}

/* Responsive adjustments */
@media (max-width: 400px) {
  .login-card {
    width: 100%;
    padding: 1rem;
  }

  .form-group label {
    font-size: 0.9rem;
  }

  .input-field {
    padding: 0.5rem;
    font-size: 0.9rem;
  }

  .btn {
    padding: 0.5rem;
    font-size: 0.9rem;
  }
}
</style>