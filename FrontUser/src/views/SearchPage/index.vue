<template>
  <div class="search-box">
    <input type="text" placeholder="Search..." v-model="searchQuery" />
    <button @click="handleSearch">Search</button>
  </div>
  <div>
    <!-- 确保传递 searchQuery 和 page 给子组件，并监听子组件的 page 更新事件 -->
    <SearchResultsList :searchQuery="searchQuery" :page="page" @update:page="handlePageChange" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import SearchResultsList from '@/components/SearchResultsList/index.vue';

const searchQuery = ref('');
const page = ref(1); // 添加一个页码状态

const handleSearch = () => {
  // 重置页码为1
  page.value = 1;
};

const handlePageChange = (newPage: number) => {
  // 更新父组件的页码状态
  page.value = newPage;
};
</script>


<style scoped>
.search-box {
  margin: 50px auto; /* 居中并设置上下边距 */
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: center;
  width: 100%;
  max-width: 800px; /* 限制最大宽度 */
  background: transparent; /* 透明背景 */
  padding: 10px; /* 内边距 */
  border-radius: 24px; /* 圆角边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.search-box input[type="text"] {
  flex-grow: 1; /* 输入框占据剩余空间 */
  padding: 15px 20px;
  border: 2px solid #00b894; /* 青色边框 */
  border-radius: 24px; /* 圆角边框 */
  font-size: 16px;
  color: #333;
  outline: none; /* 移除聚焦时的轮廓线 */
  background-color: rgba(255, 255, 255, 0.8); /* 半透明背景 */
  backdrop-filter: blur(10px); /* 添加背景模糊效果 */
}

.search-box input[type="text"]:focus {
  border-color: #00b894; /* 输入框聚焦时边框颜色 */
}

.search-box button {
  padding: 15px 25px;
  margin-left: 10px; /* 与输入框间隔 */
  border: none;
  border-radius: 24px; /* 圆角边框 */
  background-color: #00b894; /* 青色背景 */
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.search-box button:hover {
  background-color: #009578; /* 按钮悬停颜色 */
  transform: scale(1.05); /* 按钮悬停时放大 */
}

.search-box button:active {
  transform: scale(0.95); /* 按钮按下时缩小 */
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-box {
    padding: 15px;
    flex-direction: column;
    align-items: stretch;
  }

  .search-box input[type="text"] {
    width: 100%; /* 输入框宽度为100% */
    margin-bottom: 10px; /* 输入框与按钮间隔 */
  }

  .search-box button {
    width: 100%; /* 按钮宽度为100% */
  }
}

@media (max-width: 480px) {
  .search-box input[type="text"] {
    padding: 12px 15px;
  }

  .search-box button {
    padding: 12px 15px;
  }
}
</style>