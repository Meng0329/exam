<template>
  <div class="forum-detail">
    <!-- 阅读进度条 -->
    <div class="reading-progress" :style="{ width: readingProgress + '%' }"></div>

    <!-- 文章头部 -->
    <div class="article-header">
      <div class="header-content">
        <nav class="breadcrumb">
          <el-button link @click="navTo('/front/forum')" class="back-button">
            <el-icon><ArrowLeft /></el-icon>
            返回论坛
          </el-button>
        </nav>
        <h1 class="article-title">{{ data.articleData.title }}</h1>
        
        <!-- 作者信息卡片 -->
        <div class="author-card">
          <div class="author-info">
            <img :src="data.articleData.studentAvatar || '/default-avatar.png'" 
                 :alt="data.articleData.studentName" 
                 class="author-avatar">
            <div class="author-details">
              <span class="author-name">{{ data.articleData.studentName }}</span>
              <span class="publish-time">{{ data.articleData.time }}</span>
            </div>
          </div>
          <div class="article-stats">
            <span class="stat-item">
              <el-icon><View /></el-icon>
              {{ data.articleData.views || 0 }} 阅读
            </span>
            <span class="stat-item">
              <el-icon><ChatLineRound /></el-icon>
              {{ data.articleData.comments || 0 }} 评论
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 文章内容 -->
    <div class="article-content" ref="contentRef">
      <div class="content-wrapper" v-html="data.articleData.content"></div>
    </div>

    <!-- 评论区 -->
    <div class="comments-section">
      <h2 class="section-title">评论区</h2>
      <div class="comment-input">
        <el-input
          v-model="newComment"
          type="textarea"
          :rows="3"
          placeholder="分享你的想法..."
          class="apple-textarea"
        />
        <el-button type="primary" class="submit-comment" @click="submitComment">
          发表评论
        </el-button>
      </div>
      
      <!-- 评论列表 -->
      <div class="comments-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <img :src="comment.avatar" :alt="comment.name" class="comment-avatar">
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.name }}</span>
              <span class="comment-time">{{ comment.time }}</span>
            </div>
            <p class="comment-text">{{ comment.content }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onUnmounted } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import { ArrowLeft, View, ChatLineRound } from '@element-plus/icons-vue'

const data = reactive({
  articleId: router.currentRoute.value.query.id,
  articleData: {}
})

const contentRef = ref(null)
const readingProgress = ref(0)
const newComment = ref('')
const comments = ref([])

const loadArticle = () => {
  data.articleId = router.currentRoute.value.query.id
  request.get('/article/selectById/' + data.articleId).then(res => {
    if (res.code === '200') {
      data.articleData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const navTo = (url) => {
  location.href = url
}

const updateReadingProgress = () => {
  if (!contentRef.value) return
  
  const element = contentRef.value
  const totalHeight = element.scrollHeight - element.clientHeight
  const progress = (element.scrollTop / totalHeight) * 100
  readingProgress.value = Math.min(progress, 100)
}

const submitComment = () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  // TODO: 实现评论提交逻辑
  ElMessage.success('评论提交成功')
  newComment.value = ''
}

onMounted(() => {
  loadArticle()
  window.addEventListener('scroll', updateReadingProgress)
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateReadingProgress)
})
</script>

<style scoped>
.forum-detail {
  min-height: 100vh;
  background-color: #ffffff;
}

/* 阅读进度条 */
.reading-progress {
  position: fixed;
  top: 0;
  left: 0;
  height: 2px;
  background: #0066cc;
  z-index: 1000;
  transition: width 0.2s ease;
}

/* 文章头部 */
.article-header {
  background: linear-gradient(135deg, #ffffff 0%, #f5f5f7 100%);
  padding: 40px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.breadcrumb {
  margin-bottom: 24px;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #0066cc;
}

.article-title {
  font-size: 40px;
  font-weight: 600;
  margin: 0 0 32px 0;
  letter-spacing: -0.5px;
  line-height: 1.2;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 作者卡片 */
.author-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
}

.author-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.author-name {
  font-size: 16px;
  font-weight: 500;
  color: #1d1d1f;
}

.publish-time {
  font-size: 14px;
  color: #86868b;
}

.article-stats {
  display: flex;
  gap: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #86868b;
  padding: 4px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
}

/* 文章内容 */
.article-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.content-wrapper {
  font-size: 16px;
  line-height: 1.6;
  color: #1d1d1f;
}

.content-wrapper :deep(h1),
.content-wrapper :deep(h2),
.content-wrapper :deep(h3) {
  margin: 24px 0 16px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.content-wrapper :deep(p) {
  margin: 16px 0;
}

.content-wrapper :deep(img) {
  max-width: 100%;
  border-radius: 12px;
  margin: 24px 0;
}

.content-wrapper :deep(pre) {
  background: #f5f5f7;
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 16px 0;
}

.content-wrapper :deep(code) {
  font-family: 'SF Mono', monospace;
  font-size: 14px;
}

.content-wrapper :deep(blockquote) {
  border-left: 4px solid #0066cc;
  margin: 16px 0;
  padding: 8px 24px;
  background: #f5f5f7;
  border-radius: 4px;
}

/* 评论区 */
.comments-section {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #1d1d1f;
}

.comment-input {
  margin-bottom: 32px;
}

.apple-textarea {
  margin-bottom: 16px;
}

:deep(.el-textarea__inner) {
  border-radius: 12px;
  padding: 12px 16px;
  background: #f5f5f7;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

:deep(.el-textarea__inner:focus) {
  background: #ffffff;
  border-color: #0066cc;
  box-shadow: 0 0 0 4px rgba(0, 102, 204, 0.1);
}

.submit-comment {
  height: 40px;
  padding: 0 24px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 980px;
  background: #0066cc;
  border: none;
  transition: all 0.3s ease;
}

.submit-comment:hover {
  background: #004499;
  transform: translateY(-2px);
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: #f5f5f7;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.comment-item:hover {
  background: #ffffff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
  transform: translateY(-2px);
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-author {
  font-size: 14px;
  font-weight: 500;
  color: #1d1d1f;
}

.comment-time {
  font-size: 12px;
  color: #86868b;
}

.comment-text {
  font-size: 14px;
  line-height: 1.5;
  color: #1d1d1f;
  margin: 0;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .article-title {
    font-size: 32px;
  }

  .author-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .article-stats {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>