<template>
  <div class="write-article-page">
    <!-- 顶栏 -->
    <header class="write-header">
      <div class="header-content">
        <div class="header-left">
          <button class="back-btn" @click="goHome">
            <span class="back-icon">←</span>
            返回主页
          </button>
          <h1 class="page-title">写文章</h1>
        </div>
        <div class="header-right">
          <button class="preview-btn" @click="previewArticle">预览</button>
          <button class="submit-btn" @click="showModal = true">发布文章</button>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧编辑区域 -->
      <div class="editor-section">
        <div class="title-section">
          <input 
            class="title-input" 
            type="text" 
            placeholder="请输入文章标题..." 
            v-model="article.title"
            maxlength="100"
          />
          <div class="title-counter">{{ article.title.length }}/100</div>
        </div>
        
        <div class="editor-container">
          <Editor ref="editorRef" />
        </div>
      </div>

      <!-- 右侧设置面板 -->
      <div class="settings-panel">
        <div class="panel-section">
          <h3 class="section-title">文章设置</h3>
          
          <div class="setting-item">
            <label class="setting-label">文章摘要</label>
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
            <label class="setting-label">文章分类</label>
            <select v-model="article.typeId" class="category-select">
              <option value="" disabled>请选择文章分类</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.categoryName }}
              </option>
            </select>
          </div>

          <div class="setting-item">
            <label class="setting-label">文章标签</label>
            <div class="tags-container">
              <div v-for="tag in tags" :key="tag.id" class="tag-item">
                <input 
                  type="checkbox" 
                  :id="tag.id" 
                  :value="tag" 
                  v-model="article.tags" 
                  @change="updateTags(tag, $event.target.checked)"
                >
                <label :for="tag.id" class="tag-label">{{ tag.tagName }}</label>
              </div>
            </div>
          </div>

          <div class="setting-item">
            <label class="setting-label">已选标签</label>
            <div class="selected-tags">
              <span v-for="tag in article.tags" :key="tag.id" class="selected-tag">
                {{ tag.tagName }}
                <button @click="removeTag(tag)" class="remove-tag">×</button>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 发布文章模态框 -->
    <transition name="modal-fade">
      <div v-if="showModal" class="modal-backdrop" @click="showModal = false">
        <div class="modal" @click.stop>
          <div class="modal-header">
            <h2 class="modal-title">发布文章</h2>
            <button class="modal-close" @click="showModal = false">×</button>
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
              <button type="button" class="cancel-btn" @click="showModal = false">取消</button>
              <button type="button" class="publish-btn" @click="submitArticle">确认发布</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="js">
import { ref, onMounted, reactive } from 'vue';
import Editor from "@/components/write/index.vue";
import axios from 'axios';
import { useRouter } from 'vue-router';

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
  console.log('预览文章');
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
      console.log('文章发布成功', response); 
      alert('文章发布成功'); 
      showModal.value = false; 
      router.push('/home/index');
    })
    .catch(error => { 
      console.error('文章发布失败', error); 
      alert('文章发布失败'); 
    });
};

const goHome = () => {
  router.push('/home/index');
};
</script>

<style scoped>
.write-article-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* 顶栏样式 */
.write-header {
  background: white;
  border-bottom: 1px solid #e1e5e9;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  max-width: 1400px;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 24px;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: none;
  border: none;
  color: #666;
  font-size: 14px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background-color: #f0f0f0;
  color: #333;
}

.back-icon {
  font-size: 16px;
  font-weight: bold;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.header-right {
  display: flex;
  gap: 12px;
}

.preview-btn, .submit-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.preview-btn {
  background-color: #f0f0f0;
  color: #666;
}

.preview-btn:hover {
  background-color: #e0e0e0;
}

.submit-btn {
  background: linear-gradient(135deg, #06bac7 0%, #008db2 100%);
  color: white;
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(6, 186, 199, 0.3);
}

/* 主要内容区域 */
.main-content {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  gap: 24px;
  min-height: calc(100vh - 64px);
}

/* 左侧编辑区域 */
.editor-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.title-section {
  padding: 24px 24px 0;
  border-bottom: 1px solid #e1e5e9;
  position: relative;
}

.title-input {
  width: 100%;
  border: none;
  font-size: 24px;
  font-weight: 600;
  color: #333;
  background: none;
  outline: none;
  padding: 0 0 8px 0;
}

.title-input::placeholder {
  color: #999;
}

.title-counter {
  position: absolute;
  right: 24px;
  bottom: 8px;
  font-size: 12px;
  color: #999;
}

.editor-container {
  padding: 24px;
}

/* 右侧设置面板 */
.settings-panel {
  width: 320px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  height: fit-content;
  position: sticky;
  top: 88px;
}

.panel-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
}

.setting-item {
  margin-bottom: 20px;
}

.setting-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #555;
  margin-bottom: 8px;
}

.summary-input {
  width: 100%;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  padding: 12px;
  font-size: 14px;
  resize: vertical;
  min-height: 80px;
  outline: none;
  transition: border-color 0.3s ease;
}

.summary-input:focus {
  border-color: #06bac7;
}

.counter {
  font-size: 12px;
  color: #999;
  text-align: right;
  margin-top: 4px;
}

.category-select {
  width: 100%;
  padding: 12px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

.category-select:focus {
  border-color: #06bac7;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  max-height: 120px;
  overflow-y: auto;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.tag-label {
  font-size: 12px;
  color: #666;
  cursor: pointer;
}

.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 32px;
}

.selected-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: #e8f5f5;
  color: #06bac7;
  padding: 4px 8px;
  border-radius: 16px;
  font-size: 12px;
}

.remove-tag {
  background: none;
  border: none;
  color: #06bac7;
  cursor: pointer;
  font-size: 14px;
  padding: 0;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.remove-tag:hover {
  background-color: #06bac7;
  color: white;
}

/* 模态框样式 */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal {
  background: white;
  border-radius: 12px;
  width: 500px;
  max-width: 90vw;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 0;
}

.modal-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: #f0f0f0;
  color: #333;
}

.modal-body {
  padding: 24px;
}

.publish-info {
  margin-bottom: 24px;
}

.info-item {
  display: flex;
  margin-bottom: 12px;
}

.info-label {
  font-weight: 500;
  color: #555;
  width: 80px;
  flex-shrink: 0;
}

.info-value {
  color: #333;
  flex: 1;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.cancel-btn, .publish-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn {
  background-color: #f0f0f0;
  color: #666;
}

.cancel-btn:hover {
  background-color: #e0e0e0;
}

.publish-btn {
  background: linear-gradient(135deg, #06bac7 0%, #008db2 100%);
  color: white;
}

.publish-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(6, 186, 199, 0.3);
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
    padding: 0 16px;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .page-title {
    font-size: 18px;
  }
  
  .title-input {
    font-size: 20px;
  }
}
</style>

