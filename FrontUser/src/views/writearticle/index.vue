<template>
  <div class="container">
    <!-- 导航栏 -->
    <div class="navbar">
      <input class="title-input" type="text" placeholder="请输入文章标题" v-model="article.title" />
      <button class="submit-button" @click="showModal = true">提交</button>
      <button class="submit-button" @click="goHome" style="margin-left: 10px;">返回主页</button>
    </div>

    <!-- 编辑器组件 -->
    <div class="editor-container">
      <Editor ref="editorRef" />
    </div>

    <!-- 提交表单模态框 -->
    <transition name="modal-fade">
      <div v-if="showModal" class="modal-backdrop">
        <div class="modal">
          <button class="modal-close" @click="showModal = false">×</button>
          <h2 class="modal-title">提交文章</h2>
          <form @submit.prevent="submitArticle">
            <div class="form-group">
              <label for="summary">摘要</label>
              <input v-model="article.summary" class="form-input" type="text" id="summary" placeholder="请输入摘要" />
            </div>
            <div class="form-group">
              <label for="typeId">文章类型</label>
              <select v-model="article.typeId" class="form-input" id="typeId">
                <option disabled value="">请选择文章类型</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.categoryName }}
                </option>
              </select>
            </div>
            <div class="form-group tag-group">
              <label for="tags">文章标签</label>
              <div class="tag-list">
                <div v-for="tag in tags" :key="tag.id" class="tag-item">
                  <input type="checkbox" :id="tag.id" :value="tag" v-model="article.tags" @change="updateTags(tag, $event.target.checked)">
                  <label :for="tag.id">{{ tag.tagName }}</label>
                </div>
              </div>
            </div>
            <div class="form-actions">
              <button type="button" class="cancel-button" @click="showModal = false">取消</button>
              <button type="submit" class="submit-button">发布文章</button>
            </div>
          </form>
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
    // 检查标签是否已经存在于数组中
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

const submitArticle = () => {
  const contentHtml = editorRef.value.getEditorContentHtml();
  const contentText = editorRef.value.getEditorContentText();
  article.body = { content: contentText, contentHtml: contentHtml };
  const token = localStorage.getItem('token_CellBlog');
  article.category = { id: article.typeId, categoryName: categories.value.find(c => c.id === article.typeId)?.categoryName };

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
    })
    .catch(error => { 
      console.error('文章发布失败', error); 
      alert('文章发布失败'); 
    });
};

const goHome = () => {
  router.push({ name: 'home' });
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #ffffff; /* 白色背景 */
}

.navbar {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 800px; /* 限制最大宽度 */
  padding: 10px;
  background-color: #f6f8fa; /* 浅灰色背景 */
  border-bottom: 1px solid #e1e4e8; /* 浅灰色边框 */
}

.title-input {
  margin-right: 20px;
  padding: 10px;
  border: 1px solid #d1d5db; /* 浅灰色边框 */
  border-radius: 4px;
  background-color: #ffffff;
  color: #26aba2; /* 青色文字 */
  font-size: 16px;
  transition: border-color 0.3s;
}

.title-input:focus {
  border-color: #26aba2; /* 青色边框 */
  outline: none;
}

.submit-button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  background-color: #26aba2; /* 青色背景 */
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #1e7a74; /* 深青色 */
}

.editor-container {
  width: 100%;
  max-width: 800px;
  margin: 20px 0;
  padding: 20px;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

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
  z-index: 1000;
}

.modal {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 6px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  max-width: 500px;
  width: 100%;
}

.modal-close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  cursor: pointer;
  background: none;
  border: none;
  color: #26aba2;
}

.modal-title {
  text-align: center;
  margin-bottom: 20px;
  color: #26aba2;
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.form-input,
select.form-input {
  width: 100%; /* 使输入框和下拉框宽度一致 */
  padding: 8px; /* 调整内边距以提供更好的点击区域 */
  border: 1px solid #ccc; /* 统一边框样式 */
  border-radius: 4px;
  font-size: 14px; /* 统一字体大小 */
  line-height: 1.5; /* 调整行高以匹配输入框大小 */
  margin-top: 4px; /* 增加顶部间距 */
  box-sizing: border-box; /* 包括边框和内边距在宽度内 */
}

.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.cancel-button {
  padding: 8px 15px;
  background-color: #ffffff;
  color: #333;
  border: 1px solid #d1d5db;
  border-radius: 4px;
cursor: pointer;
margin-right: 10px;
transition: background-color 0.3s;
}

.cancel-button:hover {
background-color: #f0f0f0;
}

.modal-fade-enter-active, .modal-fade-leave-active {
transition: opacity 0.5s;
}

.modal-fade-enter-from, .modal-leave-to {
opacity: 0;
}
</style>

