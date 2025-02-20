<template>
  <div class="forum-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-content">
        <h1 class="page-title">交流论坛</h1>
        <p class="page-subtitle">分享你的学习心得和考试经验</p>
        <div class="search-box">
          <el-input
            v-model="data.title"
            placeholder="搜索帖子"
            clearable
            @clear="reset"
            class="search-input"
            size="large"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" size="large" class="search-button" @click="load">
            搜索
          </el-button>
        </div>
      </div>
    </div>

    <!-- 帖子列表 -->
    <div class="posts-section">
      <div class="posts-grid">
        <el-row :gutter="24">
          <el-col :span="6" v-for="item in data.articleData" :key="item.id">
            <div class="post-card" @click="navTo('/front/forumDetail?id=' + item.id)">
              <div class="post-image" :style="{ backgroundImage: `url(${item.img})` }">
                <div class="post-overlay"></div>
              </div>
              <div class="post-content">
                <h3 class="post-title">{{ item.title }}</h3>
                <div class="post-info">
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

      <!-- 分页 -->
      <div v-if="data.total" class="pagination-section">
        <el-pagination
          v-model:current-page="data.pageNum"
          :page-size="data.pageSize"
          :total="data.total"
          @current-change="load"
          background
          layout="prev, pager, next"
          class="apple-pagination"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Search } from '@element-plus/icons-vue'

const data = reactive({
  title: null,
  pageNum: 1,
  pageSize: 8,
  total: 0,
  articleData: []
})

const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.articleData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.title = null
  load()
}

const navTo = (url) => {
  location.href = url
}

load()
</script>

<style scoped>
.forum-container {
  min-height: 100vh;
  background-color: #ffffff;
}

/* 搜索区域 */
.search-section {
  background: linear-gradient(135deg, #f5f5f7 0%, #ffffff 100%);
  padding: 60px 0;
  margin-bottom: 40px;
}

.search-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
}

.page-title {
  font-size: 48px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 16px 0;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: 20px;
  color: #86868b;
  margin: 0 0 40px 0;
  font-weight: 400;
}

.search-box {
  display: flex;
  justify-content: center;
  gap: 16px;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
}

:deep(.el-input__wrapper) {
  border-radius: 16px;
  padding: 4px 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.04);
  background: #ffffff;
}

:deep(.el-input__inner) {
  font-size: 16px;
  height: 44px;
}

.search-button {
  border-radius: 16px;
  padding: 0 24px;
  font-size: 16px;
  font-weight: 500;
  background: #0066cc;
  border: none;
  height: 52px;
}

.search-button:hover {
  background: #004499;
}

/* 帖子列表 */
.posts-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.posts-grid {
  margin-bottom: 40px;
}

.post-card {
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

.post-card:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.post-image {
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.post-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.2) 100%);
}

.post-content {
  padding: 24px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 16px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  letter-spacing: -0.5px;
}

.post-info {
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

/* 分页 */
.pagination-section {
  display: flex;
  justify-content: center;
  margin: 40px 0;
}

:deep(.el-pagination.is-background) {
  --el-pagination-button-bg-color: #ffffff;
  --el-pagination-hover-color: #0066cc;
  --el-pagination-button-color: #1d1d1f;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #0066cc;
}

:deep(.el-pagination.is-background .el-pager li) {
  border-radius: 8px;
  margin: 0 4px;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}
</style>