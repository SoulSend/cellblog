<template>
    <div class="common-layout">
      <el-container>
        <el-header>
            <Header/>
        </el-header>
        <el-main>
            <RouterView></RouterView>
        </el-main>
      </el-container>
    </div>
</template>

  
<script setup lang="js">
import Header from './header.vue';
//home 布局页，下面挂载头部的导航区...和路由指向的展示页
</script>

<style scoped>

</style>