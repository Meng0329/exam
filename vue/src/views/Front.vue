<template>
  <div class="apple-container">
    <!-- 顶部通知栏 -->
    <div class="apple-notice" v-if="data.top">
      <div class="notice-content">
        <el-icon><Bell /></el-icon>
        <span class="notice-text">{{ data.top }}</span>
      </div>
    </div>
    
    <!-- 导航栏 -->
    <div class="apple-header" :class="{ 'header-scrolled': isScrolled }">
      <div class="header-content">
        <div class="header-left">
          <img src="@/assets/imgs/logo.png" alt="Logo" class="logo-image">
          <h1 class="site-title">在线考试系统</h1>
        </div>
        
        <nav class="header-nav">
          <el-menu :default-active="router.currentRoute.value.path" 
                  router 
                  mode="horizontal" 
                  class="apple-menu">
            <el-menu-item index="/front/home">首页</el-menu-item>
            <el-menu-item index="/front/exam">在线考试</el-menu-item>
            <el-menu-item index="/front/forum">交流论坛</el-menu-item>
            <el-menu-item index="/front/score">我的成绩</el-menu-item>
            <el-menu-item index="/front/myShare">我的分享</el-menu-item>
            <el-menu-item index="/front/person">个人中心</el-menu-item>
          </el-menu>
        </nav>
        
        <div class="header-right">
          <template v-if="!data.user.id">
            <el-button class="apple-btn login-btn" @click="router.push('/login')">登录</el-button>
            <el-button class="apple-btn register-btn" @click="router.push('/register')">注册</el-button>
          </template>
          <template v-else>
            <el-dropdown class="user-dropdown">
              <div class="user-info">
                <img :src="data.user.avatar" alt="Avatar" class="user-avatar">
                <span class="user-name">{{ data.user.name }}</span>
                <el-icon><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive, ref, onMounted, onUnmounted } from "vue";
import request from "@/utils/request.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  top: '',
  noticeData: []
})

const isScrolled = ref(false)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 0
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    data.noticeData = res.data
    let i = 0
    if (data.noticeData && data.noticeData.length) {
      data.top = data.noticeData[0].content
      setInterval(() => {
        data.top = data.noticeData[i].content
        i++
        if (i === data.noticeData.length) {
          i = 0
        }
      }, 2500)
    }
  })
}

loadNotice()
</script>

<style scoped>
.apple-container {
  min-height: 100vh;
  background-color: #ffffff;
}

.apple-notice {
  background-color: #1d1d1f;
  color: #f5f5f7;
  padding: 12px 0;
  text-align: center;
  font-size: 14px;
}

.notice-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.apple-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: saturate(180%) blur(20px);
  z-index: 1000;
  transition: all 0.3s ease;
  height: 44px;
}

.header-scrolled {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-image {
  height: 32px;
  width: auto;
}

.site-title {
  font-size: 20px;
  font-weight: 600;
  color: #1d1d1f;
}

.apple-menu {
  background: transparent;
  border: none;
}

.apple-menu :deep(.el-menu-item) {
  font-size: 14px;
  color: #1d1d1f;
  height: 44px;
  line-height: 44px;
}

.apple-menu :deep(.el-menu-item.is-active) {
  color: #0066cc;
  font-weight: 500;
}

.apple-btn {
  border: none;
  padding: 8px 16px;
  border-radius: 980px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-btn {
  background-color: transparent;
  color: #0066cc;
}

.login-btn:hover {
  background-color: rgba(0, 102, 204, 0.1);
}

.register-btn {
  background-color: #0066cc;
  color: #ffffff;
  margin-left: 12px;
}

.register-btn:hover {
  background-color: #004499;
}

.user-dropdown {
  height: 44px;
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 980px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.user-name {
  font-size: 14px;
  color: #1d1d1f;
  font-weight: 500;
}

.main-content {
  padding-top: 0;
  min-height: calc(100vh - 44px);
  margin-top: 44px;
}

.apple-notice + .apple-header {
  top: 40px;
}

.apple-notice + .apple-header + .main-content {
  margin-top: 84px;
}

.apple-notice {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1001;
  background-color: #1d1d1f;
  color: #f5f5f7;
  padding: 10px 0;
  text-align: center;
  font-size: 14px;
  height: 40px;
}

:deep(.el-dropdown-menu) {
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

:deep(.el-dropdown-menu__item) {
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 8px;
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: rgba(0, 0, 0, 0.05);
}
</style>