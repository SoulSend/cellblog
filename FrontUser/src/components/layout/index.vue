<template>
    <div class="layout">
      <el-container>
        <el-header class="header">
            <Header/>
        </el-header>
        <el-main class="main">
            <RouterView v-slot="{ Component }">
              <transition name="page" mode="out-in">
                <div :key="$route.path" class="page-container">
                  <component :is="Component" />
                </div>
              </transition>
            </RouterView>
        </el-main>
      </el-container>
    </div>
</template>

  
<script setup lang="js">
import Header from './header.vue';
//home 布局页，下面挂载头部的导航区...和路由指向的展示页
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--bg-tertiary) 100%);
}

.header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
  padding: 0;
  height: auto;
  min-height: 70px;
  display: flex;
  align-items: center;
}

.main {
  min-height: calc(100vh - 70px);
  padding: var(--space-6) 0;
  background: transparent;
}

.page-container {
  width: 100%;
  height: 100%;
  animation: fadeIn 0.3s ease-out;
}

/* 页面切换动画 */
.page-enter-active,
.page-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.page-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

.page-enter-to,
.page-leave-from {
  opacity: 1;
  transform: translateX(0);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main {
    padding: var(--space-4) 0;
  }
  
  .header {
    min-height: 60px;
  }
}

@media (max-width: 480px) {
  .main {
    padding: var(--space-3) 0;
  }
}
</style>