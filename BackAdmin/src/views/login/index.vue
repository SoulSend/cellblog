<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">博客论坛系统登陆</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="Password"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登陆</el-button>

      <!-- <div class="tips">
        <span style="margin-right:20px;">username: admin</span>
        <span> password: any</span>
      </div> -->

    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>



<style lang="scss" scoped>
$bg: #24292e; // GitHub 主题背景色
$github-black: #24292f; // GitHub 风格的黑色
$dark_gray: #586069; // 深灰色，用于图标和文字
$light_gray: #f6f8fa; // 浅灰色，用于标题和输入框文字
$blue: #0366d6; // GitHub 蓝色，用于按钮和链接

.login-container {
  background-image: url('~@/assets/back_image/back.jpg'); // 背景图片路径
  background-size: cover;
  background-position: center;
  min-height: 100vh;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;

  .login-form {
    width: 400px;
    padding: 40px;
    margin: 0 auto;
    background: rgba(255, 255, 255, 0.8);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    display: flex;
    flex-direction: column;
  }

  .el-input {
    display: block;
    width: 100%;
    margin-bottom: 20px;

    input {
      background: #fff;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      padding: 12px 20px;
      color: #333;
      height: 50px;
      line-height: 50px;
      caret-color: $blue;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px #fff inset !important;
        -webkit-text-fill-color: #333 !important;
      }
    }
  }

  .el-form-item {
    margin-bottom: 20px;
    border: none;
    background: transparent;
  }

  .el-button {
    width: 100%;
    background-color: $github-black;
    border-color: $github-black;
    color: #fff;
    height: 50px;
    line-height: 50px;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;

    &:hover {
      background-color: lighten($github-black, 10%);
      border-color: lighten($github-black, 10%);
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    text-align: center;
    margin-bottom: 30px;

    .title {
      font-size: 24px;
      color: $github-black;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}

// 动画和过渡效果
.el-input input {
  transition: border-color 0.3s, box-shadow 0.3s;
}

.el-input input:focus {
  border-color: $blue;
  box-shadow: 0 0 8px rgba($blue, 0.5);
}

.el-button {
  transition: background-color 0.3s, border-color 0.3s;
}
</style>
