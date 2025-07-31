<template>
    <div class="activity-list">
      <h1>活动列表</h1>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error">加载失败: {{ error }}</div>
      <div v-else-if="activities.length === 0">没有活动数据。</div>
      <div v-else class="grid">
        <div v-for="activity in activities" :key="activity.id" class="activity-card" @click="goToDetail(activity.id)">
          <div class="poster-container">
            <img :src="activity.poster" alt="活动海报" class="poster">
          </div>
          <div class="info-container">
            <h2 class="activity-title">{{ activity.title }}</h2>
            <p class="activity-description">{{ activity.description }}</p>
            <div class="meta">
              <div>发布者：{{ activity.publisher }}</div>
              <div>时间：{{ formatDate(activity.startTime) }} 至 {{ formatDate(activity.endTime) }}</div>
              <div>浏览量：{{ activity.views }} 次</div>
            </div>
            <button class="join-button">报名参加</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref } from 'vue';
  import axios from 'axios';
  import { useRouter } from 'vue-router';
  
  interface Activity {
    id: number;
    title: string;
    description: string;
    poster: string;
    publisher: string;
    startTime: string; // 活动开始时间
    endTime: string; // 活动结束时间
    views: number; // 活动浏览量
  }
  
  export default defineComponent({
    name: 'ActivityList',
    setup() {
      const activities = ref<Array<Activity>>([]);
      const loading = ref(false);
      const error = ref('');
      const router = useRouter();
  
      const fetchActivities = async () => {
        loading.value = true;
        error.value = '';
        try {
          const response = await axios.get('http://localhost:8888/activity');
          activities.value = response.data.data;
        } catch (err: any) {
          error.value = err.message;
        } finally {
          loading.value = false;
        }
      };
  
      const goToDetail = (id: number) => {
        router.push({ path: `/home/ActivityDetail/${id}` });
      };
  
      const formatDate = (isoDateString: string) => {
        const date = new Date(isoDateString);
        return date.toISOString().split('T')[0]; // 只取日期部分
      };
  
      fetchActivities();
  
      return {
        activities,
        loading,
        error,
        goToDetail,
        formatDate,
      };
    },
  });
  </script>

<style scoped>
.activity-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.activity-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease;
  cursor: pointer;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: flex-end; /* 将内容推到卡片顶部 */
  min-height: 300px; /* 设置最小高度以确保按钮有空间 */
}

.activity-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.poster-container {
  height: 150px;
}

.poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-container {
  padding: 20px; /* 增加内边距 */
  background-color: #fff;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.activity-title {
  margin: 0 0 10px 0;
  color: #333;
}

.activity-description {
  margin: 0 0 10px 0;
  color: #666;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.meta {
  display: block; /* 确保每个信息项都占据一行 */
  font-size: 14px;
  color: #999;
}

.meta div {
  margin-bottom: 5px; /* 为每个信息项添加底部间距 */
}

.join-button {
  background-color: #06bac7;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 5px;
  align-self: flex-end;
  margin-top: 10px; /* 与上方内容保持一定距离 */
}

.join-button:hover {
  background-color: #16adc8;
}
</style>