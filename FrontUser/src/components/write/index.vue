<template>
  <div style="border: 1px solid #ccc">
    <!-- 工具栏 -->
    <Toolbar
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      style="border-bottom: 1px solid #ccc"
    />
    <!-- 编辑器 -->
    <Editor
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      style="height: 500px; overflow-y: hidden;"
      @onCreated="handleCreated"
    />
  </div>
</template>

<script setup>
import { onBeforeUnmount, ref, shallowRef, onMounted } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';

const editorRef = shallowRef(null);
const valueHtml = ref('');

// 编辑器配置
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: 'http://localhost:8888/upload', // 上传图片到服务器的 API 地址
      fieldName: 'image', // 如果是使用 FormData 方式上传，请设置该字段
    },
  },
};

// 工具栏配置
const toolbarConfig = {
  // 这里配置你的工具栏选项
  menuConf: [
    // 例如，配置加粗按钮
    {
      name: 'bold',
      iconSvg: '<svg>...</svg>',
      hotkey: 'Ctrl+B',
      command: 'bold',
      title: '加粗',
    },
    // 其他工具栏按钮配置...
  ],
};

const handleCreated = (editor) => {
  editorRef.value = editor; // 记录 editor 实例，重要！
};

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor) editor.destroy();
});

// 暴露一个方法供外部获取编辑器内容
function getEditorContentHtml() {
  if (editorRef.value) {
    return editorRef.value.getHtml(); // 使用 getHtml() 方法获取内容
  }
  return '';
}

function getEditorContentText() {
  if (editorRef.value) {
    return editorRef.value.getText(); // 使用 getText() 方法获取内容
  }
  return '';
}
//向外暴露方法，方便父组件引用数据
defineExpose({ getEditorContentHtml, getEditorContentText });
</script>

<!-- 别忘了引入样式 -->
<style src="@wangeditor/editor/dist/css/style.css"></style>