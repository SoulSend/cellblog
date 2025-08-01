<template>
  <!-- 加载状态 -->
  <div v-if="loading" class="loading-state">
    <div class="loading-spinner"></div>
    <p class="loading-text">正在加载活动详情...</p>
  </div>

  <!-- 错误状态 -->
  <div v-else-if="error" class="error-state">
    <svg class="error-icon" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <circle cx="12" cy="12" r="10"></circle>
      <line x1="15" y1="9" x2="9" y2="15"></line>
      <line x1="9" y1="9" x2="15" y2="15"></line>
    </svg>
    <h3 class="error-title">加载失败</h3>
    <p class="error-message">{{ error }}</p>
    <button class="retry-btn" @click="fetchActivity">
      <svg class="retry-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="23,4 23,10 17,10"></polyline>
        <polyline points="1,20 1,14 7,14"></polyline>
        <path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15"></path>
      </svg>
      重新加载
    </button>
  </div>

  <!-- 活动详情内容 -->
  <div v-else-if="activity" class="activity-detail">
    <!-- 返回按钮 -->
    <div class="back-section">
      <button class="back-btn" @click="goBack">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="15,18 9,12 15,6"></polyline>
        </svg>
        返回活动列表
      </button>
    </div>

    <!-- 活动主体 -->
    <div class="activity-main">
      <!-- 活动海报 -->
      <div class="poster-section">
        <img :src="activity.poster" alt="活动海报" class="activity-poster" />
      </div>

      <!-- 活动信息 -->
      <div class="info-section">
        <div class="info-header">
          <h1 class="activity-title">{{ activity.title }}</h1>
          <p class="activity-subtitle">{{ activity.description }}</p>
        </div>

        <div class="info-grid">
          <div class="info-card">
            <div class="info-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
              </svg>
            </div>
            <div class="info-content">
              <span class="info-label">发布者</span>
              <span class="info-value">{{ activity.publisher }}</span>
            </div>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                <line x1="16" y1="2" x2="16" y2="6"></line>
                <line x1="8" y1="2" x2="8" y2="6"></line>
                <line x1="3" y1="10" x2="21" y2="10"></line>
              </svg>
            </div>
            <div class="info-content">
              <span class="info-label">活动时间</span>
              <span class="info-value">{{ formatDate(activity.startTime) }} - {{ formatDate(activity.endTime) }}</span>
            </div>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                <circle cx="8.5" cy="7" r="4"></circle>
                <line x1="20" y1="8" x2="20" y2="14"></line>
                <line x1="23" y1="11" x2="17" y2="11"></line>
              </svg>
            </div>
            <div class="info-content">
              <span class="info-label">参与者</span>
              <span class="info-value">{{ activity.participants }} 人</span>
            </div>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
              </svg>
            </div>
            <div class="info-content">
              <span class="info-label">浏览量</span>
              <span class="info-value">{{ activity.views }} 次</span>
            </div>
          </div>
        </div>

        <!-- 参与按钮 -->
        <div v-if="activity.participationUrl" class="participation-section">
          <button class="join-btn" @click="joinActivity">
            <svg class="join-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="8.5" cy="7" r="4"></circle>
              <line x1="20" y1="8" x2="20" y2="14"></line>
              <line x1="23" y1="11" x2="17" y2="11"></line>
            </svg>
            立即参与活动
          </button>
        </div>
      </div>
    </div>

    <!-- 活动详情 -->
    <div class="detail-section">
      <div class="detail-header">
        <h2 class="detail-title">
          <svg class="detail-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14,2 14,8 20,8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10,9 9,9 8,9"></polyline>
          </svg>
          活动详情
        </h2>
      </div>
      <div class="detail-content" v-html="activity.detailIntroduction"></div>
    </div>
  </div>

  <!-- 未找到状态 -->
  <div v-else class="not-found-state">
    <svg class="not-found-icon" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <circle cx="11" cy="11" r="8"></circle>
      <path d="M21 21l-4.35-4.35"></path>
    </svg>
    <h3 class="not-found-title">活动未找到</h3>
    <p class="not-found-message">抱歉，该活动可能已被删除或不存在</p>
    <button class="back-home-btn" @click="goBack">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="15,18 9,12 15,6"></polyline>
      </svg>
      返回活动列表
    </button>
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
    const loading = ref(true);
    const error = ref<string | null>(null);

    const fetchActivity = async () => {
      try {
        loading.value = true;
        error.value = null;
        const response = await axios.get(`http://localhost:8888/activity/detail/${route.params.id}`);
        activity.value = response.data.data;
      } catch (err) {
        error.value = 'Failed to fetch activity details.';
        console.error('Error fetching activity:', err);
      } finally {
        loading.value = false;
      }
    };

    const formatDate = (dateString: string) => {
      const date = new Date(dateString);
      return date.toISOString().split('T')[0]; // 只取日期部分
    };



    const joinActivity = () => {
      if (activity.value?.participationUrl) {
        window.open(activity.value.participationUrl, '_blank');
      }
    };

    const goBack = () => {
      history.back();
    };

    onMounted(fetchActivity);

    return {
      activity,
      loading,
      error,
      formatDate,
      joinActivity,
      goBack,
    };
  },
});
</script>

<style scoped>
.activity-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-6);
  animation: fadeIn 0.5s ease-out;
}

.loading-state, .error-state, .not-found-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
  color: #555;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-icon, .not-found-icon {
  color: #e74c3c;
  margin-bottom: 20px;
}

.error-title, .not-found-title {
  color: #333;
  margin-bottom: 10px;
}

.error-message, .not-found-message {
  margin-bottom: 30px;
  color: #666;
}

.retry-btn, .back-home-btn {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-size: 16px;
}

.retry-btn:hover, .back-home-btn:hover {
  background-color: #0056b3;
}

.retry-icon, .back-home-icon {
  margin-right: 8px;
  width: 20px;
  height: 20px;
}



.back-section {
  margin-bottom: 20px;
}

.back-btn {
  display: flex;
  align-items: center;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 14px;
  text-decoration: none;
  border: 2px solid var(--border-color);
  transition: all 0.3s ease;
}

.back-btn:hover {
  background-color: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.back-btn svg {
  margin-right: 8px;
  width: 16px;
  height: 16px;
}

.activity-main {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-8);
  margin-bottom: var(--space-8);
}

.poster-section {
  position: relative;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  background: var(--bg-primary);
}

.activity-poster {
  width: 100%;
  height: 400px;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.activity-poster:hover {
  transform: scale(1.02);
}

.info-section {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-8);
  box-shadow: var(--shadow-lg);
  border: 1px solid var(--border-light);
}

.info-header {
  margin-bottom: var(--space-6);
  text-align: center;
}

.activity-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--space-3);
  line-height: 1.2;
}

.activity-subtitle {
  font-size: 1.125rem;
  color: var(--text-secondary);
  line-height: 1.6;
}

.info-header {
  margin-bottom: 20px;
  text-align: center;
}

.activity-title {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.activity-subtitle {
  font-size: 18px;
  color: #666;
  line-height: 1.6;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--space-4);
  margin-bottom: var(--space-6);
}

.info-card {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  background: linear-gradient(135deg, var(--bg-secondary), var(--bg-tertiary));
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
  transition: all var(--transition-fast);
}

.info-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-light);
}

.info-icon {
  flex-shrink: 0;
  color: var(--primary-color);
  width: 24px;
  height: 24px;
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.info-label {
  font-size: 0.875rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.info-value {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--text-primary);
}

.participation-section {
  text-align: center;
  margin-top: var(--space-6);
}

.join-btn {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  padding: var(--space-4) var(--space-6);
  border: none;
  border-radius: var(--radius-xl);
  font-size: 1.125rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-md);
}

.join-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.join-icon {
  width: 20px;
  height: 20px;
}

.detail-section {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-8);
  box-shadow: var(--shadow-lg);
  border: 1px solid var(--border-light);
}

.detail-header {
  display: flex;
  align-items: center;
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.detail-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.detail-icon {
  color: var(--primary-color);
}

.detail-content {
  font-size: 1rem;
  line-height: 1.8;
  color: var(--text-secondary);
  text-align: justify;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .activity-main {
    grid-template-columns: 1fr;
    gap: var(--space-6);
  }
  
  .activity-poster {
    height: 300px;
  }
  
  .info-section {
    padding: var(--space-6);
  }
  
  .activity-title {
    font-size: 2rem;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
    gap: var(--space-3);
  }
  
  .info-card {
    flex-direction: column;
    align-items: flex-start;
    padding: var(--space-3);
  }
  
  .detail-section {
    padding: var(--space-6);
  }
}

@media (max-width: 768px) {
  .activity-detail {
    padding: var(--space-4);
  }
  
  .activity-poster {
    height: 250px;
  }
  
  .activity-title {
    font-size: 1.75rem;
  }
  
  .info-section {
    padding: var(--space-4);
  }
  
  .detail-section {
    padding: var(--space-4);
  }
  
  .detail-title {
    font-size: 1.5rem;
  }
}

@media (max-width: 576px) {
  .activity-detail {
    padding: var(--space-3);
  }
  
  .activity-poster {
    height: 200px;
  }
  
  .activity-title {
    font-size: 1.5rem;
  }
  
  .info-section {
    padding: var(--space-3);
  }
  
  .detail-section {
    padding: var(--space-3);
  }
  
  .detail-title {
    font-size: 1.25rem;
  }
}
</style>