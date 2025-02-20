<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <img src="@/assets/imgs/logo.png" alt="Logo" class="logo">
        <h1>欢迎使用在线考试系统</h1>
        <p class="subtitle">Sign in to continue</p>
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" class="login-form">
        <el-form-item prop="username">
          <el-input 
            :prefix-icon="User" 
            size="large" 
            v-model="data.form.username" 
            placeholder="请输入账号"
            class="apple-input">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            show-password 
            :prefix-icon="Lock" 
            size="large" 
            v-model="data.form.password" 
            placeholder="请输入密码"
            class="apple-input">
          </el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select 
            size="large" 
            v-model="data.form.role" 
            placeholder="请选择角色"
            class="apple-select">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="TEACHER" label="教师"></el-option>
            <el-option value="STUDENT" label="学生"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button 
            size="large" 
            type="primary" 
            class="apple-button" 
            @click="login">登 录</el-button>
        </el-form-item>
        <div class="register-link">
          还没有账号？<a href="/register" class="apple-link">立即注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: {},
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请荀泽角色', trigger: 'blur' }
    ],
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // 表示表单校验通过
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          // 存储用户信息到浏览器的缓存
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          setTimeout(() => {
            if ('STUDENT' === res.data.role) {
              location.href = '/front/home'
            } else {
              location.href = '/manager/home'
            }
          }, 500)
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
  background-position: center;
  position: relative;
  transition: all 0.3s ease;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.login-box {
  width: 400px;
  padding: 40px;
  border-radius: 18px;
  background-color: rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 64px;
  height: 64px;
  margin-bottom: 16px;
}

.login-header h1 {
  font-size: 24px;
  font-weight: 500;
  color: #1d1d1f;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 16px;
  color: #86868b;
  margin-bottom: 24px;
}

.login-form {
  margin-top: 20px;
}

.apple-input :deep(.el-input__wrapper) {
  background-color: rgba(245, 245, 247, 0.9);
  border: none;
  border-radius: 12px;
  padding: 12px;
  transition: all 0.3s ease;
}

.apple-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #0066cc;
  background-color: #ffffff;
}

.apple-input :deep(.el-input__wrapper.is-focus) ~ .login-container::before {
  background: rgba(0, 0, 0, 0.5);
}

/* 添加输入焦点时的遮罩效果 */
.login-form:focus-within ~ .login-container::before,
.login-form:focus-within .login-container::before {
  background: rgba(0, 0, 0, 0.5);
}

.login-form:focus-within .login-box {
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.2);
  transform: translateY(-2px);
}

.apple-select {
  width: 100%;
}

.apple-select :deep(.el-input__wrapper) {
  background-color: rgba(245, 245, 247, 0.9);
  border: none;
  border-radius: 12px;
  padding: 12px;
  transition: all 0.3s ease;
}

.apple-select :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #0066cc;
  background-color: #ffffff;
}

.apple-button {
  width: 100%;
  height: 48px;
  background: #0066cc;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  margin-top: 16px;
  transition: all 0.3s ease;
}

.apple-button:hover {
  background: #0056b3;
  transform: translateY(-1px);
}

.register-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #86868b;
}

.apple-link {
  color: #0066cc;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
}

.apple-link:hover {
  color: #0056b3;
  text-decoration: underline;
}
</style>