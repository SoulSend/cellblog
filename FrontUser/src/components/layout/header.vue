<template>
  <header class="navbar">
    <div class="logo-container">
      <img src="@/assets/image/logo.png" alt="logo" class="logo-image" />
      <div class="logo">CELL</div>
    </div>
    <nav class="nav-links">
      <ul>
        <li v-for="link in navLinks" :key="link.text">
          <RouterLink :to="link.to" class="nav-link">{{ link.text }}</RouterLink>
        </li>
      </ul>
    </nav>
    
    <div class="write-article-box">
      <a href="#" class="btn" @click.prevent="goToWriteArticle">写文章</a>
    </div>
    <div class="login-register-box">
      <button v-if="!isAuthenticated" @click="showLoginModal = true" class="btn">登录/注册</button>
      <div v-else class="user-profile" @click.stop="toggleDropdown">
        <img :src="user.avatar" alt="user avatar" class="avatar">
        <span>{{ user.nickname }}</span>
        <ul v-show="showDropdown" class="dropdown">
          <li @click.stop="goToProfile">个人信息</li>
          <li @click.stop="logout">注销</li>
        </ul>
      </div>
    </div>

    <!-- 登录模态框 -->
    <LoginModal 
      :visible="showLoginModal" 
      @close="showLoginModal = false"
      @login-success="handleLoginSuccess"
    />

    <!-- 登录提示警告 -->
    <div v-if="showLoginWarning" class="login-warning" @click="hideLoginWarning">
      <div class="warning-content">
        <div class="warning-icon">⚠️</div>
        <div class="warning-text">未登录不能写文章哦~~~</div>
        <button class="warning-close" @click.stop="hideLoginWarning">×</button>
      </div>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { onMounted, onBeforeUnmount, ref, computed } from 'vue';
import { RouterLink, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/index";
import LoginModal from '@/components/login-modal/index.vue';


const router = useRouter();
const store = useAuthStore();
const isAuthenticated = computed(() => store.userIsAuthenticated);
const user = computed(() => store.getUser);
const showDropdown = ref(false);
const showLoginWarning = ref(false);
const showLoginModal = ref(false);

const navLinks = [
  { to: "/home/index/前端", text: "首页" },
  //{ to: "/home/cellai", text: "AI" },
  { to: "/home/activity", text: "活动" },
  { to: "/home/search", text: "发现" },
];

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const logout = async () => {
  console.log("触发")
  await store.logout();
  router.push("/login");
  showDropdown.value = false;
};

const goToWriteArticle = () => {
  if (!isAuthenticated.value) {
    showLoginWarning.value = true;
    // 3秒后自动隐藏
    setTimeout(() => {
      showLoginWarning.value = false;
    }, 3000);
  } else {
    router.push("/WriteArticle");
  }
};

const hideLoginWarning = () => {
  showLoginWarning.value = false;
};

const handleLoginSuccess = () => {
  showLoginModal.value = false;
  // 登录成功后可以添加其他逻辑
};

const goToProfile = () => {
  router.push("/profile");
  showDropdown.value = false;
};

let handleClickOutsideHandler;

const handleClickOutside = (event: any) => {
  if (!(event.target instanceof HTMLElement)) {
    return;
  }
  
  const dropdownElement = event.target.closest('.dropdown');
  const userElement = event.target.closest('.user-profile');

  if (userElement && showDropdown.value) {
    // 如果点击的是头像，且下拉框已显示，则不关闭下拉框
    return;
  }

  if (!dropdownElement && !userElement) {
    showDropdown.value = false;
  }
};

handleClickOutsideHandler = handleClickOutside;

onMounted(() => {
  document.addEventListener('click', handleClickOutsideHandler);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutsideHandler);
});
</script>

<style scoped>
.navbar {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  position: sticky;
  top: 0;
  z-index: 10;
  font-family: 'Arial', sans-serif; /* 统一字体 */
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo-image {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #24292e;
}

.nav-links ul {
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
}

.nav-links ul li {
  margin-right: 20px;
}

.nav-link {
  color: black;
  text-decoration: none;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: rgb(7, 187, 203);
}

.nav-link:active {
  color: #005a71;
}

.write-article-box,
.login-register-box {
  display: flex;
  align-items: center;
  margin-left: 20px; /* 添加盒子间的间距 */
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  text-decoration: none;
  color: #fff;
  background-color: #06bac7;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn:hover {
  background-color: #008db2;
  transform: translateY(-2px); /* 添加hover效果 */
}

.btn:active {
  background-color: #005a71;
  transform: translateY(2px); /* 添加active效果 */
}

.user-profile {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  background-color: #06bac7;
}

.dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  border-radius: 8px;
  padding: 10px 0;
  z-index: 1000;
  width: 150px;
  list-style: none;
  margin: 0;
  opacity: 1;
  transform: translateY(20px);
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.dropdown li {
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.dropdown li:hover {
  background-color: #e0e0e0;
}

.dropdown li:not(:last-child) {
  border-bottom: 1px solid #e0e0e0;
}

/* 当下拉框显示时 */
.dropdown[v-show="showDropdown"] {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
}

/* 登录警告样式 */
.login-warning {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2000;
  animation: warningSlideIn 0.3s ease-out;
}

.warning-content {
  background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
  color: white;
  padding: 20px 30px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(255, 107, 107, 0.3);
  display: flex;
  align-items: center;
  gap: 15px;
  min-width: 300px;
  position: relative;
}

.warning-icon {
  font-size: 24px;
  animation: pulse 2s infinite;
}

.warning-text {
  font-size: 16px;
  font-weight: 500;
  flex-grow: 1;
}

.warning-close {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.warning-close:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

@keyframes warningSlideIn {
  from {
    opacity: 0;
    transform: translate(-50%, -60%);
  }
  to {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-links ul {
    display: none; /* 隐藏导航链接 */
  }

  .write-article-box,
  .login-register-box {
    flex-grow: 1;
    justify-content: space-around;
  }
}
</style>