<template>
    <div class="common-layout">
      <el-container>
        <el-header class="header-container">
            <Header/>
        </el-header>
        <el-main class="main-container">
            <RouterView v-slot="{ Component }">
              <transition name="fade" mode="out-in">
                <component :is="Component" />
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
.header-container {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: white;
  border-bottom: 1px solid #e1e5e9;
}

.main-container {
  min-height: calc(100vh - 60px);
  background: #f8f9fa;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>