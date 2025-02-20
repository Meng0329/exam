<template>
  <div class="person-center">
    <!-- 页面背景 -->
    <div class="page-background"></div>
    
    <!-- 个人信息卡片 -->
    <div class="profile-card">
      <div class="profile-header">
        <h1 class="profile-title">个人中心</h1>
        <el-button 
          type="primary" 
          class="password-button" 
          @click="updateInit"
        >
          修改密码
        </el-button>
      </div>

      <!-- 个人信息表单 -->
      <el-form 
        ref="user" 
        :model="data.user" 
        label-width="80px" 
        class="profile-form"
      >
        <!-- 头像上传 -->
        <div class="avatar-section">
          <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
          >
            <div class="avatar-container">
              <img 
                v-if="data.user.avatar" 
                :src="data.user.avatar" 
                class="avatar" 
              />
              <div v-else class="avatar-placeholder">
                <el-icon class="avatar-icon"><Plus /></el-icon>
                <span class="upload-text">上传头像</span>
              </div>
              <div class="avatar-hover">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
              </div>
            </div>
          </el-upload>
        </div>

        <!-- 信息表单项 -->
        <el-form-item prop="username" label="用户名">
          <el-input 
            disabled 
            v-model="data.user.username" 
            placeholder="请输入用户名"
            class="apple-input"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="name" label="姓名">
          <el-input 
            v-model="data.user.name" 
            placeholder="请输入姓名"
            class="apple-input"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="phone" label="电话">
          <el-input 
            v-model="data.user.phone" 
            placeholder="请输入电话"
            class="apple-input"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="email" label="邮箱">
          <el-input 
            v-model="data.user.email" 
            placeholder="请输入邮箱"
            class="apple-input"
          ></el-input>
        </el-form-item>

        <div class="form-actions">
          <el-button 
            type="primary" 
            @click="update"
            class="save-button"
          >
            保存修改
          </el-button>
        </div>
      </el-form>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog 
      title="修改密码" 
      v-model="data.formVisible" 
      width="460px" 
      destroy-on-close
      class="password-dialog"
    >
      <el-form 
        ref="formRef" 
        :rules="data.rules" 
        :model="data.user" 
        label-width="90px" 
        class="password-form"
      >
        <el-form-item label="原密码" prop="password">
          <el-input 
            v-model="data.user.password" 
            placeholder="请输入原密码" 
            show-password
            class="apple-input"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            v-model="data.user.newPassword" 
            placeholder="请输入新密码" 
            show-password
            class="apple-input"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="data.user.confirmPassword" 
            placeholder="请确认新密码" 
            show-password
            class="apple-input"
          ></el-input>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button 
            @click="data.formVisible = false"
            class="cancel-button"
          >
            取 消
          </el-button>
          <el-button 
            type="primary" 
            @click="save"
            class="confirm-button"
          >
            确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
import { Plus, Camera } from '@element-plus/icons-vue'

const formRef = ref()
const baseUrl = import.meta.env.VITE_BASE_URL

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else {
    if (value !== data.user.newPassword) {
      callback(new Error("确认密码跟新密码不一致!"))
    }
    callback()
  }
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  rules: {
    password: [
      {required: true, message: '请输入原密码', trigger: 'blur'},
    ],
    newPassword: [
      {required: true, message: '请输入新密码', trigger: 'blur'},
    ],
    confirmPassword: [
      {validator: validatePass, trigger: 'blur'}
    ]
  }
})

const handleFileUpload = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])

const update = () => {
  if (data.user.role === 'STUDENT') {
    request.put('/student/update', data.user).then(res => {
      if (res.code === '200') {
        ElMessage.success('保存成功')
        localStorage.setItem('xm-user', JSON.stringify(data.user))
        emit('updateUser')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const updateInit = () => {
  data.formVisible = true
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.put('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('保存成功')
          localStorage.removeItem('xm-user')
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.person-center {
  min-height: 100vh;
  padding: 40px 20px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.page-background {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #f5f5f7 0%, #ffffff 100%);
  z-index: -1;
}

.profile-card {
  width: 100%;
  max-width: 680px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-4px);
}

.profile-header {
  padding: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.profile-title {
  font-size: 28px;
  font-weight: 600;
  margin: 0;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.password-button {
  height: 36px;
  padding: 0 20px;
  border-radius: 18px;
  font-size: 14px;
  font-weight: 500;
  background: #0066cc;
  border: none;
  transition: all 0.3s ease;
}

.password-button:hover {
  background: #004499;
  transform: translateY(-2px);
}

.profile-form {
  padding: 32px;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 32px;
}

.avatar-container {
  width: 120px;
  height: 120px;
  border-radius: 60px;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.avatar-container:hover {
  transform: scale(1.05);
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background: #f5f5f7;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #86868b;
}

.avatar-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 14px;
}

.avatar-hover {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-container:hover .avatar-hover {
  opacity: 1;
}

.apple-input {
  height: 40px;
  border-radius: 10px;
}

:deep(.el-input__wrapper) {
  background: rgba(0, 0, 0, 0.02);
  border-radius: 10px;
  box-shadow: none !important;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #0066cc;
}

.form-actions {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.save-button {
  width: 160px;
  height: 44px;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 500;
  background: #0066cc;
  border: none;
  transition: all 0.3s ease;
}

.save-button:hover {
  background: #004499;
  transform: translateY(-2px);
}

/* 密码修改对话框样式 */
.password-dialog {
  :deep(.el-dialog) {
    border-radius: 20px;
    overflow: hidden;
  }

  :deep(.el-dialog__header) {
    margin: 0;
    padding: 20px 24px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }

  :deep(.el-dialog__body) {
    padding: 24px;
  }

  :deep(.el-dialog__footer) {
    padding: 16px 24px;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
  }
}

.password-form {
  .apple-input {
    height: 40px;
  }
}

.dialog-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.cancel-button,
.confirm-button {
  height: 36px;
  padding: 0 20px;
  border-radius: 18px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.cancel-button {
  background: #f5f5f7;
  border: none;
  color: #1d1d1f;
}

.confirm-button {
  background: #0066cc;
  border: none;
}

.cancel-button:hover,
.confirm-button:hover {
  transform: translateY(-2px);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .profile-card {
    border-radius: 0;
    box-shadow: none;
  }

  .profile-header {
    padding: 24px;
  }

  .profile-title {
    font-size: 24px;
  }

  .profile-form {
    padding: 24px;
  }
}
</style>