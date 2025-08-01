<template>
  <div class="comments-container">
    <!-- 评论统计 -->
    <div class="comments-header">
      <h3 class="comments-title">
        <svg class="comments-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
        </svg>
        评论 ({{ comments.length }})
      </h3>
    </div>

    <!-- 发表评论 -->
    <div class="new-comment">
      <div class="comment-input-wrapper">
        <textarea 
          v-model="newCommentContent" 
          placeholder="天青色等烟雨，评论区在等你..." 
          rows="3"
          class="comment-textarea"
        ></textarea>
        <div class="comment-actions">
          <button @click="submitNewComment" class="submit-button" :disabled="!newCommentContent.trim()">
            <svg class="submit-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="22" y1="2" x2="11" y2="13"></line>
              <polygon points="22,2 15,22 11,13 2,9"></polygon>
            </svg>
            发表评论
          </button>
        </div>
      </div>
    </div>

    <!-- 暂无评论 -->
    <div v-if="comments.length === 0" class="no-comments">
      <svg class="no-comments-icon" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
      </svg>
      <h3 class="no-comments-title">暂无评论</h3>
      <p class="no-comments-subtitle">来发表第一条评论吧！</p>
    </div>

    <!-- 评论列表 -->
    <div v-else class="comments-list">
      <div v-for="comment in comments" :key="comment.id" class="comment">
        <div class="comment-header">
          <div class="comment-avatar-wrapper">
            <img :src="comment.author.avatar" alt="avatar" class="comment-avatar" />
            <div class="avatar-status"></div>
          </div>
          <div class="comment-main">
            <div class="comment-author-info">
              <span class="comment-author">{{ comment.author.nickname }}</span>
              <span class="comment-badge">作者</span>
              <time class="comment-date">{{ formatDate(comment.createDate) }}</time>
            </div>
            <div class="comment-actions">
              <button @click="toggleReplyBox(comment)" class="action-btn reply-btn">
                <svg class="action-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="9,11 12,14 22,4"></polyline>
                  <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path>
                </svg>
                {{ comment.replyBoxVisible ? '取消回复' : '回复' }}
              </button>
              <button @click="reportComment(comment.id)" class="action-btn report-btn">
                <svg class="action-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
                  <line x1="12" y1="9" x2="12" y2="13"></line>
                  <line x1="12" y1="17" x2="12.01" y2="17"></line>
                </svg>
                举报
              </button>
            </div>
          </div>
        </div>

        <div class="comment-content">{{ comment.content }}</div>

        <!-- 回复列表 -->
        <div v-if="comment.childrens && comment.childrens.length > 0" class="comment-replies">
          <div v-for="reply in comment.childrens" :key="reply.id" class="reply">
            <div class="reply-header">
              <div class="reply-avatar-wrapper">
                <img :src="reply.author.avatar" alt="avatar" class="reply-avatar" />
              </div>
              <div class="reply-main">
                <div class="reply-author-info">
                  <span class="reply-author">{{ reply.author.nickname }}</span>
                  <span v-if="reply.toUser" class="reply-to">
                    回复 @{{ reply.toUser.nickname }}
                  </span>
                  <time class="reply-date">{{ formatDate(reply.createDate) }}</time>
                </div>
              </div>
            </div>
            <div class="reply-content">{{ reply.content }}</div>
          </div>
        </div>

        <!-- 回复框 -->
        <div v-if="comment.replyBoxVisible" class="reply-box">
          <div class="reply-input-wrapper">
            <textarea 
              v-model="comment.replyContent" 
              placeholder="唠会嗑..." 
              rows="2"
              class="reply-textarea"
            ></textarea>
            <div class="reply-actions">
              <button @click="submitComment(comment.id, comment.author.id, comment.replyContent || '')" class="submit-reply-button" :disabled="!comment.replyContent?.trim()">
                <svg class="submit-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="22" y1="2" x2="11" y2="13"></line>
                  <polygon points="22,2 15,22 11,13 2,9"></polygon>
                </svg>
                回复
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, onMounted } from 'vue';
import axios from '@/axios';

interface Author {
  nickname: string;
  avatar: string;
  id: number;
}

interface Comment {
  id: string;
  author: Author;
  content: string;
  childrens?: Comment[];
  createDate: string;
  level?: number;
  toUser?: Author;
  replyBoxVisible?: boolean;
  replyContent?: string | undefined;
}

interface CommentsProps {
  articleId: string;
}

const props = defineProps<CommentsProps>();
const comments = ref<Comment[]>([]);
const newCommentContent = ref('');
// 提示信息
const showAlert = ref(false);
const alertMessage = ref('');

const toggleReplyBox = (comment: Comment) => {
  comment.replyBoxVisible = !comment.replyBoxVisible;
};

const fetchComments = async () => {
  try {
    const response = await axios.get(`http://localhost:8888/comments/article/${props.articleId}`);
    comments.value = response.data.data.map((commentData: any) => ({
      ...commentData,
      replyBoxVisible: false,
      replyContent: ''
    }));
  } catch (error) {
    console.error('Failed to fetch comments:', error);
  }
};

onMounted(async () => {
  await fetchComments(); // 组件挂载时获取评论列表
});

const formatDate = (date: string) => {
  const dateObj = new Date(date);
  return dateObj.toLocaleDateString();
};

const submitComment = async (parentId: string | null, toUserId: number | null, content: string) => {
  if (!content) return; // 如果内容为空，则不提交
  try {
    const response = await axios.post('http://localhost:8888/comments/create/change', {
      articleId: props.articleId,
      content: content,
      parent: parentId !== null ? parentId : '',
      toUserId: toUserId !== null ? toUserId.toString() : ''
    });
    if (response.data.success) {
      // 评论成功后，重新获取评论列表
      await fetchComments();
    } else {
      // 如果后端返回未登录信息，提示用户
      if (response.data.code === 90002) {
        alert("未登录不能写评论哦~~~"); // 显示未登录提示
      } else {
        console.error('Failed to submit comment:', response.data.message);
      }
    }
  } catch (error) {
    console.error('Failed to submit comment:', error);
  }
};

const submitNewComment = async () => {
  if (newCommentContent.value.trim()) {
    await submitComment(null, null, newCommentContent.value.trim());
    newCommentContent.value = ''; // 清空输入框
  }
};

// 添加举报功能
const reportComment = async (commentId: string) => {
  try {
    const response = await axios.put(`http://localhost:8888/comments/report/${commentId}`);
    if (response.data.code === 200) {
      alert('举报成功！');
    } else {
      alert('举报失败，请稍后再试。');
    }
  } catch (error) {
    console.error('Failed to report comment:', error);
    alert('举报失败，请稍后再试。');
  }
};
</script>

<style scoped>
.comments-container {
  margin-top: 20px;
  padding: 0;
  max-width: 100%; /* 增加最大宽度 */
  margin-left: auto;
  margin-right: auto;
  width: 100%; /* 增加宽度百分比 */
  box-sizing: border-box;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.comments-header {
  padding: 20px;
  background-color: #fff;
  border-bottom: 1px solid #eee;
  border-radius: 8px 8px 0 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.comments-title {
  display: flex;
  align-items: center;
  color: #333;
  font-size: 20px;
  font-weight: bold;
}

.comments-icon {
  margin-right: 8px;
  color: #1fc2b5;
}

.new-comment {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.comment-input-wrapper {
  display: flex;
  flex-direction: column;
}

.comment-textarea {
  width: 100%;
  margin-bottom: 10px;
  border: none;
  border-radius: 4px;
  padding: 10px;
  font-size: 18px; /* 增加字体大小 */
  transition: border-color 0.3s;
  outline: none;
  background-color: #fff;
  resize: none;
  font-weight: bold; /* 设置文本为加粗 */
  color: #000; /* 设置文本颜色为黑色，与评论文本颜色相同 */
}

.comment-textarea:focus {
  border-color: #4CAF50;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
}

.submit-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 12px 24px; /* 增加内边距 */
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
  border-radius: 4px;
  font-size: 18px; /* 增加字体大小 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: inline-flex; /* 使用inline-flex使图标和文本在同一行 */
  align-items: center;
  gap: 8px; /* 图标和文本之间的间距 */
}

.submit-button:hover {
  background-color: #45a049;
  transform: translateY(-2px);
}

.submit-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  color: #888;
}

.submit-icon {
  fill: none;
  stroke: currentColor;
  stroke-width: 2;
}

.no-comments {
  text-align: center;
  color: #888;
  font-size: 20px; /* 增加字体大小 */
  margin: 30px 0; /* 增加外边距 */
  padding: 50px 20px; /* 增加内边距 */
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.no-comments-icon {
  color: #1fc2b5;
  margin-bottom: 15px;
}

.no-comments-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.no-comments-subtitle {
  font-size: 16px;
  color: #666;
}

.comments-list {
  padding: 0 20px; /* 增加左右内边距 */
}

.comment {
  padding: 25px; /* 增加内边距 */
  border-bottom: 1px solid #eee;
  display: flex;
  flex-direction: column;
}

.comment:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  width: 100%;
  margin-bottom: 15px; /* 增加底部边距 */
}

.comment-avatar-wrapper {
  position: relative;
  width: 50px; /* 增加头像大小 */
  height: 50px;
  margin-right: 20px; /* 增加右边距 */
}

.comment-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  transition: transform 0.3s;
}

.comment-avatar:hover {
  transform: scale(1.05); /* 鼠标悬停悬停时头像放大 */
}

.avatar-status {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 15px;
  height: 15px;
  background-color: #4CAF50;
  border-radius: 50%;
  border: 2px solid #fff;
}

.comment-main {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.comment-author-info {
  display: flex;
  align-items: center;
  flex-wrap: wrap; /* 允许换行 */
}

.comment-author {
  font-weight: bold;
  color: #333;
  margin-right: 15px; /* 增加右边距 */
}

.comment-badge {
  background-color: #1fc2b5;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  margin-right: 10px;
}

.comment-date {
  color: #888;
  font-size: 16px; /* 增加日期字体大小 */
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 10px; /* 按钮之间的间距 */
}

.action-btn {
  background-color: #f0f0f0;
  color: #333;
  border: none;
  padding: 8px 12px; /* 增加内边距 */
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
  border-radius: 6px;
  font-size: 14px; /* 增加字体大小 */
  display: inline-flex; /* 使图标和文本在同一行 */
  align-items: center;
  gap: 6px; /* 图标和文本之间的间距 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.action-btn:hover {
  background-color: #e0e0e0;
  transform: translateY(-1px);
}

.action-btn:active {
  transform: translateY(0);
}

.action-icon {
  fill: none;
  stroke: currentColor;
  stroke-width: 2;
}

.reply-btn {
  color: #1fc2b5;
}

.reply-btn:hover {
  color: #1fc2b5;
}

.reply-btn.active {
  color: #1fc2b5;
}

.report-btn {
  color: #d9534f; /* 设置举报链接颜色 */
}

.report-btn:hover {
  color: #c9302c; /* 鼠标悬停时颜色加深 */
}

.comment-content {
  margin-top: 15px; /* 增加顶部边距 */
  font-size: 18px; /* 增加内容字体大小 */
  color: #000; /* 设置文本颜色为黑色 */
  font-weight: bold; /* 设置文本为加粗 */
  line-height: 1.8; /* 增加行高 */
}

.comment-replies {
  margin-left: 100px; /* 增加子评论左边距 */
  border-left: 4px solid #4CAF50;
  padding-left: 25px; /* 增加内边距 */
  margin-top: 40px; /* 增加顶部边距 */
}

.reply {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px; /* 增加底部边距 */
}

.reply-header {
  display: flex;
  align-items: center;
  width: 100%;
}

.reply-avatar-wrapper {
  position: relative;
  width: 40px; /* 增加头像大小 */
  height: 40px;
  margin-right: 20px; /* 增加右边距 */
}

.reply-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  transition: transform 0.3s;
}

.reply-avatar:hover {
  transform: scale(1.05); /* 鼠标停悬停时头像放大 */
}

.reply-main {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.reply-author-info {
  display: flex;
  align-items: center;
  flex-wrap: wrap; /* 允许换行 */
}

.reply-author {
  font-weight: bold;
  color: #333;
  margin-right: 15px; /* 增加右边距 */
}

.reply-to {
  color: #1fc2b5;
  font-weight: bold;
  margin-right: 10px;
}

.reply-date {
  color: #888;
  font-size: 16px; /* 增加日期字体大小 */
}

.reply-content {
  margin-top: 15px; /* 增加顶部边距 */
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 8px;
  font-size: 16px; /* 增加内容字体大小 */
  color: #000; /* 设置文本颜色为黑色 */
  font-weight: bold; /* 设置文本为加粗 */
  line-height: 1.8; /* 增加行高 */
}

.reply-box {
margin-top: 15px; /* 增加顶部边距 */
animation: slideDown 0.3s ease;
}

.reply-input-wrapper {
  display: flex;
  flex-direction: column;
}

.reply-textarea {
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px; /* 增加内边距 */
  font-size: 18px; /* 增加字体大小 */
  transition: border-color 0.3s;
  font-weight: bold; /* 设置文本为加粗 */
  color: #000; /* 设置文本颜色为黑色，与评论文本颜色相同 */
}

.reply-textarea:focus {
border-color: #4CAF50;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px; /* 增加顶部边距 */
}

.submit-reply-button {
background-color: #4CAF50;
color: white;
border: none;
padding: 10px 20px; /* 增加内边距 */
cursor: pointer;
transition: background-color 0.3s, transform 0.3s;
border-radius: 8px; /* 增加圆角 */
font-size: 16px; /* 增加字体大小 */
box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
display: inline-flex; /* 使图标和文本在同一行 */
align-items: center;
gap: 8px; /* 图标和文本之间的间距 */
}

.submit-reply-button:hover {
background-color: #45a049;
transform: translateY(-2px);
}

.submit-reply-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  color: #888;
}

.submit-icon {
  fill: none;
  stroke: currentColor;
  stroke-width: 2;
}

@keyframes slideDown {
0% {
opacity: 0;
transform: translateY(-10px);
}
100% {
opacity: 1;
transform: translateY(0);
}
/* new */
}
</style>

