<template>
  <div class="activity-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">活动中心</h1>
        <p class="page-subtitle">发现精彩活动，参与技术交流</p>
      </div>
      <div class="header-stats">
        <div class="stat-item">
          <svg class="stat-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
          </svg>
          <span class="stat-text">{{ activities.length }} 个活动</span>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p class="loading-text">正在加载活动...</p>
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
      <button class="retry-btn" @click="fetchActivities">
        <svg class="retry-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="23,4 23,10 17,10"></polyline>
          <polyline points="1,20 1,14 7,14"></polyline>
          <path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15"></path>
        </svg>
        重新加载
      </button>
    </div>

    <!-- 空状态 -->
    <div v-else-if="activities.length === 0" class="empty-state">
      <svg class="empty-icon" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
      </svg>
      <h3 class="empty-title">暂无活动</h3>
      <p class="empty-description">当前没有可参与的活动，请稍后再来查看</p>
    </div>

    <!-- 活动列表 -->
    <div v-else class="activity-grid">
      <div 
        v-for="activity in activities" 
        :key="activity.id" 
        class="activity-card"
        @click="goToDetail(activity.id)"
      >
        <div class="card-header">
          <div class="poster-container">
            <img :src="activity.poster" alt="活动海报" class="poster" />
            <div class="poster-overlay">
              <div class="activity-status">
                <span class="status-badge">进行中</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="card-content">
          <h3 class="activity-title">{{ activity.title }}</h3>
          <p class="activity-description">{{ activity.description }}</p>
          
          <div class="activity-meta">
            <div class="meta-item">
              <svg class="meta-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
              </svg>
              <span class="meta-text">{{ activity.publisher }}</span>
            </div>
            
            <div class="meta-item">
              <svg class="meta-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                <line x1="16" y1="2" x2="16" y2="6"></line>
                <line x1="8" y1="2" x2="8" y2="6"></line>
                <line x1="3" y1="10" x2="21" y2="10"></line>
              </svg>
              <span class="meta-text">{{ formatDate(activity.startTime) }}</span>
            </div>
            
            <div class="meta-item">
              <svg class="meta-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
              </svg>
              <span class="meta-text">{{ activity.views }} 次浏览</span>
            </div>
          </div>
        </div>
        
        <div class="card-footer">
          <button class="join-btn" @click.stop="joinActivity(activity.id)">
            <svg class="join-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="8.5" cy="7" r="4"></circle>
              <line x1="20" y1="8" x2="20" y2="14"></line>
              <line x1="23" y1="11" x2="17" y2="11"></line>
            </svg>
            报名参加
          </button>
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
  startTime: string;
  endTime: string;
  views: number;
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

    const joinActivity = (id: number) => {
      // 实现报名逻辑
      console.log('Joining activity:', id);
    };

    const formatDate = (isoDateString: string) => {
      const date = new Date(isoDateString);
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    };

    fetchActivities();

    return {
      activities,
      loading,
      error,
      goToDetail,
      joinActivity,
      formatDate,
      fetchActivities,
    };
  },
});
</script>

<style scoped>
.activity-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-6);
  animation: fadeIn 0.5s ease-out;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-8);
  padding-bottom: var(--space-6);
  border-bottom: 1px solid var(--border-light);
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.page-subtitle {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0;
  line-height: 1.6;
}

.header-stats {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.stat-icon {
  color: var(--primary-color);
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-12);
  color: var(--text-secondary);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--border-light);
  border-top: 3px solid var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: var(--space-4);
}

.loading-text {
  font-size: 0.875rem;
  margin: 0;
}

/* 错误状态 */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-12);
  text-align: center;
  color: var(--text-secondary);
}

.error-icon {
  color: var(--error-color);
  margin-bottom: var(--space-4);
}

.error-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.error-message {
  font-size: 0.875rem;
  margin: 0 0 var(--space-6) 0;
  max-width: 400px;
}

.retry-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.retry-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
}

.retry-icon {
  flex-shrink: 0;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-12);
  text-align: center;
  color: var(--text-secondary);
}

.empty-icon {
  color: var(--text-tertiary);
  margin-bottom: var(--space-4);
}

.empty-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.empty-description {
  font-size: 0.875rem;
  margin: 0;
  max-width: 400px;
  line-height: 1.6;
}

/* 活动网格 */
.activity-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: var(--space-6);
}

.activity-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
  cursor: pointer;
  border: 1px solid var(--border-light);
}

.activity-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.card-header {
  position: relative;
}

.poster-container {
  height: 200px;
  position: relative;
  overflow: hidden;
}

.poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.activity-card:hover .poster {
  transform: scale(1.05);
}

.poster-overlay {
  position: absolute;
  top: var(--space-3);
  right: var(--space-3);
}

.status-badge {
  background: var(--success-color);
  color: white;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  font-weight: 600;
}

.card-content {
  padding: var(--space-6);
}

.activity-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-3) 0;
  line-height: 1.4;
}

.activity-description {
  font-size: 0.875rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0 0 var(--space-4) 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.activity-meta {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.meta-icon {
  flex-shrink: 0;
}

.meta-text {
  flex: 1;
}

.card-footer {
  padding: 0 var(--space-6) var(--space-6);
}

.join-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  width: 100%;
  padding: var(--space-3) var(--space-4);
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.join-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.join-icon {
  flex-shrink: 0;
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .activity-page {
    padding: var(--space-4);
  }
  
  .page-header {
    flex-direction: column;
    gap: var(--space-4);
    align-items: flex-start;
  }
  
  .page-title {
    font-size: 1.5rem;
  }
  
  .activity-grid {
    grid-template-columns: 1fr;
    gap: var(--space-4);
  }
  
  .card-content {
    padding: var(--space-4);
  }
  
  .card-footer {
    padding: 0 var(--space-4) var(--space-4);
  }
}

@media (max-width: 480px) {
  .activity-page {
    padding: var(--space-3);
  }
  
  .page-title {
    font-size: 1.25rem;
  }
  
  .poster-container {
    height: 150px;
  }
}
</style>