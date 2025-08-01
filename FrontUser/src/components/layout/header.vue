<template>
  <header class="navbar">
    <div class="navbar-container">
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo-wrapper">
          <img src="@/assets/image/logo.png" alt="CELL" class="logo-image" />
          <div class="logo-text">
            <span class="logo-primary">CELL</span>
            <span class="logo-subtitle">Blog</span>
          </div>
        </div>
      </div>

      <!-- 导航链接 -->
      <nav class="nav-section">
        <ul class="nav-list">
          <li v-for="link in navLinks" :key="link.text" class="nav-item">
            <RouterLink :to="link.to" class="nav-link" :class="{ 'active': isActiveRoute(link.to) }">
              <svg class="nav-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path v-if="link.icon === 'home'" d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                <polyline v-if="link.icon === 'activity'" points="22,12 18,12 15,21 9,3 6,12 2,12"></polyline>
                <circle v-if="link.icon === 'search'" cx="11" cy="11" r="8"></circle>
                <path v-if="link.icon === 'search'" d="M21 21l-4.35-4.35"></path>
              </svg>
              <span class="nav-text">{{ link.text }}</span>
            </RouterLink>
          </li>
        </ul>
      </nav>

      <!-- 右侧操作区 -->
      <div class="actions-section">
        <!-- 写文章按钮 -->
        <button class="write-btn" @click="goToWriteArticle">
          <svg class="write-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
          </svg>
          <span class="write-text">写文章</span>
        </button>

        <!-- 用户区域 -->
        <div v-if="!isAuthenticated" class="auth-section">
          <button class="login-btn" @click="showLoginModal = true">
            <svg class="login-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"></path>
              <polyline points="10,17 15,12 10,7"></polyline>
              <line x1="15" y1="12" x2="3" y2="12"></line>
            </svg>
            <span>登录</span>
          </button>
        </div>
        
        <div v-else class="user-section" @click.stop="toggleDropdown">
          <div class="user-avatar">
            <img :src="user.avatar" alt="用户头像" class="avatar-image" />
            <div class="avatar-status"></div>
          </div>
          <span class="user-name">{{ user.nickname }}</span>
          <svg class="dropdown-arrow" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="6,9 12,15 18,9"></polyline>
          </svg>
          
          <!-- 用户下拉菜单 -->
          <div v-show="showDropdown" class="user-dropdown">
            <div class="dropdown-header">
              <img :src="user.avatar" alt="用户头像" class="dropdown-avatar" />
              <div class="dropdown-user-info">
                <div class="dropdown-name">{{ user.nickname }}</div>
                <div class="dropdown-email">{{ user.email || '用户' }}</div>
              </div>
            </div>
            <div class="dropdown-divider"></div>
            <ul class="dropdown-menu">
              <li class="dropdown-item" @click.stop="goToProfile">
                <svg class="dropdown-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
                <span>个人信息</span>
              </li>
              <li class="dropdown-item" @click.stop="logout">
                <svg class="dropdown-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
                  <polyline points="16,17 21,12 16,7"></polyline>
                  <line x1="21" y1="12" x2="9" y2="12"></line>
                </svg>
                <span>退出登录</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <!-- 登录模态框 -->
    <LoginModal 
      :visible="showLoginModal" 
      @close="showLoginModal = false"
      @login-success="handleLoginSuccess"
    />

    <!-- 登录提示 -->
    <div v-if="showLoginWarning" class="login-warning" @click="hideLoginWarning">
      <div class="warning-content">
        <svg class="warning-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
          <line x1="12" y1="9" x2="12" y2="13"></line>
          <line x1="12" y1="17" x2="12.01" y2="17"></line>
        </svg>
        <div class="warning-text">请先登录后再写文章</div>
        <button class="warning-close" @click.stop="hideLoginWarning">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
      </div>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { onMounted, onBeforeUnmount, ref, computed } from 'vue';
import { RouterLink, useRouter, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/index";
import LoginModal from '@/components/login-modal/index.vue';

const router = useRouter();
const route = useRoute();
const store = useAuthStore();
const isAuthenticated = computed(() => store.userIsAuthenticated);
const user = computed(() => store.getUser);
const showDropdown = ref(false);
const showLoginWarning = ref(false);
const showLoginModal = ref(false);

const navLinks = [
  { to: "/home", text: "首页", icon: "home" },
  { to: "/home/activity", text: "活动", icon: "activity" },
  { to: "/home/search", text: "发现", icon: "search" },
];

// 检查当前路由是否激活
const isActiveRoute = (path: string) => {
  if (path === "/home") {
    return route.path === '/home';
  }
  return route.path === path;
};

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const logout = async () => {
  await store.logout();
  showDropdown.value = false;
};

const goToWriteArticle = () => {
  if (!isAuthenticated.value) {
    showLoginWarning.value = true;
    setTimeout(() => {
      showLoginWarning.value = false;
    }, 3000);
  } else {
    router.push("/write");
  }
};

const hideLoginWarning = () => {
  showLoginWarning.value = false;
};

const handleLoginSuccess = () => {
  showLoginModal.value = false;
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
  
  const dropdownElement = event.target.closest('.user-dropdown');
  const userElement = event.target.closest('.user-section');

  if (userElement && showDropdown.value) {
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
  width: 100%;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-light);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--space-6);
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

/* Logo区域 */
.logo-section {
  flex-shrink: 0;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  cursor: pointer;
  transition: transform var(--transition-fast);
}

.logo-wrapper:hover {
  transform: scale(1.05);
}

.logo-image {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
}

.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1;
}

.logo-primary {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
  letter-spacing: -0.5px;
}

.logo-subtitle {
  font-size: 0.75rem;
  color: var(--text-secondary);
  font-weight: 500;
}

/* 导航区域 */
.nav-section {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: var(--space-2);
}

.nav-item {
  position: relative;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
  font-weight: 500;
  position: relative;
  overflow: hidden;
}

.nav-link::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--primary-light);
  opacity: 0;
  transition: opacity var(--transition-fast);
  border-radius: var(--radius-lg);
}

.nav-link:hover::before {
  opacity: 1;
}

.nav-link:hover {
  color: var(--primary-color);
  transform: translateY(-1px);
}

.nav-link.active {
  color: var(--primary-color) !important;
  background: var(--primary-light);
  font-weight: 600;
  position: relative;
  z-index: 1;
  box-shadow: 0 0 0 1px var(--primary-color);
}

.nav-link.active::before {
  opacity: 0;
}

.nav-link.active .nav-icon {
  color: var(--primary-color) !important;
}

.nav-link.active .nav-text {
  color: var(--primary-color) !important;
  font-weight: 600;
}

.nav-icon {
  flex-shrink: 0;
  transition: transform var(--transition-fast);
}

.nav-link:hover .nav-icon {
  transform: scale(1.1);
}

.nav-text {
  font-size: 0.875rem;
}

/* 操作区域 */
.actions-section {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

/* 写文章按钮 */
.write-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-lg);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.write-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.write-icon {
  flex-shrink: 0;
}

.write-text {
  font-size: 0.875rem;
}

/* 登录按钮 */
.login-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  background: var(--bg-primary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.login-btn:hover {
  background: var(--bg-secondary);
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.login-icon {
  flex-shrink: 0;
}

/* 用户区域 */
.user-section {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  cursor: pointer;
  padding: var(--space-2);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
  position: relative;
}

.user-section:hover {
  background: var(--bg-secondary);
}

.user-avatar {
  position: relative;
}

.avatar-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--border-light);
}

.avatar-status {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: var(--success-color);
  border-radius: 50%;
  border: 2px solid white;
}

.user-name {
  font-weight: 500;
  color: var(--text-primary);
  font-size: 0.875rem;
}

.dropdown-arrow {
  flex-shrink: 0;
  color: var(--text-tertiary);
  transition: transform var(--transition-fast);
}

.user-section:hover .dropdown-arrow {
  transform: rotate(180deg);
}

/* 用户下拉菜单 */
.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: var(--space-2);
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xl);
  border: 1px solid var(--border-light);
  min-width: 240px;
  z-index: 1000;
  animation: dropdownSlideIn 0.2s ease-out;
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.dropdown-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.dropdown-user-info {
  flex: 1;
}

.dropdown-name {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.875rem;
}

.dropdown-email {
  color: var(--text-secondary);
  font-size: 0.75rem;
  margin-top: var(--space-1);
}

.dropdown-divider {
  height: 1px;
  background: var(--border-light);
  margin: var(--space-2) 0;
}

.dropdown-menu {
  list-style: none;
  margin: 0;
  padding: var(--space-2);
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  color: var(--text-primary);
  cursor: pointer;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  font-size: 0.875rem;
}

.dropdown-item:hover {
  background: var(--bg-secondary);
  color: var(--primary-color);
}

.dropdown-icon {
  flex-shrink: 0;
}

@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 登录警告 */
.login-warning {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2000;
  animation: warningSlideIn 0.3s ease-out;
}

.warning-content {
  background: var(--warning-color);
  color: white;
  padding: var(--space-6);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  display: flex;
  align-items: center;
  gap: var(--space-4);
  min-width: 320px;
  position: relative;
}

.warning-icon {
  flex-shrink: 0;
  animation: pulse 2s infinite;
}

.warning-text {
  font-size: 1rem;
  font-weight: 500;
  flex-grow: 1;
}

.warning-close {
  background: none;
  border: none;
  color: white;
  font-size: 1.25rem;
  cursor: pointer;
  padding: var(--space-1);
  border-radius: 50%;
  transition: background-color var(--transition-fast);
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
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
  .navbar-container {
    padding: 0 var(--space-4);
  }
  
  .nav-text {
    display: none;
  }
  
  .write-text {
    display: none;
  }
  
  .user-name {
    display: none;
  }
  
  .dropdown-arrow {
    display: none;
  }
}

@media (max-width: 480px) {
  .navbar-container {
    padding: 0 var(--space-3);
  }
  
  .nav-section {
    display: none;
  }
  
  .logo-subtitle {
    display: none;
  }
}
</style>