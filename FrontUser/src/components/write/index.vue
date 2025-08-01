<template>
  <div class="editor-wrapper">
    <!-- 模式切换工具栏 -->
    <div class="mode-toolbar">
      <div class="mode-tabs">
        <button 
          class="mode-tab" 
          :class="{ active: editorMode === 'wysiwyg' }"
          @click="switchMode('wysiwyg')"
        >
          <svg class="tab-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14,2 14,8 20,8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10,9 9,9 8,9"></polyline>
          </svg>
          富文本编辑
        </button>
        <button 
          class="mode-tab" 
          :class="{ active: editorMode === 'markdown' }"
          @click="switchMode('markdown')"
        >
          <svg class="tab-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14,2 14,8 20,8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10,9 9,9 8,9"></polyline>
          </svg>
          Markdown
        </button>
      </div>
      
      <!-- Markdown模式下的预览切换 -->
      <div v-if="editorMode === 'markdown'" class="preview-toggle">
        <button 
          class="preview-btn" 
          :class="{ active: showPreview }"
          @click="togglePreview"
        >
          <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
            <circle cx="12" cy="12" r="3"></circle>
          </svg>
          {{ showPreview ? '编辑' : '预览' }}
        </button>
      </div>
    </div>

    <!-- 富文本编辑器 -->
    <div v-if="editorMode === 'wysiwyg'" class="wysiwyg-editor">
      <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        class="editor-toolbar"
      />
      <Editor
        v-model="valueHtml"
        :defaultConfig="editorConfig"
        class="editor-content"
        @onCreated="handleCreated"
      />
    </div>

    <!-- Markdown编辑器 -->
    <div v-if="editorMode === 'markdown'" class="markdown-editor">
      <div class="markdown-container" :class="{ 'split-view': showPreview }">
        <!-- Markdown输入区域 -->
        <div class="markdown-input" :class="{ 'half-width': showPreview }">
          <div class="input-header">
            <h3 class="input-title">Markdown 编辑</h3>
            <div class="input-tips">
              <span class="tip">支持标准Markdown语法</span>
            </div>
          </div>
          <textarea
            v-model="markdownContent"
            class="markdown-textarea"
            placeholder="开始编写你的Markdown文章...
            
# 标题语法
## 二级标题
### 三级标题

**粗体文本**
*斜体文本*

- 列表项1
- 列表项2

1. 有序列表1
2. 有序列表2

[链接文本](URL)
![图片描述](图片URL)

`代码片段`

```javascript
// 代码块
console.log('Hello World');
```

> 引用文本

---
分割线"
            @input="updateMarkdownContent"
          ></textarea>
        </div>
        
        <!-- 预览区域 -->
        <div v-if="showPreview" class="markdown-preview half-width">
          <div class="preview-header">
            <h3 class="preview-title">预览效果</h3>
          </div>
          <div class="preview-content" v-html="renderedMarkdown"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onBeforeUnmount, ref, shallowRef, onMounted, computed, watch } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { marked } from 'marked';
import hljs from 'highlight.js';

const editorRef = shallowRef(null);
const valueHtml = ref('');
const editorMode = ref('wysiwyg'); // 'wysiwyg' | 'markdown'
const markdownContent = ref('');
const showPreview = ref(false);

// 配置marked
marked.setOptions({
  highlight: function(code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(code, { language: lang }).value;
      } catch (err) {}
    }
    return hljs.highlightAuto(code).value;
  },
  breaks: true,
  gfm: true
});

// 渲染Markdown
const renderedMarkdown = computed(() => {
  if (!markdownContent.value) return '';
  return marked(markdownContent.value);
});

// 编辑器配置
const editorConfig = {
  placeholder: '开始创作你的文章...',
  autoFocus: false,
  scroll: true,
  MENU_CONF: {
    uploadImage: {
      server: 'http://localhost:8888/upload',
      fieldName: 'image',
      maxFileSize: 10 * 1024 * 1024, // 10MB
      allowedFileTypes: ['image/*'],
      customInsert(res, insertFn) {
        if (res.success) {
          insertFn(res.data.url, res.data.alt, res.data.href);
        } else {
          console.error('图片上传失败:', res.message);
        }
      },
    },
    insertImage: {
      checkImage: true,
      parseImageSrc: (src) => {
        if (src.indexOf('http') !== 0) {
          return `http://localhost:8888${src}`;
        }
        return src;
      },
    },
  },
};

// 工具栏配置
const toolbarConfig = {
  excludeKeys: [
    'group-video',
    'insertTable',
    'codeBlock',
    'fullScreen',
  ],
};

const handleCreated = (editor) => {
  editorRef.value = editor;
};

// 切换编辑模式
const switchMode = (mode) => {
  if (mode === 'markdown' && editorMode.value === 'wysiwyg') {
    // 从富文本切换到Markdown时，转换HTML为Markdown
    if (editorRef.value) {
      const html = editorRef.value.getHtml();
      // 简单的HTML到Markdown转换（这里可以集成更复杂的转换库）
      markdownContent.value = convertHtmlToMarkdown(html);
    }
  } else if (mode === 'wysiwyg' && editorMode.value === 'markdown') {
    // 从Markdown切换到富文本时，转换Markdown为HTML
    if (markdownContent.value) {
      valueHtml.value = marked(markdownContent.value);
    }
  }
  editorMode.value = mode;
};

// 切换预览
const togglePreview = () => {
  showPreview.value = !showPreview.value;
};

// 更新Markdown内容
const updateMarkdownContent = () => {
  // 可以在这里添加实时同步逻辑
};

// 简单的HTML到Markdown转换函数
const convertHtmlToMarkdown = (html) => {
  // 这是一个简化的转换，实际项目中可以使用专门的库
  let markdown = html
    .replace(/<h1[^>]*>(.*?)<\/h1>/g, '# $1\n')
    .replace(/<h2[^>]*>(.*?)<\/h2>/g, '## $1\n')
    .replace(/<h3[^>]*>(.*?)<\/h3>/g, '### $1\n')
    .replace(/<strong[^>]*>(.*?)<\/strong>/g, '**$1**')
    .replace(/<em[^>]*>(.*?)<\/em>/g, '*$1*')
    .replace(/<p[^>]*>(.*?)<\/p>/g, '$1\n\n')
    .replace(/<br\s*\/?>/g, '\n')
    .replace(/<[^>]*>/g, '');
  
  return markdown.trim();
};

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor) editor.destroy();
});

// 暴露方法供外部获取编辑器内容
function getEditorContentHtml() {
  if (editorMode.value === 'markdown') {
    return marked(markdownContent.value);
  }
  if (editorRef.value) {
    return editorRef.value.getHtml();
  }
  return '';
}

function getEditorContentText() {
  if (editorMode.value === 'markdown') {
    return markdownContent.value;
  }
  if (editorRef.value) {
    return editorRef.value.getText();
  }
  return '';
}

// 向外暴露方法，方便父组件引用数据
defineExpose({ getEditorContentHtml, getEditorContentText });
</script>

<style scoped>
.editor-wrapper {
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--bg-primary);
  box-shadow: var(--shadow-sm);
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 模式切换工具栏 */
.mode-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) var(--space-4);
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-light);
  flex-shrink: 0;
}

.mode-tabs {
  display: flex;
  gap: var(--space-2);
}

.mode-tab {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  background: transparent;
  border: 1px solid transparent;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.mode-tab:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
}

.mode-tab.active {
  background: var(--primary-light);
  color: var(--primary-color);
  border-color: var(--primary-color);
}

.tab-icon {
  flex-shrink: 0;
}

.preview-toggle {
  display: flex;
  align-items: center;
}

.preview-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  background: var(--bg-tertiary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.preview-btn:hover {
  background: var(--bg-primary);
  color: var(--text-primary);
}

.preview-btn.active {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.btn-icon {
  flex-shrink: 0;
}

/* 富文本编辑器 */
.wysiwyg-editor {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.editor-toolbar {
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
  padding: var(--space-2);
  flex-shrink: 0;
}

.editor-content {
  flex: 1;
  min-height: 500px;
  overflow-y: auto;
  padding: var(--space-4);
  background: var(--bg-primary);
}

/* Markdown编辑器 */
.markdown-editor {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.markdown-container {
  flex: 1;
  display: flex;
  min-height: 500px;
}

.markdown-input {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-right: 1px solid var(--border-light);
}

.markdown-input.half-width {
  flex: 0 0 50%;
}

.input-header {
  padding: var(--space-3) var(--space-4);
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-light);
  flex-shrink: 0;
}

.input-title {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.input-tips {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.tip {
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.markdown-textarea {
  flex: 1;
  border: none;
  outline: none;
  padding: var(--space-4);
  background: var(--bg-primary);
  color: var(--text-primary);
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.6;
  resize: none;
  min-height: 400px;
}

.markdown-textarea::placeholder {
  color: var(--text-tertiary);
}

/* 预览区域 */
.markdown-preview {
  flex: 0 0 50%;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
}

.preview-header {
  padding: var(--space-3) var(--space-4);
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-light);
  flex-shrink: 0;
}

.preview-title {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.preview-content {
  flex: 1;
  padding: var(--space-4);
  overflow-y: auto;
  color: var(--text-primary);
  line-height: 1.6;
}

/* Markdown预览样式 */
.preview-content :deep(h1) {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 var(--space-4) 0;
  color: var(--text-primary);
}

.preview-content :deep(h2) {
  font-size: 1.5rem;
  font-weight: 600;
  margin: var(--space-6) 0 var(--space-3) 0;
  color: var(--text-primary);
}

.preview-content :deep(h3) {
  font-size: 1.25rem;
  font-weight: 600;
  margin: var(--space-4) 0 var(--space-2) 0;
  color: var(--text-primary);
}

.preview-content :deep(p) {
  margin: 0 0 var(--space-3) 0;
  color: var(--text-secondary);
}

.preview-content :deep(strong) {
  font-weight: 600;
  color: var(--text-primary);
}

.preview-content :deep(em) {
  font-style: italic;
  color: var(--text-secondary);
}

.preview-content :deep(ul), .preview-content :deep(ol) {
  margin: var(--space-3) 0;
  padding-left: var(--space-6);
}

.preview-content :deep(li) {
  margin: var(--space-1) 0;
  color: var(--text-secondary);
}

.preview-content :deep(blockquote) {
  margin: var(--space-4) 0;
  padding: var(--space-3) var(--space-4);
  background: var(--bg-secondary);
  border-left: 4px solid var(--primary-color);
  border-radius: var(--radius-sm);
}

.preview-content :deep(blockquote p) {
  margin: 0;
  color: var(--text-secondary);
  font-style: italic;
}

.preview-content :deep(code) {
  background: var(--bg-secondary);
  color: var(--primary-color);
  padding: 2px 6px;
  border-radius: var(--radius-sm);
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 0.875rem;
}

.preview-content :deep(pre) {
  background: var(--bg-secondary);
  padding: var(--space-3);
  border-radius: var(--radius-md);
  overflow-x: auto;
  margin: var(--space-3) 0;
}

.preview-content :deep(pre code) {
  background: none;
  padding: 0;
  color: var(--text-primary);
}

.preview-content :deep(a) {
  color: var(--primary-color);
  text-decoration: none;
}

.preview-content :deep(a:hover) {
  text-decoration: underline;
}

.preview-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-md);
  margin: var(--space-3) 0;
}

.preview-content :deep(hr) {
  border: none;
  border-top: 1px solid var(--border-light);
  margin: var(--space-6) 0;
}

/* 自定义编辑器样式 */
:deep(.w-e-text-container) {
  background: var(--bg-primary) !important;
}

:deep(.w-e-text) {
  background: var(--bg-primary) !important;
  color: var(--text-primary) !important;
  font-family: var(--font-sans) !important;
  font-size: 16px !important;
  line-height: 1.6 !important;
}

:deep(.w-e-toolbar) {
  background: var(--bg-secondary) !important;
  border-bottom: 1px solid var(--border-light) !important;
}

:deep(.w-e-bar-item) {
  color: var(--text-primary) !important;
}

:deep(.w-e-bar-item:hover) {
  background: var(--primary-light) !important;
  color: var(--primary-color) !important;
}

:deep(.w-e-bar-item-active) {
  background: var(--primary-light) !important;
  color: var(--primary-color) !important;
}

:deep(.w-e-panel-container) {
  background: var(--bg-primary) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--radius-md) !important;
  box-shadow: var(--shadow-md) !important;
}

:deep(.w-e-panel-tab-title) {
  color: var(--text-primary) !important;
}

:deep(.w-e-panel-tab-title-active) {
  color: var(--primary-color) !important;
  border-bottom: 2px solid var(--primary-color) !important;
}

:deep(.w-e-panel-tab-content) {
  background: var(--bg-primary) !important;
}

:deep(.w-e-panel-tab-pane) {
  background: var(--bg-primary) !important;
}

:deep(.w-e-panel-tab-pane input) {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-color) !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-sm) !important;
}

:deep(.w-e-panel-tab-pane button) {
  background: var(--primary-color) !important;
  color: white !important;
  border: none !important;
  border-radius: var(--radius-sm) !important;
  padding: var(--space-2) var(--space-3) !important;
}

:deep(.w-e-panel-tab-pane button:hover) {
  background: var(--primary-hover) !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .markdown-container {
    flex-direction: column;
  }
  
  .markdown-input.half-width {
    flex: 0 0 100%;
    border-right: none;
    border-bottom: 1px solid var(--border-light);
  }
  
  .markdown-preview.half-width {
    flex: 0 0 100%;
  }
  
  .editor-content {
    min-height: 300px;
  }
  
  .editor-wrapper {
    height: calc(100vh - 150px);
  }
}
</style>

<!-- 引入编辑器样式 -->
<style src="@wangeditor/editor/dist/css/style.css"></style>