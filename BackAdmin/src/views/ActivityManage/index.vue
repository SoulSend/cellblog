<template>
  <div class="manage-activity">
    <el-table :data="activities" style="width: 100%">
      <el-table-column prop="id" label="活动ID" width="100"></el-table-column>
      <el-table-column prop="title" label="活动标题"></el-table-column>
      <el-table-column prop="description" label="活动描述"></el-table-column>
      <el-table-column prop="poster" label="活动海报" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.poster" alt="活动海报" style="width: 100%; height: auto;">
        </template>
      </el-table-column>
      <el-table-column prop="publisher" label="发布者"></el-table-column>
      <el-table-column prop="startTime" label="开始时间"></el-table-column>
      <el-table-column prop="endTime" label="结束时间"></el-table-column>
      <el-table-column prop="participants" label="参与人数"></el-table-column>
      <el-table-column prop="views" label="浏览次数"></el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="large" @click="editActivity(scope.row)">修改</el-button>
          <el-button size="large" type="danger" @click="confirmRemove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" title="修改活动信息" custom-class="activity-edit-dialog">
      <el-form :model="currentActivity" ref="activityForm" label-width="120px">
        <el-form-item label="活动标题" :rules="{ required: true, message: '请输入活动标题', trigger: 'blur' }">
          <el-input v-model="currentActivity.title"></el-input>
        </el-form-item>
        <el-form-item label="活动描述" :rules="{ required: true, message: '请输入活动描述', trigger: 'blur' }">
          <el-input type="textarea" v-model="currentActivity.description"></el-input>
        </el-form-item>
        <el-form-item label="活动海报" :rules="{ required: true, message: '请输入活动海报链接', trigger: 'blur' }">
          <el-input v-model="currentActivity.poster"></el-input>
        </el-form-item>
        <el-form-item label="发布者" :rules="{ required: true, message: '请输入发布者名称', trigger: 'blur' }">
          <el-input v-model="currentActivity.publisher"></el-input>
        </el-form-item>
        <el-form-item label="开始时间" :rules="{ required: true, type: 'date', message: '请选择开始时间', trigger: 'change' }">
          <el-date-picker v-model="currentActivity.startTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" :rules="{ required: true, type: 'date', message: '请选择结束时间', trigger: 'change' }">
          <el-date-picker v-model="currentActivity.endTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="详细介绍">
          <el-input type="textarea" v-model="currentActivity.detailIntroduction"></el-input>
        </el-form-item>
        <el-form-item label="参与链接">
          <el-input v-model="currentActivity.participationUrl"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUpdate">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchActivities, removeActivity, updateActivity, getActivityDetail} from '@/api/activity';
import { Message, MessageBox } from 'element-ui';

export default {
  data() {
    return {
      activities: [],
      dialogVisible: false,
      currentActivity: {}
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      fetchActivities().then(response => {
        if (response.code === 200) {
          this.activities = response.data;
        }
      });
    },
    editActivity(activity) {
      this.currentActivity = { ...activity };
      this.dialogVisible = true;
    },
    confirmRemove(id) {
      MessageBox.confirm('此操作将永久删除该活动, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.removeActivity(id);
      }).catch(() => {
        Message.info('已取消删除');
      });
    },
    removeActivity(id) {
      removeActivity(id).then(response => {
        if (response.code === 200) {
          Message.success('删除成功');
          this.fetchData(); // 重新加载数据
        }
      });
    },
    submitUpdate() {
      this.$refs.activityForm.validate((valid) => {
        if (valid) {
          updateActivity(this.currentActivity).then(response => {
            if (response.code === 200) {
              Message.success('更新成功');
              this.dialogVisible = false; // 关闭对话框
              this.fetchData(); // 重新加载数据
            }
          });
        }
      });
    },
    editActivity(activity) {
      getActivityDetail(activity.id).then(response => {
        if (response.code === 200) {
          this.currentActivity = response.data; // 更新 currentActivity 为完整的活动详情
          this.dialogVisible = true; // 显示对话框
        }
      });
    },
  }
};
</script>

<style scoped>
.manage-activity {
  padding: 20px;
}
.activity-edit-dialog .el-dialog__body {
  padding: 20px;
}
.activity-edit-form {
  margin-bottom: 20px;
}
.el-button {
  padding: 10px 20px; /* 增大按钮的内边距 */
}
</style>