<template>
    <div class="comment-manage">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <div class="search-group">
        <el-input v-model="searchCriteria.articleId" placeholder="输入文章ID搜索评论" @keyup.enter.native="fetchCommentsByArticleId" class="search-input"></el-input>
        <el-button type="primary" @click="fetchCommentsByArticleId" class="search-button">搜索文章评论</el-button>
      </div>
      <div class="search-group">
        <el-input v-model="searchCriteria.userId" placeholder="输入用户ID搜索评论" @keyup.enter.native="fetchCommentsByUserId" class="search-input"></el-input>
        <el-button type="primary" @click="fetchCommentsByUserId" class="search-button">搜索用户评论</el-button>
      </div>
      <el-button type="warning" @click="fetchReportedComments" class="search-button" style="margin-left: 20px;">审核被举报评论</el-button>
    </div>

    <!-- 评论列表 -->
    <el-table :data="comments" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="评论ID" width="180"></el-table-column>
      <el-table-column label="作者" width="180">
        <template slot-scope="scope">
          <img :src="scope.row.author.avatar" alt="作者头像" class="avatar">
          <span>{{ scope.row.author.nickname }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评论内容"></el-table-column>
      <el-table-column label="回复用户" width="180">
        <template slot-scope="scope">
          <div v-if="scope.row.toUser">
            <img :src="scope.row.toUser.avatar" alt="回复用户头像" class="avatar">
            <span>{{ scope.row.toUser.nickname }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createDate" label="评论时间" width="180">
        <template slot-scope="scope">
          <span>{{ new Date(scope.row.createDate).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button @click="deleteComment(scope.row.id)" type="danger" size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
// 导入 API 函数
import { fetchComments, fetchCommentsByArticleId, fetchCommentsByUserId, fetchReportedComments, deleteComment } from '@/api/comment';
import { Message, MessageBox } from 'element-ui';

export default {
  data() {
    return {
      comments: [],
      searchCriteria: {
        articleId: '',
        userId: ''
      }
    };
  },
  methods: {
    fetchComments() {
      fetchComments().then(response => {
        if (response.code === 200) {
          this.comments = response.data;
        }
      });
    },
    fetchCommentsByArticleId() {
      this.searchCriteria.userId = ''; // 清空用户名搜索框
      fetchCommentsByArticleId(this.searchCriteria.articleId).then(response => {
        if (response.code === 200) {
          this.comments = response.data;
        }
      });
    },
    fetchCommentsByUserId() {
      this.searchCriteria.articleId = ''; // 清空文章ID搜索框
      fetchCommentsByUserId(this.searchCriteria.userId).then(response => {
        if (response.code === 200) {
          this.comments = response.data;
        }
      });
    },
    fetchReportedComments() {
      this.searchCriteria.articleId = '';
      this.searchCriteria.userId = '';
      fetchReportedComments().then(response => {
        if (response.code === 200) {
          this.comments = response.data;
        }
      });
    },
    deleteComment(id) {
      MessageBox.confirm('此操作将永久删除该评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteComment(id).then(response => {
          if (response.code === 200) {
            Message.success('删除成功');
            this.fetchComments(); // 重新加载数据
          }
        });
      }).catch(() => {
        Message.info('已取消删除');
      });
    }
  },
  created() {
    this.fetchComments(); // 初始加载所有评论
  }
};
</script>

<style scoped>
.comment-manage {
  padding: 20px;
}
.search-bar {
  display: flex;
  align-items: center;
  flex-wrap: wrap; /* 允许换行 */
  margin-bottom: 20px;
}
.search-group {
  margin-right: 50px; /* 搜索组之间的间距 */
}
.search-input {
  width: 350px; /* 搜索输入框的宽度 */
}
.search-button {
  margin-left: 10px; /* 按钮与输入框之间的间距 */
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}
</style>