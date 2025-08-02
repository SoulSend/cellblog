<template>
  <div class="profile-page">
    <!-- 顶部导航栏 -->
    <div class="profile-nav">
      <button class="back-home-btn" @click="goHome">
        <svg class="back-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="19" y1="12" x2="5" y2="12"></line>
          <polyline points="12,19 5,12 12,5"></polyline>
        </svg>
        返回首页
      </button>
      <h2 class="nav-title">个人信息</h2>
    </div>

    <!-- 顶部用户信息卡片 -->
    <div class="profile-header">
      <div class="profile-card">
        <div class="profile-avatar-section">
          <div class="avatar-container">
            <img :src="user?.avatar || '/src/assets/image/userAvatar.png'" alt="User Avatar" class="profile-avatar" />
            <div class="avatar-overlay">
              <button class="change-avatar-btn" @click="changeAvatar">
                <svg class="camera-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
                  <circle cx="12" cy="13" r="4"></circle>
                </svg>
              </button>
            </div>
          </div>
          <div class="profile-info">
            <h1 class="profile-name">{{ user?.nickname || '用户昵称' }}</h1>
            <p class="profile-account">@{{ user?.account }}</p>
            <div class="profile-stats">
              <div class="stat-item">
                <span class="stat-number">{{ articleList.length }}</span>
                <span class="stat-label">文章</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ totalViews }}</span>
                <span class="stat-label">浏览量</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ totalComments }}</span>
                <span class="stat-label">评论</span>
              </div>
            </div>
          </div>
        </div>
        <div class="profile-actions">
          <button class="edit-profile-btn" @click="editUserInfo">
            <svg class="edit-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
            编辑资料
          </button>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="profile-content">
      <!-- 左侧：详细信息 -->
      <div class="profile-details">
        <div class="detail-card">
          <h3 class="card-title">
            <svg class="card-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
            个人信息
          </h3>
          <div class="detail-list">
            <div class="detail-item">
              <span class="detail-label">用户ID</span>
              <span class="detail-value">{{ user?.id || '未知' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">邮箱</span>
              <span class="detail-value">{{ user?.email || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">手机号</span>
              <span class="detail-value">{{ user?.mobilePhone || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">注册时间</span>
              <span class="detail-value">{{ formatDate(user?.createDate || '') }}</span>
            </div>
          </div>
        </div>

        <div class="detail-card">
          <h3 class="card-title">
            <svg class="card-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14,2 14,8 20,8"></polyline>
              <line x1="16" y1="13" x2="8" y2="13"></line>
              <line x1="16" y1="17" x2="8" y2="17"></line>
              <polyline points="10,9 9,9 8,9"></polyline>
            </svg>
            账户统计
          </h3>
          <div class="stats-grid">
            <div class="stat-card">
              <svg class="stat-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14,2 14,8 20,8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10,9 9,9 8,9"></polyline>
              </svg>
              <div class="stat-content">
                <div class="stat-value">{{ articleList.length }}</div>
                <div class="stat-name">发布文章</div>
              </div>
            </div>
            <div class="stat-card">
              <svg class="stat-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
              </svg>
              <div class="stat-content">
                <div class="stat-value">{{ totalViews }}</div>
                <div class="stat-name">总浏览量</div>
              </div>
            </div>
            <div class="stat-card">
              <svg class="stat-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
              </svg>
              <div class="stat-content">
                <div class="stat-value">{{ totalComments }}</div>
                <div class="stat-name">总评论数</div>
              </div>
            </div>
            <div class="stat-card">
              <svg class="stat-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
              </svg>
              <div class="stat-content">
                <div class="stat-value">{{ averageRating }}</div>
                <div class="stat-name">平均评分</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：文章列表 -->
      <div class="articles-section">
        <div class="articles-header">
          <h3 class="section-title">
            <svg class="section-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14,2 14,8 20,8"></polyline>
              <line x1="16" y1="13" x2="8" y2="13"></line>
              <line x1="16" y1="17" x2="8" y2="17"></line>
              <polyline points="10,9 9,9 8,9"></polyline>
            </svg>
            我的文章
          </h3>
          <div class="articles-filter">
            <div class="select-wrapper">
              <select v-model="sortBy" class="filter-select">
                <option value="date">按时间排序</option>
                <option value="views">按浏览量排序</option>
                <option value="comments">按评论数排序</option>
              </select>
              <svg class="select-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="6,9 12,15 18,9"></polyline>
              </svg>
            </div>
          </div>
        </div>

        <div class="articles-list">
          <div v-if="articleList.length === 0" class="empty-state">
            <svg class="empty-icon" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14,2 14,8 20,8"></polyline>
              <line x1="16" y1="13" x2="8" y2="13"></line>
              <line x1="16" y1="17" x2="8" y2="17"></line>
              <polyline points="10,9 9,9 8,9"></polyline>
            </svg>
            <h4>还没有发布文章</h4>
            <p>开始创作你的第一篇文章吧！</p>
            <button class="create-article-btn" @click="createArticle">
              <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
              </svg>
              写文章
            </button>
          </div>

          <div v-else class="article-item" v-for="article in sortedArticles" :key="article.id" @click="goToDetail(article.id)">
            <div class="article-main">
              <h4 class="article-title">{{ article.title }}</h4>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-meta">
                <span class="article-category" v-if="article.category">{{ article.category }}</span>
                <span class="article-date">{{ formatDate(article.createDate) }}</span>
              </div>
            </div>
            <div class="article-stats">
              <div class="stat">
                <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                  <circle cx="12" cy="12" r="3"></circle>
                </svg>
                <span class="stat-value">{{ article.viewCounts }}</span>
              </div>
              <div class="stat">
                <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                </svg>
                <span class="stat-value">{{ article.commentCounts }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息模态框 -->
    <Teleport to="body">
      <div v-if="showEditForm" class="modal-overlay" @click="cancelEdit">
        <div class="edit-modal" @click.stop>
          <div class="modal-header">
            <h3>
              <svg class="modal-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
              </svg>
              编辑个人信息
            </h3>
            <button class="close-btn" @click="cancelEdit">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
              </svg>
            </button>
          </div>
          <form @submit.prevent="submitEdit" class="edit-form">
            <div class="form-group">
              <label for="nickname">
                <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
                昵称
              </label>
              <input 
                id="nickname"
                type="text" 
                v-model="editForm.nickname" 
                placeholder="请输入昵称" 
                required 
              />
            </div>
            <div class="form-group">
              <label for="email">
                <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                  <polyline points="22,6 12,13 2,6"></polyline>
                </svg>
                邮箱
              </label>
              <input 
                id="email"
                type="email" 
                v-model="editForm.email" 
                placeholder="请输入邮箱" 
                required 
              />
            </div>
            <div class="form-group">
              <label for="mobilePhone">
                <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                </svg>
                手机号
              </label>
              <input 
                id="mobilePhone"
                type="tel" 
                v-model="editForm.mobilePhone" 
                placeholder="请输入手机号" 
              />
            </div>
            <div class="form-actions">
              <button type="button" class="cancel-btn" @click="cancelEdit">
                <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
                取消
              </button>
              <button type="submit" class="save-btn">
                <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                  <polyline points="17,21 17,13 7,13 7,21"></polyline>
                  <polyline points="7,3 7,8 15,8"></polyline>
                </svg>
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';
import { toast } from '@/utils/toast';

// 定义文章的数据结构
interface Article {
  id: string;
  title: string;
  summary: string;
  commentCounts: number;
  viewCounts: number;
  createDate: string;
  category?: string;
}

// 定义用户的数据结构
interface User {
  id: string;
  nickname: string;
  avatar: string;
  account: string;
  createDate: string;
  email: string;
  mobilePhone?: string | null;
}

const user = ref<User | null>(null);
const articleList = ref<Article[]>([]);
const router = useRouter();
const showEditForm = ref(false);
const sortBy = ref('date');
const editForm = ref({
  nickname: '',
  email: '',
  mobilePhone: ''
});

// 计算属性
const totalViews = computed(() => {
  return articleList.value.reduce((sum, article) => sum + article.viewCounts, 0);
});

const totalComments = computed(() => {
  return articleList.value.reduce((sum, article) => sum + article.commentCounts, 0);
});

const averageRating = computed(() => {
  if (articleList.value.length === 0) return 0;
  const totalRating = articleList.value.reduce((sum, article) => sum + (article.viewCounts / 100), 0);
  return (totalRating / articleList.value.length).toFixed(1);
});

const sortedArticles = computed(() => {
  const articles = [...articleList.value];
  switch (sortBy.value) {
    case 'views':
      return articles.sort((a, b) => b.viewCounts - a.viewCounts);
    case 'comments':
      return articles.sort((a, b) => b.commentCounts - a.commentCounts);
    case 'date':
    default:
      return articles.sort((a, b) => new Date(b.createDate).getTime() - new Date(a.createDate).getTime());
  }
});

const fetchUserInfo = async () => {
  try {
    const response = await axios.get<{ data: User }>('/users/userInfo');
    if (response.status === 200 && response.data.data) {
      user.value = response.data.data;
    }
  } catch (error) {
    console.error('Failed to fetch user info:', error);
  }
};

const fetchArticleList = async () => {
  try {
    const response = await axios.get<{ data: Article[] }>('/articles/userInfo/articleList');
    if (response.status === 200 && response.data.data) {
      articleList.value = response.data.data;
    }
  } catch (error) {
    console.error('Failed to fetch article list:', error);
  }
};

const goToDetail = (id: string) => {
  router.push({ name: 'ArticleDetail', params: { id } });
};

const editUserInfo = () => {
  showEditForm.value = true;
  editForm.value.nickname = user.value?.nickname || '';
  editForm.value.email = user.value?.email || '';
  editForm.value.mobilePhone = user.value?.mobilePhone || '';
};

const submitEdit = async () => {
  const userData = {
    id: user.value?.id,
    nickname: editForm.value.nickname || null,
    email: editForm.value.email || null,
    mobilePhone: editForm.value.mobilePhone || null
  };

  try {
    const response = await axios.post('/users/changeUserInfo', userData);
    if (response.status === 200) {
      toast.success('个人信息修改成功！');
      await fetchUserInfo();
      showEditForm.value = false;
    } else {
      toast.error('修改失败，请重试');
    }
  } catch (error) {
    console.error('Failed to update user info:', error);
    toast.error('修改失败，请重试');
  }
};

const cancelEdit = () => {
  showEditForm.value = false;
};

const changeAvatar = () => {
  // 头像更换功能
};

const createArticle = () => {
  router.push('/write');
};

const goHome = () => {
  router.push('/home');
};

const formatDate = (dateString: string) => {
  if (!dateString) return '未知';
  const date = new Date(dateString);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
};

onMounted(() => {
  fetchUserInfo();
  fetchArticleList();
});
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: var(--bg-secondary);
  padding: 0;
}

/* 顶部导航栏 */
.profile-nav {
  display: flex;
  align-items: center;
  gap: var(--space-5);
  padding: var(--space-5) var(--space-10);
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
}

.back-home-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  padding: var(--space-2) var(--space-4);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.back-home-btn:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
  border-color: var(--border-color);
}

.back-icon {
  flex-shrink: 0;
}

.nav-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

/* 顶部用户信息卡片 */
.profile-header {
  padding: var(--space-10);
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
}

.profile-card {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-avatar-section {
  display: flex;
  align-items: center;
  gap: var(--space-6);
}

.avatar-container {
  position: relative;
  width: 120px;
  height: 120px;
}

.profile-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid var(--border-light);
  transition: all var(--transition-normal);
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.avatar-container:hover .avatar-overlay {
  opacity: 1;
}

.change-avatar-btn {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  padding: var(--space-2);
  border-radius: 50%;
  transition: all var(--transition-fast);
}

.change-avatar-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.camera-icon {
  flex-shrink: 0;
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.profile-account {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0 0 var(--space-5) 0;
}

.profile-stats {
  display: flex;
  gap: var(--space-8);
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
}

.stat-label {
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.profile-actions {
  display: flex;
  gap: var(--space-3);
}

.edit-profile-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: var(--primary-color);
  color: white;
  border: none;
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.edit-profile-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.edit-icon {
  flex-shrink: 0;
}

/* 主要内容区域 */
.profile-content {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: var(--space-10);
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-10);
}

/* 左侧详细信息 */
.profile-details {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.detail-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-6);
  border: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
}

.card-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-5) 0;
}

.card-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.detail-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--border-light);
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-label {
  font-size: 0.875rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.detail-value {
  font-size: 0.875rem;
  color: var(--text-primary);
  font-weight: 500;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-4);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.stat-card:hover {
  background: var(--bg-tertiary);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--primary-color);
  line-height: 1;
}

.stat-name {
  font-size: 0.75rem;
  color: var(--text-secondary);
  margin-top: var(--space-1);
}

/* 右侧文章列表 */
.articles-section {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--space-6);
  border: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
}

.articles-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-6);
}

.section-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.section-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.articles-filter {
  position: relative;
}

.select-wrapper {
  position: relative;
}

.filter-select {
  padding: var(--space-2) var(--space-3);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  outline: none;
  transition: all var(--transition-fast);
  background: var(--bg-primary);
  color: var(--text-primary);
  appearance: none;
  padding-right: calc(var(--space-3) + 16px + var(--space-2));
}

.filter-select:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(6, 186, 199, 0.1);
}

.select-icon {
  position: absolute;
  right: var(--space-3);
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-tertiary);
  pointer-events: none;
}

.articles-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.empty-state {
  text-align: center;
  padding: var(--space-15) var(--space-5);
  color: var(--text-secondary);
}

.empty-icon {
  color: var(--text-tertiary);
  margin-bottom: var(--space-4);
}

.empty-state h4 {
  font-size: 1.125rem;
  margin: 0 0 var(--space-2) 0;
  color: var(--text-primary);
}

.empty-state p {
  margin: 0 0 var(--space-6) 0;
}

.create-article-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: var(--primary-color);
  color: white;
  border: none;
  padding: var(--space-3) var(--space-6);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.create-article-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.btn-icon {
  flex-shrink: 0;
}

.article-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-5);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.article-item:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.article-main {
  flex: 1;
}

.article-title {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
  line-height: 1.4;
}

.article-summary {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin: 0 0 var(--space-3) 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: var(--space-4);
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.article-category {
  background: var(--bg-secondary);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
}

.article-stats {
  display: flex;
  gap: var(--space-4);
}

.stat {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: 0.75rem;
  color: var(--text-secondary);
}

.stat-icon {
  flex-shrink: 0;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.edit-modal {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  width: 500px;
  max-width: 90vw;
  box-shadow: var(--shadow-xl);
  border: 1px solid var(--border-light);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-6) var(--space-6) 0;
}

.modal-header h3 {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.modal-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.close-btn {
  background: none;
  border: none;
  color: var(--text-tertiary);
  cursor: pointer;
  padding: var(--space-1);
  border-radius: 50%;
  transition: all var(--transition-fast);
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.edit-form {
  padding: var(--space-6);
}

.form-group {
  margin-bottom: var(--space-5);
}

.form-group label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: var(--space-2);
}

.label-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

.form-group input {
  width: 100%;
  padding: var(--space-3) var(--space-4);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  outline: none;
  transition: all var(--transition-fast);
  box-sizing: border-box;
  background: var(--bg-primary);
  color: var(--text-primary);
}

.form-group input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(6, 186, 199, 0.1);
}

.form-actions {
  display: flex;
  gap: var(--space-3);
  justify-content: flex-end;
  margin-top: var(--space-6);
}

.cancel-btn, .save-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  border: none;
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.cancel-btn {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.cancel-btn:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
}

.save-btn {
  background: var(--primary-color);
  color: white;
}

.save-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  
  .profile-card {
    flex-direction: column;
    text-align: center;
    gap: var(--space-5);
  }
  
  .profile-avatar-section {
    flex-direction: column;
  }
  
  .profile-stats {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .profile-page {
    padding: var(--space-4);
  }
  
  .profile-card {
    padding: var(--space-5);
  }
  
  .avatar-container {
    width: 100px;
    height: 100px;
  }
  
  .profile-name {
    font-size: 1.5rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .article-item {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-3);
  }
  
  .article-stats {
    align-self: flex-end;
  }
}
</style>