<template>
  <div class="write-article-page">
    <!-- 顶栏 -->
    <header class="write-header">
      <div class="header-content">
        <div class="header-left">
          <button class="back-btn" @click="goHome">
            <svg class="back-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="19" y1="12" x2="5" y2="12"></line>
              <polyline points="12,19 5,12 12,5"></polyline>
            </svg>
            返回主页
          </button>
          <h1 class="page-title">写文章</h1>
        </div>
        <div class="header-right">
          <button class="preview-btn" @click="previewArticle">
            <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
              <circle cx="12" cy="12" r="3"></circle>
            </svg>
            预览
          </button>
          <button class="submit-btn" @click="showModal = true">
            <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
            </svg>
            发布文章
          </button>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧编辑区域 -->
      <div class="editor-section">
        <div class="title-section">
          <div class="title-wrapper">
            <svg class="title-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
              <polyline points="14,2 14,8 20,8"></polyline>
              <line x1="16" y1="13" x2="8" y2="13"></line>
              <line x1="16" y1="17" x2="8" y2="17"></line>
              <polyline points="10,9 9,9 8,9"></polyline>
            </svg>
            <input 
              class="title-input" 
              type="text" 
              placeholder="请输入文章标题..." 
              v-model="article.title"
              maxlength="100"
            />
          </div>
          <div class="title-counter">{{ article.title.length }}/100</div>
        </div>
        
        <div class="editor-container">
          <Editor ref="editorRef" />
        </div>
      </div>

      <!-- 右侧设置面板 -->
      <div class="settings-panel">
        <div class="panel-header">
          <h3 class="section-title">
            <svg class="section-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="3"></circle>
              <path d="M12 1v6m0 6v6"></path>
              <path d="M17.66 7.34l-1.41 1.41m-8.5 8.5l-1.41 1.41"></path>
              <path d="M17.66 16.66l-1.41-1.41m-8.5-8.5l-1.41-1.41"></path>
            </svg>
            文章设置
          </h3>
        </div>
        
        <div class="panel-content">
          <div class="setting-item">
            <label class="setting-label">
              <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14,2 14,8 20,8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10,9 9,9 8,9"></polyline>
              </svg>
              文章摘要
            </label>
            <textarea 
              v-model="article.summary" 
              class="summary-input" 
              placeholder="请输入文章摘要..."
              maxlength="200"
              rows="4"
            ></textarea>
            <div class="counter">{{ article.summary.length }}/200</div>
          </div>

          <div class="setting-item">
            <label class="setting-label">
              <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 3h18v18H3zM21 9H3M21 15H3M12 3v18"></path>
              </svg>
              文章分类
            </label>
            <div class="select-wrapper">
              <select v-model="article.typeId" class="category-select">
                <option value="" disabled>请选择文章分类</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.categoryName }}
                </option>
              </select>
              <svg class="select-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="6,9 12,15 18,9"></polyline>
              </svg>
            </div>
          </div>

          <div class="setting-item">
            <label class="setting-label">
              <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 14V2h12l5.59 5.59a2 2 0 0 1 0 2.82z"></path>
                <line x1="7" y1="7" x2="7.01" y2="7"></line>
              </svg>
              文章标签
            </label>
            <div class="tags-container">
              <div v-for="tag in tags" :key="tag.id" class="tag-item">
                <input 
                  type="checkbox" 
                  :id="tag.id" 
                  :value="tag" 
                  v-model="article.tags" 
                  @change="updateTags(tag, $event.target.checked)"
                  class="tag-checkbox"
                >
                <label :for="tag.id" class="tag-label">{{ tag.tagName }}</label>
              </div>
            </div>
          </div>

          <div class="setting-item">
            <label class="setting-label">
              <svg class="label-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 14V2h12l5.59 5.59a2 2 0 0 1 0 2.82z"></path>
                <line x1="7" y1="7" x2="7.01" y2="7"></line>
              </svg>
              已选标签
            </label>
            <div class="selected-tags">
              <span v-for="tag in article.tags" :key="tag.id" class="selected-tag">
                {{ tag.tagName }}
                <button @click="removeTag(tag)" class="remove-tag">
                  <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                  </svg>
                </button>
              </span>
              <span v-if="article.tags.length === 0" class="no-tags">暂无标签</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 发布文章模态框 -->
    <Teleport to="body">
      <transition name="modal-fade">
        <div v-if="showModal" class="modal-backdrop" @click="showModal = false">
          <div class="modal" @click.stop>
            <div class="modal-header">
              <h2 class="modal-title">
                <svg class="modal-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                </svg>
                发布文章
              </h2>
              <button class="modal-close" @click="showModal = false">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </button>
            </div>
            <div class="modal-body">
              <div class="publish-info">
                <div class="info-item">
                  <span class="info-label">标题：</span>
                  <span class="info-value">{{ article.title || '未填写' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">分类：</span>
                  <span class="info-value">{{ getCategoryName(article.typeId) || '未选择' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">标签：</span>
                  <span class="info-value">{{ getSelectedTagsText() || '未选择' }}</span>
                </div>
              </div>
              <div class="modal-actions">
                <button type="button" class="cancel-btn" @click="showModal = false">
                  <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                  </svg>
                  取消
                </button>
                <button type="button" class="publish-btn" @click="submitArticle">
                  <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                  </svg>
                  确认发布
                </button>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </Teleport>
  </div>
</template>

<script setup lang="js">
import { ref, onMounted, reactive } from 'vue';
import Editor from "@/components/write/index.vue";
import axios from 'axios';
import { useRouter } from 'vue-router';
import { toast } from '@/utils/toast';

const article = reactive({
  id: null,
  summary: '',
  title: '',
  category: null,
  typeId: null,
  tags: [],
  body: null,
});

const categories = ref([]);
const tags = ref([]);
const showModal = ref(false);
const editorRef = ref();
const router = useRouter();

onMounted(() => {
  axios.get('http://localhost:8888/categorys')
    .then(response => { categories.value = response.data.data; })
    .catch(error => { console.error('获取文章类型失败', error); });

  axios.get('http://localhost:8888/tags')
    .then(response => { tags.value = response.data.data; })
    .catch(error => { console.error('获取文章标签失败', error); });
});

const updateTags = (tag, isChecked) => {
  if (isChecked) {
    if (!article.tags.some(t => t.id === tag.id)) {
      article.tags.push(tag);
    }
  } else {
    const index = article.tags.findIndex(t => t.id === tag.id);
    if (index !== -1) {
      article.tags.splice(index, 1);
    }
  }
};

const removeTag = (tag) => {
  const index = article.tags.findIndex(t => t.id === tag.id);
  if (index !== -1) {
    article.tags.splice(index, 1);
  }
};

const getCategoryName = (typeId) => {
  const category = categories.value.find(c => c.id === typeId);
  return category ? category.categoryName : '';
};

const getSelectedTagsText = () => {
  return article.tags.map(tag => tag.tagName).join(', ');
};

const previewArticle = () => {
  // 预览功能
};

const submitArticle = () => {
  const contentHtml = editorRef.value.getEditorContentHtml();
  const contentText = editorRef.value.getEditorContentText();
  article.body = { content: contentText, contentHtml: contentHtml };
  const token = localStorage.getItem('token_CellBlog');
  article.category = { id: article.typeId, categoryName: getCategoryName(article.typeId) };

  const config = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  };

  axios.post('http://localhost:8888/articles/publish', article, config)
    .then(response => { 
      toast.success('文章发布成功！'); 
      showModal.value = false; 
      router.push('/home');
    })
    .catch(error => { 
      console.error('文章发布失败', error); 
      toast.error('文章发布失败，请重试'); 
    });
};

const goHome = () => {
          router.push('/home');
};
</script>

<style scoped>
.write-article-page {
  min-height: 100vh;
  background: var(--bg-secondary);
}

/* 顶栏样式 */
.write-header {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  padding: 0 var(--space-6);
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: var(--shadow-sm);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
  max-width: 1400px;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-6);
}

.back-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: none;
  border: none;
  color: var(--text-secondary);
  font-size: 0.875rem;
  cursor: pointer;
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.back-btn:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.back-icon {
  flex-shrink: 0;
}

.page-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.header-right {
  display: flex;
  gap: var(--space-3);
}

.preview-btn, .submit-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  border: none;
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.preview-btn {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.preview-btn:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
}

.submit-btn {
  background: var(--primary-color);
  color: white;
}

.submit-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.btn-icon {
  flex-shrink: 0;
}

/* 主要内容区域 */
.main-content {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: var(--space-6);
  gap: var(--space-6);
  min-height: calc(100vh - 70px);
}

/* 左侧编辑区域 */
.editor-section {
  flex: 1;
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  border: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 150px);
}

.title-section {
  padding: var(--space-6) var(--space-6) 0;
  border-bottom: 1px solid var(--border-light);
  position: relative;
  flex-shrink: 0;
}

.title-wrapper {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.title-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

.title-input {
  flex: 1;
  border: none;
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  background: none;
  outline: none;
  padding: 0 0 var(--space-2) 0;
}

.title-input::placeholder {
  color: var(--text-tertiary);
}

.title-counter {
  position: absolute;
  right: var(--space-6);
  bottom: var(--space-2);
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.editor-container {
  padding: var(--space-6);
  height: calc(100vh - 200px);
  min-height: 500px;
  display: flex;
  flex-direction: column;
}

/* 右侧设置面板 */
.settings-panel {
  width: 360px;
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-light);
  height: fit-content;
  position: sticky;
  top: 90px;
}

.panel-header {
  padding: var(--space-6) var(--space-6) 0;
  border-bottom: 1px solid var(--border-light);
}

.section-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 1rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.section-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.panel-content {
  padding: var(--space-6);
}

.setting-item {
  margin-bottom: var(--space-6);
}

.setting-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: var(--space-3);
}

.label-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

.summary-input {
  width: 100%;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: var(--space-3);
  font-size: 0.875rem;
  resize: vertical;
  min-height: 80px;
  outline: none;
  transition: all var(--transition-fast);
  background: var(--bg-primary);
  color: var(--text-primary);
}

.summary-input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(6, 186, 199, 0.1);
}

.counter {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  text-align: right;
  margin-top: var(--space-1);
}

.select-wrapper {
  position: relative;
}

.category-select {
  width: 100%;
  padding: var(--space-3);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  outline: none;
  transition: all var(--transition-fast);
  background: var(--bg-primary);
  color: var(--text-primary);
  appearance: none;
}

.category-select:focus {
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

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  max-height: 120px;
  overflow-y: auto;
  padding: var(--space-2);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
}

.tag-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.tag-checkbox {
  margin: 0;
}

.tag-label {
  font-size: 0.75rem;
  color: var(--text-secondary);
  cursor: pointer;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.tag-label:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
}

.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  min-height: 32px;
  padding: var(--space-2);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
}

.selected-tag {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  background: var(--primary-light);
  color: var(--primary-color);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  font-weight: 500;
}

.remove-tag {
  background: none;
  border: none;
  color: var(--primary-color);
  cursor: pointer;
  padding: 0;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all var(--transition-fast);
}

.remove-tag:hover {
  background: var(--primary-color);
  color: white;
}

.no-tags {
  color: var(--text-tertiary);
  font-size: 0.75rem;
  font-style: italic;
}

/* 模态框样式 */
.modal-backdrop {
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

.modal {
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

.modal-title {
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

.modal-close {
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

.modal-close:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.modal-body {
  padding: var(--space-6);
}

.publish-info {
  margin-bottom: var(--space-6);
}

.info-item {
  display: flex;
  margin-bottom: var(--space-3);
}

.info-label {
  font-weight: 600;
  color: var(--text-secondary);
  width: 80px;
  flex-shrink: 0;
}

.info-value {
  color: var(--text-primary);
  flex: 1;
}

.modal-actions {
  display: flex;
  gap: var(--space-3);
  justify-content: flex-end;
}

.cancel-btn, .publish-btn {
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

.publish-btn {
  background: var(--primary-color);
  color: white;
}

.publish-btn:hover {
  background: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

/* 动画 */
.modal-fade-enter-active, .modal-fade-leave-active {
  transition: opacity 0.3s ease;
}

.modal-fade-enter-from, .modal-fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
  }
  
  .settings-panel {
    width: 100%;
    position: static;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 var(--space-4);
  }
  
  .main-content {
    padding: var(--space-4);
  }
  
  .page-title {
    font-size: 1.125rem;
  }
  
  .title-input {
    font-size: 1.25rem;
  }
  
  .header-right {
    gap: var(--space-2);
  }
  
  .preview-btn, .submit-btn {
    padding: var(--space-2) var(--space-3);
    font-size: 0.75rem;
  }
}
</style>

