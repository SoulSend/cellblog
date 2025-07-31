<template>
  <div class="article-manage">
    <el-input v-model="searchTitle" placeholder="输入文章标题搜索" class="search-input"></el-input>
    <el-button type="primary" size="medium" @click="fetchArticles">搜索</el-button>

    <el-table :data="articles" border>
      <!-- 列定义 -->
      <el-table-column prop="id" label="文章ID" width="100"></el-table-column>
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="summary" label="摘要"></el-table-column>
      <el-table-column prop="commentCounts" label="评论数"></el-table-column>
      <el-table-column prop="viewCounts" label="浏览量"></el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column prop="category.categoryName" label="分类"></el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button @click="editArticleInfo(scope.row)" type="primary" size="medium">修改信息</el-button>
          <el-button @click="editArticleContent(scope.row)" type="success" size="medium">修改内容</el-button>
          <el-button :type="scope.row.weight === 1 ? 'success' : 'primary'" size="medium" @click="toggleTopArticle(scope.row.id)">
            {{ scope.row.weight === 1 ? '取消置顶' : '置顶文章' }}
          </el-button>
          <el-button type="danger" size="medium" @click="deleteArticle(scope.row.id)">删除文章</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改文章信息对话框 -->
    <el-dialog :visible.sync="editInfoDialogVisible" title="修改文章信息">
      <el-form :model="editArticleInfoForm" label-width="120px">
        <el-form-item label="标题" :rules="{ required: true, message: '请输入标题', trigger: 'blur' }">
          <el-input v-model="editArticleInfoForm.title"></el-input>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input type="textarea" v-model="editArticleInfoForm.summary"></el-input>
        </el-form-item>
        <el-form-item label="评论数">
          <el-input-number v-model="editArticleInfoForm.commentCounts" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="浏览量">
          <el-input-number v-model="editArticleInfoForm.viewCounts" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="作者ID">
          <el-input v-model="editArticleInfoForm.authorId"></el-input>
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input v-model="editArticleInfoForm.categoryId"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editInfoDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditInfo">提交</el-button>
      </div>
    </el-dialog>

    <!-- 修改文章内容对话框 -->
    <el-dialog :visible.sync="editContentDialogVisible" title="修改文章内容">
      <el-form>
        <el-form-item label="内容">
          <textarea v-model="editArticleContentForm.content" rows="10" cols="50" class="content-textarea"></textarea>
        </el-form-item>
        <el-form-item label="HTML内容">
          <textarea v-model="editArticleContentForm.contentHtml" rows="10" cols="50" class="content-textarea"></textarea>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editContentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditContent">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 导入API方法和组件
import { fetchArticles, updateArticleInfo, getArticleContent, updateArticleContent, toggleTopArticle, deleteArticle } from '@/api/article';
import { Message } from 'element-ui';

export default {
  data() {
    return {
      searchTitle: '',
      articles: [],
      editInfoDialogVisible: false,
      editContentDialogVisible: false,
      editArticleInfoForm: {
        id: null,
        title: '',
        summary: '',
        commentCounts: 0,
        viewCounts: 0,
        authorId: null,
        categoryId: null
      },
      editArticleContentForm: {
        articleId: null,
        content: '',
        contentHtml: ''
      }
    };
  },
  methods: {
    fetchArticles() {
      fetchArticles(this.searchTitle).then(response => {
        if (response && response.code === 200) {
          this.articles = response.data;
        } else {
          Message.error('获取文章列表失败');
        }
      }).catch(error => {
        console.error('获取文章列表时发生错误:', error);
        Message.error('获取文章列表失败');
      });
    },
    editArticleInfo(article) {
      this.editArticleInfoForm = { ...article };
      this.editInfoDialogVisible = true;
    },
    submitEditInfo() {
      updateArticleInfo(this.editArticleInfoForm).then(response => {
        if (response && response.code === 200) {
          Message.success('信息更新成功');
          this.editInfoDialogVisible = false;
          this.fetchArticles(); // 刷新文章列表
        } else {
          Message.error('信息更新失败');
        }
      }).catch(error => {
        console.error('更新文章信息时发生错误:', error);
        Message.error('信息更新失败');
      });
    },
    editArticleContent(article) {
      getArticleContent(article.id).then(response => {
        if (response && response.code === 200) {
          this.editArticleContentForm = {
            articleId: article.id,
            content: response.data.content,
            contentHtml: response.data.contentHtml
          };
          this.editContentDialogVisible = true;
        } else {
          Message.error('获取内容失败');
        }
      }).catch(error => {
        console.error('获取文章内容时发生错误:', error);
        Message.error('获取内容失败');
      });
    },
    submitEditContent() {
      updateArticleContent(this.editArticleContentForm).then(response => {
        if (response && response.code === 200) {
          Message.success('内容更新成功');
          this.editContentDialogVisible = false;
          this.fetchArticles(); // 刷新文章列表
        } else {
          Message.error('内容更新失败');
        }
      }).catch(error => {
        console.error('更新文章内容时发生错误:', error);
        Message.error('内容更新失败');
      });
    },
    toggleTopArticle(id) {
      toggleTopArticle(id).then(response => {
        if (response && response.code === 200) {
          Message.success('操作成功');
          this.fetchArticles(); // 刷新文章列表
        } else {
          Message.error('操作失败');
        }
      }).catch(error => {
        console.error('操作文章时发生错误:', error);
        Message.error('操作失败');
      });
    },
    deleteArticle(id) {
      this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteArticle(id).then(response => {
          if (response && response.code === 200) {
            Message.success('删除成功');
            this.fetchArticles(); // 刷新文章列表
          } else {
            Message.error('删除失败');
          }
        }).catch(error => {
          console.error('删除文章时发生错误:', error);
          Message.error('删除失败');
        });
      }).catch(() => {
        Message.info('已取消删除');
      });
    }
  },
  mounted() {
    this.fetchArticles();
  }
};
</script>

<style scoped>
.article-manage {
  padding: 20px;
}
.search-input {
  width: 300px;
  margin-right: 10px;
}
.el-button {
  margin: 5px;
  padding: 8px 16px; /* 调整内边距以适应四个字的大小 */
  font-size: 14px; /* 设置字体大小 */
}
.dialog-footer {
  text-align: right;
}
.content-textarea {
  width: 100%;
}
</style>