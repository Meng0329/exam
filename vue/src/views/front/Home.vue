<template>
  <div class="home-container">
    <!-- Hero Section -->
    <div class="hero-section">
      <el-carousel height="600px" indicator-position="none" :interval="5000" arrow="always">
        <el-carousel-item v-for="item in data.carouselData" :key="item">
          <div class="carousel-content">
            <img :src="item" :alt="'轮播图片'" />
            <div class="carousel-overlay">
              <h1 class="hero-title">在线考试新体验</h1>
              <p class="hero-subtitle">简单、高效、安全的考试平台</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- Main Content -->
    <div class="content-container">
      <!-- Ongoing Exams Section -->
      <section class="section-exams">
        <div class="section-header">
          <h2 class="section-title">正在进行中的考试</h2>
          <button class="apple-link" @click="navTo('/front/exam')">
            查看更多 <el-icon><ArrowRight /></el-icon>
          </button>
        </div>

        <div class="exam-grid">
          <el-row :gutter="24">
            <el-col :span="6" v-for="item in data.examData" :key="item.id">
              <div class="exam-card" @click="navToTestPaper(item)">
                <div class="exam-image" :style="{ backgroundImage: `url(${item.courseImg})` }"></div>
                <div class="exam-content">
                  <h3 class="exam-title">{{ item.name }}</h3>
                  <div class="exam-info">
                    <div class="teacher-info">
                      <img :src="item.teacherAvatar" :alt="item.teacherName" class="teacher-avatar">
                      <span class="teacher-name">{{ item.teacherName }}</span>
                    </div>
                    <el-tag 
                      :type="item.status === '进行中' ? 'success' : item.status === '未开始' ? 'warning' : 'danger'"
                      class="exam-status"
                    >
                      {{ item.status }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </section>

      <!-- Forum and Schedule Section -->
      <div class="dual-section">
        <!-- Forum Section -->
        <section class="section-forum">
          <div class="section-header">
            <h2 class="section-title">精选分享</h2>
            <button class="apple-link" @click="navTo('/front/forum')">
              查看更多 <el-icon><ArrowRight /></el-icon>
            </button>
          </div>

          <div class="forum-grid">
            <el-row :gutter="24">
              <el-col :span="8" v-for="item in data.articleData" :key="item.id">
                <div class="forum-card" @click="navTo('/front/forumDetail?id=' + item.id)">
                  <div class="forum-image" :style="{ backgroundImage: `url(${item.img})` }"></div>
                  <div class="forum-content">
                    <h3 class="forum-title">{{ item.title }}</h3>
                    <div class="forum-info">
                      <div class="author-info">
                        <img :src="item.studentAvatar" :alt="item.studentName" class="author-avatar">
                        <span class="author-name">{{ item.studentName }}</span>
                      </div>
                      <span class="post-time">{{ item.time }}</span>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </section>

        <!-- Schedule Section -->
        <section class="section-schedule">
          <h2 class="section-title">考试安排</h2>
          <div class="schedule-card">
            <div v-for="item in data.examPlanData" 
                 :key="item.id" 
                 class="schedule-item"
                 @click="viewContent(item)">
              <span class="schedule-title">{{ item.title }}</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </section>
      </div>
    </div>

    <!-- Detail Dialog -->
    <el-dialog 
      v-model="data.dialogVisible" 
      title="详细信息" 
      width="40%" 
      destroy-on-close
      class="apple-dialog">
      <div class="dialog-content">
        <p class="dialog-text">{{ data.form.content }}</p>
        <div class="dialog-time">发布时间：{{ data.form.time }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import carousel_1 from "@/assets/imgs/carousel-1.jpg";
import carousel_2 from "@/assets/imgs/carousel-2.jpg";
import carousel_3 from "@/assets/imgs/carousel-3.jpg";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { ArrowRight } from '@element-plus/icons-vue'

const data = reactive({
  carouselData: [carousel_1, carousel_2, carousel_3],
  examData: [],
  articleData: [],
  examPlanData: [],
  dialogVisible: false,
  form: {}
})

const loadTestPaper = () => {
  request.get('/testPaper/selectRandom').then(res => {
    if (res.code === '200') {
      data.examData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadArticle = () => {
  request.get('/article/selectRandom').then(res => {
    if (res.code === '200') {
      data.articleData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadExamPlan = () => {
  request.get('/examPlan/selectAll').then(res => {
    if (res.code === '200') {
      if (res.data.length > 5) {
        data.examPlanData = res.data.slice(0, 5)
      } else {
        data.examPlanData = res.data
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const viewContent = (item) => {
  data.form = JSON.parse(JSON.stringify(item))
  data.dialogVisible = true
}

const navTo = (url) => {
  location.href = url
}

const navToTestPaper = (item) => {
  if (item.status === '未开始') {
    ElMessage.warning('该考试还未开放')
    return
  }
  if (item.status === '已结束') {
    ElMessage.warning('该考试已结束')
    return
  }
  request.get('/testPaper/check/' + item.id).then(res => {
    if (res.code === '200') {
      location.href = '/front/testPaper?id=' + item.id
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadTestPaper()
loadArticle()
loadExamPlan()
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #ffffff;
}

/* Hero Section */
.hero-section {
  position: relative;
  margin-bottom: 60px;
  overflow: hidden; /* 防止图片溢出 */
}

.carousel-content {
  height: 600px;
  width: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 轮播图样式调整 */
:deep(.el-carousel),
:deep(.el-carousel__container) {
  height: 600px !important;
  width: 100%;
}

:deep(.el-carousel__item) {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

:deep(.el-carousel__item img) {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.carousel-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.6));
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #ffffff;
  padding: 0 20px;
  backdrop-filter: blur(2px);
}

.hero-title {
  font-size: 56px;
  font-weight: 600;
  margin-bottom: 20px;
  letter-spacing: -0.5px;
  text-align: center;
  padding: 0 20px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.hero-subtitle {
  font-size: 24px;
  font-weight: 400;
  opacity: 0.9;
  text-align: center;
  padding: 0 20px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* Content Container */
.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Section Styles */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  position: relative;
}

.section-header::after {
  content: '';
  position: absolute;
  bottom: -20px;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, #d2d2d7 0%, rgba(210, 210, 215, 0) 100%);
}

.section-title {
  font-size: 40px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: -0.5px;
}

.apple-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #0066cc;
  font-size: 17px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 980px;
  transition: all 0.3s ease;
}

.apple-link:hover {
  background-color: rgba(0, 102, 204, 0.1);
}

/* Exam Cards */
.exam-grid {
  margin-top: 40px;
}

.exam-card {
  background: #ffffff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  cursor: pointer;
  margin-bottom: 30px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  position: relative;
}

.exam-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.8) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: 1;
}

.exam-card:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.exam-card:hover::before {
  opacity: 1;
}

.exam-image {
  height: 240px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.exam-image::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.2) 100%);
}

.exam-content {
  padding: 24px;
  position: relative;
  z-index: 2;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
}

.exam-title {
  font-size: 20px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 16px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  letter-spacing: -0.5px;
}

.exam-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.teacher-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
}

.teacher-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
}

.teacher-name {
  font-size: 14px;
  color: #1d1d1f;
  font-weight: 500;
}

/* Status Tags */
:deep(.el-tag) {
  border-radius: 980px;
  padding: 6px 16px;
  font-size: 13px;
  font-weight: 500;
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

:deep(.el-tag--success) {
  background: #E8F5E9;
  color: #2E7D32;
}

:deep(.el-tag--warning) {
  background: #FFF3E0;
  color: #EF6C00;
}

:deep(.el-tag--danger) {
  background: #FFEBEE;
  color: #C62828;
}

/* Forum Section */
.section-forum {
  margin-top: 60px;
}

.forum-grid {
  margin-top: 40px;
}

.forum-card {
  background: #ffffff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  cursor: pointer;
  margin-bottom: 30px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.forum-card:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.forum-image {
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.forum-image::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.2) 100%);
}

.forum-content {
  padding: 24px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
}

.forum-title {
  font-size: 18px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 16px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  letter-spacing: -0.5px;
}

.forum-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
}

.author-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
}

.author-name {
  font-size: 14px;
  color: #1d1d1f;
  font-weight: 500;
}

.post-time {
  font-size: 13px;
  color: #86868b;
  font-weight: 500;
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
}

/* Schedule Section */
.section-schedule {
  background: #f5f5f7;
  border-radius: 20px;
  padding: 30px;
  margin-left: 40px;
  flex: 0 0 320px;
  align-self: flex-start;
  position: sticky;
  top: 100px;
}

.schedule-card {
  margin-top: 24px;
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.04);
}

.schedule-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
  background: #ffffff;
}

.schedule-item:last-child {
  border-bottom: none;
}

.schedule-item:hover {
  background-color: #f5f5f7;
}

.schedule-title {
  font-size: 15px;
  color: #1d1d1f;
  font-weight: 500;
  flex: 1;
  margin-right: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Dialog Styles */
:deep(.el-dialog) {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

:deep(.el-dialog__header) {
  padding: 20px 24px;
  margin: 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
}

:deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #1d1d1f;
  letter-spacing: -0.5px;
}

:deep(.el-dialog__body) {
  padding: 24px;
  font-size: 15px;
  line-height: 1.6;
  color: #1d1d1f;
}

.dialog-content {
  padding: 0;
}

.dialog-text {
  font-size: 15px;
  line-height: 1.6;
  color: #1d1d1f;
  margin: 0 0 20px 0;
}

.dialog-time {
  font-size: 13px;
  color: #86868b;
  padding: 6px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
  display: inline-block;
}

/* Dual Section Layout */
.dual-section {
  display: flex;
  gap: 40px;
  margin-top: 60px;
}

.section-forum {
  flex: 1;
}
</style>