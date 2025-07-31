<template>
  <div class="user-manage">
    <!-- 搜索框 -->
    <el-input
      placeholder="请输入用户名"
      v-model="searchUsername"
      @input="handleSearch"
      clearable
      style="margin-bottom: 20px"
    >
      <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
    </el-input>

    <el-table :data="users" style="width: 100%">
      <el-table-column prop="id" label="用户ID" width="180"></el-table-column>
      <el-table-column prop="nickname" label="昵称"></el-table-column>
      <el-table-column prop="account" label="账号"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="mobilePhone" label="手机号"></el-table-column>
      <el-table-column prop="avatar" label="头像" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" alt="用户头像" class="avatar">
        </template>
      </el-table-column>
      <el-table-column prop="createDate" label="创建日期">
        <template slot-scope="scope">
          {{ new Date(scope.row.createDate).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column prop="lastLogin" label="最后登录时间">
        <template slot-scope="scope">
          {{ new Date(scope.row.lastLogin).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column prop="password" label="密码"></el-table-column>
      <el-table-column prop="ban" label="是否封禁">
        <template slot-scope="scope">
          {{ scope.row.ban ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button @click="editUser(scope.row)" type="primary" size="mini">修改</el-button>
          <el-button :type="scope.row.ban ? 'success' : 'warning'" size="mini" @click="toggleBan(scope.row)">
            {{ scope.row.ban ? '解禁' : '封禁' }}
          </el-button>
          <el-button @click="deleteUser(scope.row.id)" type="danger" size="mini">删除</el-button>
          <el-button type="info" size="mini" @click="changePassword(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改用户信息的对话框 -->
    <el-dialog :visible.sync="dialogVisible" title="修改用户信息">
      <el-form :model="modifyUserParam" ref="modifyUserForm" label-width="120px">
        <el-form-item label="昵称" :rules="{ required: true, message: '请输入昵称', trigger: 'blur' }">
          <el-input v-model="modifyUserParam.nickname"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="modifyUserParam.avatar"></el-input>
        </el-form-item>
        <el-form-item label="账号" :rules="{ required: true, message: '请输入账号', trigger: 'blur' }">
          <el-input v-model="modifyUserParam.account"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" :rules="{ required: true, type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }">
          <el-input v-model="modifyUserParam.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号" :rules="{ required: true, pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }">
          <el-input v-model="modifyUserParam.mobilePhone"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitModify">提交</el-button>
      </div>
    </el-dialog>

    <!-- 修改密码的对话框 -->
    <el-dialog :visible.sync="passwordDialogVisible" title="修改密码">
      <el-form :model="modifyUserParam" ref="passwordForm" label-width="120px">
        <el-form-item label="新密码" :rules="{ required: true, message: '请输入新密码', trigger: 'blur' }">
          <el-input type="password" v-model="modifyUserParam.newPassword"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPasswordChange">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchUsers, deleteUser, banUser, updateUser, changePassword } from '@/api/user';
import { Message, MessageBox } from 'element-ui';

export default {
  data() {
    return {
      users: [],
      dialogVisible: false,
      passwordDialogVisible: false,
      modifyUserParam: {
        id: null,
        nickname: '',
        avatar: '',
        account: '',
        email: '',
        mobilePhone: '',
        newPassword: '',
      },
      searchUsername: '', // 搜索框绑定的数据，默认为null
    };
  },
  created() {
    // 组件创建时，使用搜索框的内容（默认为null）查询所有用户信息
    this.fetchUsers(this.searchUsername);
  },
  methods: {
    fetchUsers(username) {
      // 使用 fetchUsers API 获取用户信息
      fetchUsers(username).then(response => {
        if (response.code === 200) {
          this.users = response.data.map(user => ({
            ...user,
            mobilePhone: user.mobilePhoneNumber || user.mobilePhone, // 确保 mobilePhone 始终有值
          }));
        } else {
          Message.error('获取用户信息失败');
        }
      }).catch(error => {
        console.error('获取用户信息失败:', error);
        Message.error('获取用户信息失败');
      });
    },
    handleSearch() {
      // 用户点击搜索或输入搜索内容时触发
      this.fetchUsers(this.searchUsername);
    },
    deleteUser(id) {
      MessageBox.confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(id).then(response => {
          if (response.code === 200) {
            Message.success('删除成功');
            this.fetchUsers(this.searchUsername);
          }
        });
      }).catch(() => {
        Message.info('已取消删除');
      });
    },
    toggleBan(user) {
      const action = user.ban ? '解禁' : '封禁';
      MessageBox.confirm(`${action}此用户将改变其状态, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        banUser(user.id).then(response => {
          if (response.code === 200) {
            Message.success(`${action}成功`);
            this.fetchUsers(this.searchUsername);
          }
        });
      }).catch(() => {
        Message.info('已取消操作');
      });
    },
    editUser(user) {
      this.modifyUserParam = { ...user };
      this.dialogVisible = true;
    },
    submitModify() {
      updateUser(this.modifyUserParam).then(response => {
        if (response.code === 200) {
          Message.success('修改成功');
          this.dialogVisible = false;
          this.fetchUsers(this.searchUsername); // 刷新用户列表
        }
      }).catch(error => {
        console.error('更新用户信息失败:', error);
        Message.error('修改失败');
      });
    },
    changePassword(user) {
      this.modifyUserParam = { ...user, newPassword: '' }; // 初始化 newPassword
      this.passwordDialogVisible = true;
    },
    submitPasswordChange() {
      changePassword(this.modifyUserParam).then(response => {
        if (response.code === 200) {
          Message.success('密码修改成功');
          this.passwordDialogVisible = false;
          this.modifyUserParam.newPassword = ''; // 清空新密码字段
        } else {
          Message.error('密码修改失败');
        }
      });
    },
  }
};
</script>

<style scoped>
.user-manage {
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