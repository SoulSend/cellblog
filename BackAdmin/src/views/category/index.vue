<template>
  <div class="category-manage">
    <!-- <el-button type="primary" @click="showAddCategoryDialog">添加分类</el-button> -->

    <el-table :data="categories" style="width: 100%">
      <el-table-column prop="id" label="分类ID" width="180"></el-table-column>
      <el-table-column prop="avatar" label="头像" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" alt="分类头像" class="avatar">
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="分类名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="220">
        <template slot-scope="scope">
          <el-button @click="editCategory(scope.row)" type="primary" size="mini">修改</el-button>
          <!-- <el-button @click="deleteCategory(scope.row.id)" type="danger" size="mini">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/修改分类的对话框 -->
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle">
      <el-form :model="categoryParam" ref="categoryForm" label-width="120px">
        <el-form-item label="头像">
          <el-input v-model="categoryParam.avatar"></el-input>
        </el-form-item>
        <el-form-item label="分类名称" :rules="{ required: true, message: '请输入分类名称', trigger: 'blur' }">
          <el-input v-model="categoryParam.categoryName"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="categoryParam.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCategory">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchCategories, updateCategory, deleteCategory, addCategory } from '@/api/category';
import { Message, MessageBox } from 'element-ui';

export default {
  data() {
    return {
      categories: [],
      dialogVisible: false,
      dialogTitle: '添加分类',
      categoryParam: {
        id: null,
        avatar: '',
        categoryName: '',
        description: ''
      }
    };
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    fetchCategories() {
      fetchCategories().then(response => {
        if (response.code === 200) {
          this.categories = response.data;
        }
      });
    },
    showAddCategoryDialog() {
      this.dialogTitle = '添加分类';
      this.categoryParam = { avatar: '', categoryName: '', description: '' };
      this.dialogVisible = true;
    },
    editCategory(category) {
      this.dialogTitle = '修改分类';
      this.categoryParam = { ...category };
      this.dialogVisible = true;
    },
    submitCategory() {
      if (this.categoryParam.id) {
        updateCategory(this.categoryParam).then(response => {
          if (response.code === 200) {
            Message.success('分类更新成功');
            this.fetchCategories();
            this.dialogVisible = false;
          }
        });
      } else {
        addCategory(this.categoryParam).then(response => {
          if (response.code === 200) {
            Message.success('分类添加成功');
            this.fetchCategories();
            this.dialogVisible = false;
          }
        });
      }
    },
    deleteCategory(id) {
      MessageBox.confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCategory(id).then(response => {
          if (response.code === 200) {
            Message.success('分类删除成功');
            this.fetchCategories();
          }
        });
      }).catch(() => {
        Message.info('已取消删除');
      });
    }
  }
};
</script>

<style scoped>
.category-manage {
  padding: 20px;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}
.dialog-footer {
  text-align: right;
}
</style>