<template>
  <div class="exam-container">
    <!-- 试卷头部信息 -->
    <div class="exam-header">
      <div class="header-content">
        <h1 class="exam-title">{{ data.testPaperData.name }}</h1>
        <div class="exam-info">
          <div class="info-item">
            <span class="info-label">课程：</span>
            <span class="info-value">{{ data.testPaperData.courseName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">教师：</span>
            <span class="info-value">{{ data.testPaperData.teacherName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">时长：</span>
            <span class="info-value">{{ data.testPaperData.time }}分钟</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 计时器 -->
    <div class="timer-section" :class="{'timer-warning': isTimeWarning}">
      <div class="timer-content">
        <el-icon class="timer-icon"><Timer /></el-icon>
        <div class="timer-display">
          <span class="timer-unit">{{ data.hour }}<small>时</small></span>
          <span class="timer-unit">{{ data.minutes }}<small>分</small></span>
          <span class="timer-unit">{{ data.seconds }}<small>秒</small></span>
        </div>
      </div>
    </div>

    <!-- 字体大小控制 -->
    <div class="font-control">
      <div class="font-control-content">
        <span class="font-label">字体大小：</span>
        <el-radio-group v-model="fontSize" size="small" @change="changeFontSize">
          <el-radio-button label="small">小</el-radio-button>
          <el-radio-button label="medium">中</el-radio-button>
          <el-radio-button label="large">大</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <div class="exam-layout">
      <!-- 侧边导航栏 -->
      <div class="question-nav">
        <div class="nav-header">
          <span>题目导航</span>
          <el-button type="primary" link @click="showPreview = true">
            <el-icon><Document /></el-icon> 整卷预览
          </el-button>
        </div>
        <div class="nav-content">
          <div v-for="(questions, type) in groupedQuestions" :key="type" class="nav-group">
            <div class="nav-group-header">
              <span class="group-title">{{ type }}</span>
              <span class="group-count">{{ questions.length }}题</span>
            </div>
            <div class="nav-group-items">
              <div v-for="question in questions" 
                   :key="question.index"
                   :class="['nav-item', { 'nav-item-active': currentQuestion === question.index }]"
                   @click="scrollToQuestion(question.index)">
                <span class="nav-number">{{ question.index + 1 }}</span>
                <span class="nav-score">{{ question.score }}分</span>
                <span class="nav-status" :class="getAnswerStatus(question)"></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 试卷内容区域 -->
      <div class="exam-content" ref="examContent">
        <div class="questions-container">
          <div v-for="(item, index) in data.testPaperData.questions" 
               :key="index" 
               :ref="el => questionRefs[index] = el"
               class="question-card">
            <!-- 题目标题 -->
            <div class="question-header">
              <div class="question-type">
                <span class="type-tag">{{ index + 1 }}. {{ getQuestionType(item.typeId) }}</span>
                <span class="score-tag">{{ item.typeScore }}分</span>
              </div>
              <div class="question-title">{{ item.name }}</div>
            </div>

            <!-- 题目内容 -->
            <div class="question-body">
              <!-- 单选题 -->
              <div v-if="item.typeId === 1" class="options-group">
                <el-radio-group v-model="item.newAnswer" class="apple-radio-group single-choice">
                  <el-radio v-for="option in ['A', 'B', 'C', 'D']" 
                           :key="option" 
                           :label="option"
                           class="apple-radio">
                    {{ option }}. {{ item['option' + option] }}
                  </el-radio>
                </el-radio-group>
              </div>

              <!-- 多选题 -->
              <div v-if="item.typeId === 2" class="options-group">
                <el-checkbox-group v-model="item.checkList" class="apple-checkbox-group">
                  <el-checkbox v-for="option in ['A', 'B', 'C', 'D']"
                              :key="option"
                              :label="option"
                              class="apple-checkbox">
                    {{ option }}. {{ item['option' + option] }}
                  </el-checkbox>
          </el-checkbox-group>
        </div>

              <!-- 判断题 -->
              <div v-if="item.typeId === 3" class="options-group">
                <el-radio-group v-model="item.newAnswer" class="apple-radio-group single-choice">
                  <el-radio label="正确" class="apple-radio">A. 正确</el-radio>
                  <el-radio label="错误" class="apple-radio">B. 错误</el-radio>
          </el-radio-group>
        </div>

              <!-- 填空题 -->
              <div v-if="item.typeId === 4" class="fill-blank">
                <el-input 
                  v-model="item.newAnswer" 
                  class="apple-input"
                  placeholder="请输入您的答案">
                </el-input>
              </div>

              <!-- 简答题 -->
              <div v-if="item.typeId === 5" class="essay">
                <el-input 
                  type="textarea" 
                  :rows="6" 
                  v-model="item.newAnswer"
                  class="apple-textarea"
                  placeholder="请输入您的答案">
                </el-input>
              </div>
            </div>
          </div>
        </div>

        <!-- 提交按钮 -->
        <div class="submit-section">
          <el-button class="submit-button" type="primary" @click="submitPaper">
            提交试卷
          </el-button>
        </div>
      </div>
    </div>

    <!-- 整卷预览对话框 -->
    <el-dialog
      v-model="showPreview"
      title="试卷预览"
      width="80%"
      class="preview-dialog"
    >
      <div class="preview-content">
        <div v-for="(item, index) in data.testPaperData.questions" 
             :key="index"
             class="preview-item"
             @click="scrollToQuestion(index); showPreview = false">
          <div class="preview-header">
            <span class="preview-number">{{ index + 1 }}</span>
            <span class="preview-type">{{ getQuestionType(item.typeId) }}</span>
            <span class="preview-score">{{ item.typeScore }}分</span>
          </div>
          <div class="preview-title">{{ item.name }}</div>
          <div class="preview-status" :class="getAnswerStatus(item)">
            {{ getAnswerStatusText(item) }}
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, computed, ref, onMounted, onUnmounted } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import { Timer, Document } from '@element-plus/icons-vue'

const data = reactive({
  testPaperId: router.currentRoute.value.query.id,
  testPaperData: {},
  hour: 0,
  minutes: 0,
  seconds: 0,
})

const showPreview = ref(false)
const currentQuestion = ref(0)
const questionRefs = ref([])
const examContent = ref(null)
const fontSize = ref('medium')

const isTimeWarning = computed(() => {
  const totalMinutes = data.hour * 60 + data.minutes;
  return totalMinutes <= 5; // 剩余5分钟内显示警告
})

const getQuestionType = (typeId) => {
  const types = {
    1: '单选题',
    2: '多选题',
    3: '判断题',
    4: '填空题',
    5: '简答题'
  }
  return types[typeId] || '未知类型'
}

let time
const loadTestPaper = () => {
  data.testPaperId = router.currentRoute.value.query.id
  request.get('/testPaper/selectById/' + data.testPaperId).then(res => {
    if (res.code === '200') {
      data.testPaperData = res.data
      // 初始化每个题目的答案
      data.testPaperData.questions.forEach(question => {
        if (question.typeId === 2) {
          question.checkList = question.checkList || []
        } else {
          question.newAnswer = question.newAnswer || ''
        }
      })
      let maxTime = data.testPaperData.maxTime
      time = setInterval(() => {
        if (maxTime >= 0) {
          let remain = Math.floor(maxTime % 3600)
          data.hour = Math.floor(maxTime / 3600)
          data.minutes = Math.floor(remain / 60)
          data.seconds = Math.floor(remain % 60)
          maxTime--
        } else {
          ElMessage.error('考试时间已结束')
          submitPaper()
        }
      }, 1000)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const submitPaper = () => {
  clearInterval(time)
  request.post('/score/add', data.testPaperData).then(res => {
    if (res.code === '200') {
      ElMessage.success('试卷提交成功')
      setTimeout(() => {
        location.href = '/front/score'
      }, 500)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const scrollToQuestion = (index) => {
  currentQuestion.value = index
  const element = questionRefs.value[index]
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

const getAnswerStatus = (question) => {
  if (question.typeId === 2) {
    return question.checkList && question.checkList.length > 0 ? 'answered' : 'unanswered'
  }
  return question.newAnswer ? 'answered' : 'unanswered'
}

const getAnswerStatusText = (question) => {
  return getAnswerStatus(question) === 'answered' ? '已作答' : '未作答'
}

// 监听滚动位置以更新当前题目
const updateCurrentQuestion = () => {
  if (!examContent.value) return
  const scrollTop = examContent.value.scrollTop
  const elements = questionRefs.value
  
  for (let i = 0; i < elements.length; i++) {
    const element = elements[i]
    if (!element) continue
    const rect = element.getBoundingClientRect()
    if (rect.top >= 0 && rect.top <= window.innerHeight / 2) {
      currentQuestion.value = i
      break
    }
  }
}

onMounted(() => {
  if (examContent.value) {
    examContent.value.addEventListener('scroll', updateCurrentQuestion)
  }
})

onUnmounted(() => {
  if (examContent.value) {
    examContent.value.removeEventListener('scroll', updateCurrentQuestion)
  }
})

// 添加分组计算属性
const groupedQuestions = computed(() => {
  if (!data.testPaperData.questions) return {}
  
  const groups = {
    '单选题': [],
    '多选题': [],
    '判断题': [],
    '填空题': [],
    '简答题': []
  }
  
  data.testPaperData.questions.forEach((question, index) => {
    const type = getQuestionType(question.typeId)
    if (groups[type]) {
      groups[type].push({
        ...question,
        index,
        score: question.typeScore
      })
    }
  })
  
  // 移除空的题型组
  return Object.fromEntries(
    Object.entries(groups).filter(([_, questions]) => questions.length > 0)
  )
})

const changeFontSize = (size) => {
  const fontSizes = {
    small: {
      title: '13px',
      option: '12px',
      content: '13px'
    },
    medium: {
      title: '14px',
      option: '13px',
      content: '14px'
    },
    large: {
      title: '16px',
      option: '15px',
      content: '16px'
    }
  }
  
  document.documentElement.style.setProperty('--question-title-size', fontSizes[size].title)
  document.documentElement.style.setProperty('--option-text-size', fontSizes[size].option)
  document.documentElement.style.setProperty('--content-text-size', fontSizes[size].content)
}

loadTestPaper()
</script>

<style scoped>
.exam-container {
  min-height: 100vh;
  background-color: #f5f5f7;
}

/* 试卷头部样式 */
.exam-header {
  background: linear-gradient(135deg, #ffffff 0%, #f5f5f7 100%);
  padding: 20px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.header-content {
  width: 100%;
  padding: 0 20px;
  text-align: center;
}

.exam-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 16px 0;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.exam-info {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 980px;
}

.info-label {
  color: #86868b;
  font-size: 14px;
}

.info-value {
  color: #1d1d1f;
  font-weight: 500;
  font-size: 14px;
}

/* 计时器样式 */
.timer-section {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: saturate(180%) blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.timer-section.timer-warning {
  background: rgba(255, 59, 48, 0.1);
}

.timer-content {
  width: 100%;
  padding: 8px 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.timer-icon {
  font-size: 20px;
  color: #1d1d1f;
}

.timer-display {
  display: flex;
  gap: 12px;
  font-size: 24px;
  font-weight: 500;
  color: #1d1d1f;
}

.timer-unit {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.timer-unit small {
  font-size: 14px;
  color: #86868b;
  font-weight: 400;
}

/* 字体大小控制样式 */
.font-control {
  position: sticky;
  top: 44px;
  z-index: 99;
  background: #ffffff;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  padding: 8px 0;
}

.font-control-content {
  width: 100%;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
}

.font-label {
  font-size: 14px;
  color: #1d1d1f;
}

/* 试卷内容区域样式 */
.exam-layout {
  display: flex;
  gap: 20px;
  padding: 20px;
  height: calc(100vh - 220px);
}

.question-nav {
  position: sticky;
  top: 140px;
  width: 240px;
  background: #ffffff;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
  height: fit-content;
  max-height: calc(100vh - 240px);
  overflow-y: auto;
}

.nav-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  font-weight: 500;
}

.nav-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-group {
  margin-bottom: 16px;
}

.nav-group:last-child {
  margin-bottom: 0;
}

.nav-group-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  background: #f5f5f7;
  border-radius: 8px;
  margin-bottom: 8px;
}

.group-title {
  font-size: 13px;
  font-weight: 600;
  color: #1d1d1f;
}

.group-count {
  font-size: 12px;
  color: #86868b;
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 8px;
  border-radius: 980px;
}

.nav-group-items {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #ffffff;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.nav-item:hover {
  background: #f5f5f7;
  border-color: rgba(0, 0, 0, 0.1);
}

.nav-item-active {
  background: #e6f3ff;
  border-color: #0066cc;
}

.nav-number {
  font-size: 12px;
  font-weight: 500;
  color: #1d1d1f;
  min-width: 16px;
  text-align: center;
}

.nav-score {
  font-size: 11px;
  color: #ff3b30;
  background: #fff2f2;
  padding: 1px 4px;
  border-radius: 4px;
}

.nav-status {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-left: auto;
}

.nav-status.answered {
  background: #30d158;
}

.nav-status.unanswered {
  background: #ff3b30;
}

.exam-content {
  flex: 1;
  overflow-y: auto;
  padding-right: 12px;
}

.questions-container {
  width: 100%;
  padding: 0 16px;
}

.question-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
  margin-bottom: 12px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.question-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.08);
}

.question-header {
  padding: 8px 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
}

.question-type {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.type-tag {
  background: #e6f3ff;
  color: #0066cc;
  padding: 2px 10px;
  border-radius: 980px;
  font-size: 12px;
  font-weight: 500;
}

.score-tag {
  background: #fff2f2;
  color: #ff3b30;
  padding: 2px 10px;
  border-radius: 980px;
  font-size: 12px;
  font-weight: 500;
}

.question-title {
  font-size: var(--question-title-size);
  font-weight: 500;
  color: #1d1d1f;
  line-height: 1.4;
}

.question-body {
  padding: 12px;
}

/* 选项组样式 */
.options-group {
  margin-bottom: 16px;
}

.apple-radio-group,
.apple-checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

/* 移除单选题和判断题的横向布局 */
.apple-radio-group.single-choice {
  flex-direction: column;
  gap: 8px;
}

.apple-radio-group.single-choice :deep(.el-radio) {
  margin-right: 0;
  width: 100%;
  max-width: none;
  min-width: 0;
}

:deep(.el-radio),
:deep(.el-checkbox) {
  margin: 0;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: #f5f5f7;
  height: auto;
  white-space: normal;
  line-height: 1.4;
  width: 100%;
}

:deep(.el-radio__label),
:deep(.el-checkbox__label) {
  font-size: var(--option-text-size);
  line-height: 1.4;
  padding-left: 8px;
  white-space: normal;
  width: 100%;
}

/* 填空题样式 */
.fill-blank {
  margin-bottom: 16px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 6px 12px;
  background: #f5f5f7;
  box-shadow: none !important;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

/* 简答题样式 */
.essay {
  margin-bottom: 16px;
}

:deep(.el-textarea__inner) {
  border-radius: 8px;
  padding: 8px 12px;
  background: #f5f5f7;
  border: 1px solid rgba(0, 0, 0, 0.1);
  resize: none;
  font-size: var(--content-text-size);
  line-height: 1.4;
}

/* 提交按钮样式 */
.submit-section {
  width: 100%;
  margin: 24px auto;
  padding: 0 16px;
  text-align: center;
}

.submit-button {
  height: 40px;
  padding: 0 32px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 980px;
  background: #0066cc;
  border: none;
  transition: all 0.3s ease;
}

.submit-button:hover {
  background: #004499;
  transform: translateY(-2px);
}

/* 调整内容区域的滚动条样式 */
.exam-content::-webkit-scrollbar {
  width: 6px;
}

.exam-content::-webkit-scrollbar-track {
  background: transparent;
}

.exam-content::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

.exam-content::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.2);
}

/* 预览对话框样式 */
.preview-dialog :deep(.el-dialog__body) {
  padding: 0;
  max-height: 70vh;
  overflow-y: auto;
}

.preview-content {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  padding: 20px;
}

.preview-item {
  background: #f5f5f7;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.preview-item:hover {
  background: #e6f3ff;
  transform: translateY(-2px);
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.preview-number {
  font-weight: 500;
}

.preview-type {
  color: #86868b;
  font-size: 13px;
}

.preview-score {
  margin-left: auto;
  color: #ff3b30;
  font-size: 13px;
}

.preview-title {
  font-size: 14px;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.preview-status {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  display: inline-block;
}

.preview-status.answered {
  background: #e3f9e5;
  color: #1d893a;
}

.preview-status.unanswered {
  background: #fff2f2;
  color: #ff3b30;
}

/* 添加 CSS 变量 */
:root {
  --question-title-size: 14px;
  --option-text-size: 13px;
  --content-text-size: 14px;
}
</style>