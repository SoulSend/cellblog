<template>
  <div class="activity-detail" v-if="activity">
    <h1>{{ activity.title }}</h1>
    <img :src="activity.poster" alt="活动海报" class="poster">
    <div class="detail-content" v-html="activity.detailIntroduction">
    </div>
    <div class="activity-info">
      <p><strong>简介：</strong>{{ activity.description }}</p>
      <p><strong>发布者：</strong>{{ activity.publisher }}</p>
      <p><strong>时间：</strong>{{ formatDate(activity.startTime) }} - {{ formatDate(activity.endTime) }}</p>
      <p><strong>参与者：</strong>{{ activity.participants }} 人</p>
      <p><strong>浏览量：</strong>{{ activity.views }} 次</p>
      <p v-if="activity.participationUrl">
        <strong>参与方式：</strong>
        <a :href="activity.participationUrl" target="_blank">点击参与</a>
      </p>
    </div>
  </div>
  <div v-else>
    活动详情未找到或加载中...
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

interface Activity {
  id: number;
  title: string;
  description: string;
  poster: string;
  publisher: string;
  startTime: string;
  endTime: string;
  participants: number;
  views: number;
  detailIntroduction: string;
  participationUrl: string;
}

export default defineComponent({
  name: 'ActivityDetail',
  setup() {
    const activity = ref<Activity | null>(null);
    const route = useRoute();

    const fetchActivity = async () => {
      try {
        const response = await axios.get(`http://localhost:8888/activity/detail/${route.params.id}`);
        activity.value = response.data.data;
      } catch (error) {
        console.error('Error fetching activity:', error);
      }
    };

    const formatDate = (dateString: string) => {
      const date = new Date(dateString);
      return date.toISOString().split('T')[0]; // 只取日期部分
    };

    onMounted(fetchActivity);

    return {
      activity,
      formatDate,
    };
  },
});
</script>

<style scoped>
.activity-detail {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9f9;
}

.activity-detail h1 {
  color: #333;
  margin-bottom: 20px;
}

.poster {
  width: 100%;
  height: auto;
  margin-bottom: 20px;
}

.detail-content {
  margin-bottom: 20px;
}

.activity-info p {
  margin: 10px 0;
  color: #666;
  font-size: 16px;
}

.activity-info a {
  color: #007bff;
  text-decoration: none;
}

.activity-info a:hover {
  text-decoration: underline;
}
</style>