<template>
  <div class="score-container">
    <!-- 顶部统计区域 -->
    <div class="stats-section">
      <div class="stats-content">
        <h1 class="page-title">我的成绩</h1>
        <p class="page-subtitle">追踪你的学习进度和考试表现</p>
        <div class="stats-cards">
          <div class="stat-card">
            <div class="stat-value">{{ data.tableData.length }}</div>
            <div class="stat-label">考试总数</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ getAverageScore() }}</div>
            <div class="stat-label">平均分</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ getCompletedExams() }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 成绩列表区域 -->
    <div class="scores-section">
      <div class="scores-content">
        <el-table 
          :data="data.tableData"
          class="score-table"
          :header-cell-style="{
            background: 'transparent',
            color: '#1d1d1f',
            fontWeight: '600',
            fontSize: '15px'
          }"
        >
          <el-table-column prop="name" label="试卷名称" min-width="200">
            <template #default="scope">
              <div class="exam-name">{{ scope.row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="courseName" label="课程名称" min-width="150">
            <template #default="scope">
              <div class="course-name">{{ scope.row.courseName }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="teacherName" label="授课教师" min-width="120">
            <template #default="scope">
              <div class="teacher-name">{{ scope.row.teacherName }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <div class="status-tag" :class="scope.row.status === '已阅卷' ? 'status-completed' : 'status-pending'">
                {{ scope.row.status }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="分数" width="100">
            <template #default="scope">
              <div class="score-value" :class="getScoreClass(scope.row.score)">
                {{ scope.row.score }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button 
                class="view-button"
                @click="navTo('/front/testPaperView?id=' + scope.row.id)"
              >
                查看试卷
              </el-button>
            </template>
          </el-table-column>
        </el-table>

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
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0
})

const getAverageScore = () => {
  if (data.tableData.length === 0) return '0';
  const completedExams = data.tableData.filter(exam => exam.status === '已阅卷');
  if (completedExams.length === 0) return '0';
  const totalScore = completedExams.reduce((sum, exam) => sum + Number(exam.score || 0), 0);
  return (totalScore / completedExams.length).toFixed(1);
}

const getCompletedExams = () => {
  return data.tableData.filter(exam => exam.status === '已阅卷').length;
}

const getScoreClass = (score) => {
  if (!score) return '';
  const numScore = Number(score);
  if (numScore >= 90) return 'score-excellent';
  if (numScore >= 80) return 'score-good';
  if (numScore >= 60) return 'score-pass';
  return 'score-fail';
}

const load = () => {
  request.get('/score/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const navTo = (url) => {
  location.href = url
}

load()
</script>

<style scoped>
.score-container {
  min-height: 100vh;
  background-color: #f5f5f7;
}

/* 统计区域样式 */
.stats-section {
  background: linear-gradient(135deg, #ffffff 0%, #f5f5f7 100%);
  padding: 60px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.stats-content {
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

.stats-cards {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 40px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 24px 40px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.08);
}

.stat-value {
  font-size: 36px;
  font-weight: 600;
  color: #1d1d1f;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  color: #86868b;
  font-weight: 500;
}

/* 成绩列表区域样式 */
.scores-section {
  padding: 40px 0;
}

.scores-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.score-table {
  background: transparent;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
}

:deep(.el-table) {
  --el-table-border-color: transparent;
  --el-table-header-bg-color: #f5f5f7;
  --el-table-row-hover-bg-color: rgba(0, 0, 0, 0.02);
}

:deep(.el-table__row) {
  background-color: #ffffff;
}

:deep(.el-table__row:nth-child(even)) {
  background-color: #f9f9fc;
}

.exam-name {
  font-weight: 500;
  color: #1d1d1f;
}

.course-name {
  color: #1d1d1f;
}

.teacher-name {
  color: #86868b;
  font-size: 14px;
}

.status-tag {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 980px;
  font-size: 13px;
  font-weight: 500;
}

.status-completed {
  background-color: #e3f9e5;
  color: #1d893a;
}

.status-pending {
  background-color: #fff2f2;
  color: #ff3b30;
}

.score-value {
  font-weight: 600;
  font-size: 16px;
  padding: 4px 12px;
  border-radius: 8px;
}

.score-excellent {
  color: #1d893a;
  background-color: #e3f9e5;
}

.score-good {
  color: #147cd1;
  background-color: #e6f3ff;
}

.score-pass {
  color: #9f8a3b;
  background-color: #faf3d6;
}

.score-fail {
  color: #ff3b30;
  background-color: #fff2f2;
}

.view-button {
  background: transparent;
  border: none;
  color: #0066cc;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 980px;
  transition: all 0.3s ease;
}

.view-button:hover {
  background-color: #0066cc;
  color: #ffffff;
}

/* 分页样式 */
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