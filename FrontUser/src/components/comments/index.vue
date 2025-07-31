<template>
  <div class="comments-container">
    <div class="new-comment">
      <textarea v-model="newCommentContent" placeholder="天青色等烟雨，评论区在等你..." rows="3"></textarea>
      <button @click="submitNewComment" class="submit-button">提交</button>
    </div>
    <div v-if="comments.length === 0" class="no-comments">
      暂无评论
    </div>

    <div v-for="comment in comments" :key="comment.id" class="comment">
      <div class="comment-header">
        <img :src="comment.author.avatar" alt="avatar" class="comment-avatar" />
        <div class="comment-main">
          <div class="comment-author-info">
            <span class="comment-author">{{ comment.author.nickname }}</span>
            <time class="comment-date">{{ formatDate(comment.createDate) }}</time>
          </div>
          <div class="comment-actions">
            <!-- 将回复按钮改为可点击的文本 -->
            <span @click="toggleReplyBox(comment)" class="reply-link">{{ comment.replyBoxVisible ? '取消回复' : '回复' }}</span>
            <!-- 添加举报按钮 -->
            <span @click="reportComment(comment.id)" class="report-link">举报</span>
          </div>
        </div>
      </div>

      <div class="comment-content">{{ comment.content }}</div>

      <!-- 移除子评论的回复功能 -->
      <div v-if="comment.childrens && comment.childrens.length > 0" class="comment-replies">
        <div v-for="reply in comment.childrens" :key="reply.id" class="reply">
          <div class="reply-header">
            <img :src="reply.author.avatar" alt="avatar" class="reply-avatar" />
            <div class="reply-main">
              <div class="reply-author-info">
                <span class="reply-author">{{ reply.author.nickname }}</span>
                <time class="reply-date">{{ formatDate(reply.createDate) }}</time>
              </div>
            </div>
          </div>
          <div class="reply-content">{{ reply.content }}</div>
        </div>
      </div>

      <!-- 回复框 -->
      <div v-if="comment.replyBoxVisible" class="reply-box">
        <textarea v-model="comment.replyContent" placeholder="唠会嗑..." rows="2"></textarea>
        <button @click="submitComment(comment.id, comment.author.id, comment.replyContent || '')" class="submit-reply-button">回复</button>
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

.new-comment {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.new-comment textarea {
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

.new-comment textarea:focus {
  border-color: #4CAF50;
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
  display: inline-block;
  margin: 0 auto;
}

.submit-button:hover {
  background-color: #45a049;
  transform: translateY(-2px);
}

.no-comments {
  text-align: center;
  color: #888;
  font-size: 20px; /* 增加字体大小 */
  margin: 30px 0; /* 增加外边距 */
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
}

.comment-avatar {
  width: 50px; /* 增加头像大小 */
  height: 50px;
  border-radius: 50%;
  margin-right: 20px; /* 增加右边距 */
  transition: transform 0.3s;
}

.comment-avatar:hover {
  transform: scale(1.05); /* 鼠标悬停悬停时头像放大 */
}

.comment-main {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.comment-author-info {
  display: flex;
  align-items: center;
}

.comment-author {
  font-weight: bold;
  color: #333;
  margin-right: 15px; /* 增加右边距 */
}

.comment-date {
  color: #888;
  font-size: 16px; /* 增加日期字体大小 */
}

.reply-link {
  color: #1fc2b5;
  cursor: pointer;
  transition: color 0.3s;
}

.reply-link:hover, .reply-link.active {
  color: #1fc2b5;
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

.reply-avatar {
  width: 40px; /* 增加头像大小 */
  height: 40px;
  border-radius: 50%;
  margin-right: 20px; /* 增加右边距 */
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
}

.reply-author {
  font-weight: bold;
  color: #333;
  margin-right: 15px; /* 增加右边距 */
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

.reply-box textarea {
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px; /* 增加内边距 */
  font-size: 18px; /* 增加字体大小 */
  transition: border-color 0.3s;
  font-weight: bold; /* 设置文本为加粗 */
  color: #000; /* 设置文本颜色为黑色，与评论文本颜色相同 */
}

.reply-box textarea:focus {
border-color: #4CAF50;
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
margin-top: 10px; /* 增加顶部边距 */
}

.submit-reply-button:hover {
background-color: #45a049;
transform: translateY(-2px);
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
.comment-actions {
  display: flex;
  align-items: center;
}

.report-link {
  margin-left: 10px; /* 增加左边距 */
  color: #d9534f; /* 设置举报链接颜色 */
  cursor: pointer;
  transition: color 0.3s;
}

.report-link:hover {
  color: #c9302c; /* 鼠标悬停时颜色加深 */
}
</style>

