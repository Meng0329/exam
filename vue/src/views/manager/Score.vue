<template>
  <div class="apple-score">
    <!-- 顶部搜索和筛选区域 -->
    <div class="header-section">
      <div class="search-filters">
        <div class="search-group">
          <el-input
            v-model="data.name"
            placeholder="搜索试卷名称..."
            class="apple-search"
            :prefix-icon="Search"
          />
          <el-input
            v-model="data.courseName"
            placeholder="搜索课程名称..."
            class="apple-search"
            :prefix-icon="Search"
          />
          <el-select
            v-model="data.status"
            placeholder="阅卷状态"
            class="apple-select"
            clearable
          >
            <el-option label="待阅卷" value="待阅卷" />
            <el-option label="已阅卷" value="已阅卷" />
          </el-select>
        </div>
        <div class="action-buttons">
          <el-button type="primary" class="apple-button" @click="load">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button class="apple-button" @click="reset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 试卷列表 -->
    <div class="papers-section">
      <el-table
        :data="data.tableData"
        class="apple-table"
      >
        <el-table-column prop="name" label="试卷名称" min-width="200">
          <template #default="scope">
            <div class="paper-info">
              <h3 class="paper-name">{{ scope.row.name }}</h3>
              <p class="paper-course">{{ scope.row.courseName }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="授课教师" width="120" v-if="data.user.role === 'ADMIN'">
          <template #default="scope">
            <span class="teacher-name">{{ scope.row.teacherName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="studentName" label="学生姓名" width="120">
          <template #default="scope">
            <span class="student-name">{{ scope.row.studentName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.status === '已阅卷' ? 'success' : 'warning'"
              class="status-tag"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分数" width="80">
          <template #default="scope">
            <div class="score-badge" :class="getScoreClass(scope.row.score)">
              {{ scope.row.score || '-' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" v-if="data.user.role === 'TEACHER'">
          <template #default="scope">
            <el-button
              :disabled="scope.row.status === '已阅卷'"
              type="primary"
              class="grade-button"
              @click="handleEdit(scope.row)"
            >
              阅卷
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页器 -->
    <div class="pagination-section" v-if="data.total">
      <el-pagination
        @current-change="load"
        :current-page="data.pageNum"
        :page-size="data.pageSize"
        :total="data.total"
        layout="prev, pager, next"
        background
        class="apple-pagination"
      />
    </div>

    <!-- 阅卷对话框 -->
    <el-dialog
      title="试卷批改"
      v-model="data.formVisible"
      width="85%"
      destroy-on-close
      class="apple-dialog"
    >
      <div class="grading-content">
        <el-table
          :data="data.answerData"
          class="answer-table"
        >
          <el-table-column prop="questionName" label="题目" min-width="200">
            <template #default="scope">
              <div class="question-content">{{ scope.row.questionName }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="分值" width="80">
            <template #default="scope">
              <div class="max-score">{{ scope.row.score }}分</div>
            </template>
          </el-table-column>
          <el-table-column prop="typeName" label="题型" width="100">
            <template #default="scope">
              <el-tag :type="getQuestionTypeTag(scope.row.typeName)" class="type-tag">
                {{ scope.row.typeName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="标准答案" min-width="200">
            <template #default="scope">
              <div class="answer-content">
                <template v-if="scope.row.typeName === '简答题'">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="scope.row.answer"
                    disabled
                    class="apple-textarea standard-answer"
                  />
                </template>
                <template v-else>
                  <span>{{ scope.row.answer }}</span>
                </template>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="学生答案" min-width="200">
            <template #default="scope">
              <div class="answer-content">
                <template v-if="scope.row.typeName === '简答题'">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="scope.row.newAnswer"
                    disabled
                    class="apple-textarea student-answer"
                  />
                </template>
                <template v-else>
                  <span>{{ scope.row.newAnswer }}</span>
                </template>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="得分" width="150">
            <template #default="scope">
              <div class="score-input">
                <template v-if="scope.row.typeName === '简答题' || scope.row.typeName === '填空题'">
                  <el-input-number
                    v-model="scope.row.result"
                    :min="0"
                    :max="scope.row.score"
                    :step="0.5"
                    class="apple-input-number"
                  />
                </template>
                <template v-else>
                  <div class="auto-score">{{ scope.row.result }}</div>
                </template>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="AI评分建议" width="400" v-if="data.user.role === 'TEACHER'">
            <template #default="scope">
              <div class="ai-suggestion" v-if="scope.row.typeName === '简答题' || scope.row.typeName === '填空题'">
                <template v-if="scope.row.aiFeedback">
                  <div class="suggestion-content">
                    <div class="suggested-score">
                      <strong>建议分数：</strong>
                      <el-tag type="success" class="score-tag">{{ scope.row.aiSuggestedScore }}</el-tag>
                    </div>
                    <div class="feedback-section">
                      <strong>评分建议：</strong>
                      <p class="feedback-text">{{ scope.row.displayFeedback }}</p>
                    </div>
                    <div class="details-section">
                      <strong>评分细则：</strong>
                      <p class="details-text">{{ scope.row.displayScoringDetails }}</p>
                    </div>
                  </div>
                </template>
                <template v-else>
                  <div class="loading-suggestion">
                    <el-empty v-if="!scope.row.loading" description="正在获取AI建议..." :image-size="50" />
                    <div v-else class="progress-container">
                      <el-progress type="circle" :percentage="scope.row.loadingProgress" />
                    </div>
                  </div>
                </template>
              </div>
              <span v-else class="no-suggestion">-</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false" class="cancel-button">取消</el-button>
          <el-button type="primary" @click="update" class="submit-button">提交评分</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  courseName: null,
  status: null,
  answerData: [],
  form: {}
})

const load = () => {
  request.get('/score/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      courseName: data.courseName,
      status: data.status
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  request.get('/score/selectAnswer/' + row.id).then(res => {
    if (res.code === '200') {
      data.answerData = res.data
      data.formVisible = true
      // 自动为简答题和填空题获取AI建议
      data.answerData.forEach(async (answer) => {
        if (answer.typeName === '简答题' || answer.typeName === '填空题') {
          answer.loading = true
          answer.loadingProgress = 0
          answer.displayFeedback = ''
          answer.displayScoringDetails = ''
          await getAiSuggestion(answer)
        }
      })
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  data.form.answerData = data.answerData
  request.put('/score/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const reset = () => {
  data.name = null
  data.courseName = null
  data.status = null
  load()
}

const getAiSuggestion = async (row) => {
  try {
    const res = await request.post('/score/aiSuggestion', {
      questionId: row.questionId,
      answer: row.answer,
      newAnswer: row.newAnswer,
      score: row.score
    });
    
    if (res.code === '200') {
      row.aiSuggestedScore = res.data.suggestedScore;
      row.aiFeedback = res.data.feedback;
      row.scoringDetails = res.data.scoringDetails;
      
      // 逐字显示评分建议
      let feedbackIndex = 0;
      let scoringDetailsIndex = 0;
      
      const displayText = () => {
        if (feedbackIndex < row.aiFeedback.length) {
          row.displayFeedback = row.aiFeedback.slice(0, ++feedbackIndex);
          row.loadingProgress = Math.floor((feedbackIndex / row.aiFeedback.length) * 50);
          setTimeout(displayText, 50);
        } else if (scoringDetailsIndex < row.scoringDetails.length) {
          row.displayScoringDetails = row.scoringDetails.slice(0, ++scoringDetailsIndex);
          row.loadingProgress = 50 + Math.floor((scoringDetailsIndex / row.scoringDetails.length) * 50);
          setTimeout(displayText, 50);
        } else {
          row.loading = false;
          row.loadingProgress = 100;
          // 自动填入建议分数
          row.result = row.aiSuggestedScore;
        }
      };
      
      setTimeout(displayText, 500); // 延迟500ms开始显示
    } else {
      ElMessage.error(res.msg);
      row.loading = false;
    }
  } catch (error) {
    ElMessage.error('获取AI建议失败');
    row.loading = false;
  }
};

const getScoreClass = (score) => {
  if (!score) return '';
  const numScore = Number(score);
  if (numScore >= 90) return 'score-excellent';
  if (numScore >= 80) return 'score-good';
  if (numScore >= 60) return 'score-pass';
  return 'score-fail';
}

const getQuestionTypeTag = (type) => {
  const typeMap = {
    '单选题': 'success',
    '多选题': 'primary',
    '判断题': 'warning',
    '填空题': 'info',
    '简答题': 'danger'
  };
  return typeMap[type] || 'default';
};

load()
</script>

<style scoped>
.apple-score {
  padding: 2rem;
  background-color: #f5f5f7;
  min-height: 100vh;
}

.header-section {
  margin-bottom: 2rem;
}

.search-filters {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.search-group {
  display: flex;
  gap: 1rem;
  flex: 1;
}

.apple-search {
  max-width: 300px;
}

.apple-search :deep(.el-input__wrapper),
.apple-select :deep(.el-input__wrapper) {
  background-color: white;
  border-radius: 0.8rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  border: none;
}

.action-buttons {
  display: flex;
  gap: 1rem;
}

.apple-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border-radius: 0.8rem;
  padding: 0.6rem 1.2rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.apple-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.papers-section {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.apple-table {
  width: 100%;
  border-radius: 0.8rem;
  overflow: hidden;
}

.apple-table :deep(.el-table__header) {
  background-color: #f5f5f7;
}

.apple-table :deep(.el-table__row) {
  transition: all 0.3s ease;
}

.apple-table :deep(.el-table__row:hover) {
  background-color: #f5f5f7;
  transform: scale(1.002);
}

.paper-info {
  padding: 0.5rem 0;
}

.paper-name {
  font-size: 1rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 0.25rem 0;
}

.paper-course {
  font-size: 0.875rem;
  color: #86868b;
  margin: 0;
}

.teacher-name,
.student-name {
  font-size: 0.875rem;
  color: #1d1d1f;
}

.status-tag {
  border-radius: 1rem;
  padding: 0.25rem 0.75rem;
}

.score-badge {
  font-size: 0.875rem;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  display: inline-block;
  font-weight: 500;
}

.score-excellent {
  background-color: #e3f9e5;
  color: #1d893a;
}

.score-good {
  background-color: #e6f3ff;
  color: #147cd1;
}

.score-pass {
  background-color: #faf3d6;
  color: #9f8a3b;
}

.score-fail {
  background-color: #fff2f2;
  color: #ff3b30;
}

.grade-button {
  border-radius: 0.8rem;
  transition: all 0.3s ease;
}

.grade-button:not(:disabled):hover {
  transform: scale(1.1);
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.apple-pagination {
  --el-pagination-button-bg-color: white;
  --el-pagination-hover-color: #0071e3;
}

.apple-dialog {
  border-radius: 1rem;
  overflow: hidden;
}

.grading-content {
  padding: 1rem;
}

.answer-table {
  margin-bottom: 1rem;
}

.question-content {
  font-size: 0.875rem;
  color: #1d1d1f;
  line-height: 1.4;
}

.max-score {
  font-weight: 500;
  color: #ff3b30;
}

.type-tag {
  border-radius: 0.8rem;
  padding: 0.25rem 0.75rem;
}

.answer-content {
  padding: 0.5rem 0;
}

.apple-textarea {
  border-radius: 0.8rem;
  background-color: #f5f5f7;
}

.standard-answer :deep(.el-textarea__inner) {
  background-color: #f5f5f7;
  border: none;
}

.student-answer :deep(.el-textarea__inner) {
  background-color: #fff2f2;
  border: none;
}

.score-input {
  display: flex;
  align-items: center;
  justify-content: center;
}

.apple-input-number {
  width: 100px;
}

.auto-score {
  font-weight: 500;
  color: #1d893a;
}

.ai-suggestion {
  padding: 0.5rem;
}

.suggestion-content {
  background: #f5f5f7;
  border-radius: 0.8rem;
  padding: 1rem;
}

.suggested-score {
  margin-bottom: 0.5rem;
}

.score-tag {
  margin-left: 0.5rem;
  border-radius: 0.8rem;
}

.feedback-section,
.details-section {
  margin-top: 0.5rem;
}

.feedback-text,
.details-text {
  margin: 0.5rem 0;
  font-size: 0.875rem;
  color: #666;
  line-height: 1.4;
  white-space: pre-line;
}

.loading-suggestion {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100px;
}

.progress-container {
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1rem;
  border-top: 1px solid #f5f5f7;
}

.cancel-button,
.submit-button {
  border-radius: 0.8rem;
  padding: 0.6rem 1.2rem;
  transition: all 0.3s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .search-filters {
    flex-direction: column;
  }

  .search-group {
    flex-direction: column;
    width: 100%;
  }

  .apple-search {
    max-width: none;
  }

  .action-buttons {
    width: 100%;
    justify-content: space-between;
  }

  .apple-score {
    padding: 1rem;
  }
}
</style>