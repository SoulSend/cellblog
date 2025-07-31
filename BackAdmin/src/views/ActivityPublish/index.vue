<template>
  <div class="publish-container">
    <el-card>
      <div slot="header">发布活动</div>
      <el-form ref="activityForm" :model="activity" label-width="120px" :rules="rules">
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="activity.title"></el-input>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input type="textarea" v-model="activity.description"></el-input>
        </el-form-item>
        <el-form-item label="活动海报链接" prop="poster">
          <el-input v-model="activity.poster"></el-input>
        </el-form-item>
        <el-form-item label="发布者" prop="publisher">
          <el-input v-model="activity.publisher"></el-input>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="activity.startTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="activity.endTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="详细介绍" prop="detailIntroduction">
          <el-input type="textarea" v-model="activity.detailIntroduction"></el-input>
        </el-form-item>
        <el-form-item label="参与链接" prop="participationUrl">
          <el-input v-model="activity.participationUrl"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { publish } from '@/api/activity'; // 确保路径正确

export default {
  data() {
    return {
      activity: {
        id: 0,
        title: '',
        description: '',
        poster: 'http://localhost:8888/img/1.png',
        publisher: '',
        startTime: '',
        endTime: '',
        detailIntroduction: '',
        participationUrl: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入活动标题', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入活动描述', trigger: 'blur' }
        ],
        poster: [
          { required: true, message: '请输入活动海报链接', trigger: 'blur' }
        ],
        publisher: [
          { required: true, message: '请输入发布者名称', trigger: 'blur' }
        ],
        startTime: [
          { type: 'date', required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { type: 'date', required: true, message: '请选择结束时间', trigger: 'change' }
        ],
        detailIntroduction: [
          { required: true, message: '请输入详细介绍', trigger: 'blur' }
        ],
        participationUrl: [
          { required: true, message: '请输入参与链接', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm() {
      this.$refs.activityForm.validate((valid) => {
        if (valid) {
          console.log('提交活动信息：', this.activity);
          // 调用 publish 方法发送 POST 请求
          publish(this.activity).then(response => {
            // 处理响应
            console.log(response);
            this.$message.success('活动发布成功');
          }).catch(error => {
            // 处理错误
            console.error(error);
            this.$message.error('活动发布失败');
          });
        } else {
          console.log('验证失败');
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.publish-container {
  padding: 20px;
}
</style>