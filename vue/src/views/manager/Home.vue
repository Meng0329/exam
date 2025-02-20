<template>
  <div class="apple-home">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">您好，{{ data.user?.name }}</h1>
        <p class="welcome-subtitle">欢迎使用在线考试系统</p>
      </div>
    </div>

    <!-- 数据统计卡片 -->
    <div class="stats-grid">
      <div class="stats-card" v-for="(stat, index) in stats" :key="index">
        <div class="stats-icon">
          <el-icon><component :is="stat.icon" /></el-icon>
        </div>
        <div class="stats-info">
          <h3 class="stats-value">{{ stat.value }}</h3>
          <p class="stats-label">{{ stat.label }}</p>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-grid">
      <!-- 系统公告 -->
      <div class="content-card notice-card">
        <div class="card-header">
          <h2 class="card-title">
            <el-icon><Bell /></el-icon>
            系统公告
          </h2>
        </div>
        <div class="card-content">
          <el-timeline>
            <el-timeline-item
                v-for="(item, index) in data.noticeData"
                :key="index"
                :timestamp="item.time"
                type="primary">
              <div class="timeline-content">{{ item.content }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>

      <!-- 考试安排 -->
      <div class="content-card exam-card">
        <div class="card-header">
          <h2 class="card-title">
            <el-icon><Calendar /></el-icon>
            考试安排
          </h2>
        </div>
        <div class="card-content">
          <el-timeline>
            <el-timeline-item
                v-for="(item, index) in data.planData"
                :key="index"
                :timestamp="item.time"
                type="success">
              <div class="timeline-content">{{ item.content }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Bell, Calendar, Document, User, Edit, Trophy } from '@element-plus/icons-vue';

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: [],
  planData: [],
  examCount: 0,
  studentCount: 0,
  questionCount: 0,
  scoreCount: 0
})

const stats = computed(() => [
  {
    label: '试卷总数',
    value: data.examCount,
    icon: 'Document'
  },
  {
    label: '学生人数',
    value: data.studentCount,
    icon: 'User'
  },
  {
    label: '题库数量',
    value: data.questionCount,
    icon: 'Edit'
  },
  {
    label: '已批改',
    value: data.scoreCount,
    icon: 'Trophy'
  }
])

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
      if (data.noticeData.length > 4) {
        data.noticeData = data.noticeData.slice(0, 4)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadExamPlan = () => {
  request.get('/examPlan/selectAll').then(res => {
    if (res.code === '200') {
      if (res.data.length > 4) {
        data.planData = res.data.slice(0, 4)
      } else {
        data.planData = res.data
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadStats = () => {
  // 加载统计数据
  Promise.all([
    request.get('/testPaper/count'),
    request.get('/student/count'),
    request.get('/question/count'),
    request.get('/score/count')
  ]).then(([examRes, studentRes, questionRes, scoreRes]) => {
    if (examRes.code === '200') data.examCount = examRes.data
    if (studentRes.code === '200') data.studentCount = studentRes.data
    if (questionRes.code === '200') data.questionCount = questionRes.data
    if (scoreRes.code === '200') data.scoreCount = scoreRes.data
  }).catch(error => {
    ElMessage.error('加载统计数据失败：' + error.message)
  })
}

onMounted(() => {
  loadNotice()
  loadExamPlan()
  loadStats()
})
</script>

<style scoped>
.apple-home {
  padding: 2rem;
  background-color: #f5f5f7;
  min-height: 100vh;
}

.welcome-section {
  margin-bottom: 2rem;
  background: linear-gradient(135deg, #0071e3 0%, #42a4ff 100%);
  border-radius: 1rem;
  padding: 2rem;
  color: white;
}

.welcome-title {
  font-size: 2.5rem;
  font-weight: 600;
  margin: 0;
  letter-spacing: -0.5px;
}

.welcome-subtitle {
  font-size: 1.25rem;
  opacity: 0.9;
  margin: 0.5rem 0 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stats-card {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.stats-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stats-icon {
  width: 3rem;
  height: 3rem;
  background: #f5f5f7;
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
}

.stats-icon .el-icon {
  font-size: 1.5rem;
  color: #0071e3;
}

.stats-value {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
  color: #1d1d1f;
}

.stats-label {
  font-size: 0.875rem;
  color: #86868b;
  margin: 0.25rem 0 0;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.content-card {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  height: 400px;
  overflow: hidden;
}

.card-header {
  margin-bottom: 1.5rem;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.card-title .el-icon {
  color: #0071e3;
}

.timeline-content {
  color: #1d1d1f;
  font-size: 0.875rem;
  line-height: 1.5;
}

:deep(.el-timeline-item__node) {
  background-color: #0071e3;
}

:deep(.el-timeline-item__tail) {
  border-left-color: #e5e5e5;
}

:deep(.el-timeline-item__timestamp) {
  color: #86868b;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
}
</style>