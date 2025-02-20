<template>
  <div class="apple-testpaper">
    <!-- 顶部搜索和操作区 -->
    <div class="header-section">
      <div class="search-bar">
        <el-input
          v-model="data.name"
          placeholder="搜索试卷..."
          class="apple-search"
          :prefix-icon="Search"
        >
          <template #append>
            <el-button @click="load">搜索</el-button>
          </template>
        </el-input>
        <el-button class="reset-btn" @click="reset">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
      <div class="action-buttons">
        <el-button
          v-if="data.user.role === 'TEACHER'"
          type="primary"
          class="apple-button"
          @click="handleAdd"
        >
          <el-icon><Plus /></el-icon>出卷
        </el-button>
        <el-button
          type="danger"
          class="apple-button"
          @click="delBatch"
          :disabled="!data.ids.length"
        >
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
      </div>
    </div>

    <!-- 试卷列表 -->
    <div class="testpaper-grid">
      <el-table
        :data="data.tableData"
        @selection-change="handleSelectionChange"
        class="apple-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="试卷名称" min-width="200">
          <template #default="scope">
            <div class="testpaper-info">
              <h3 class="testpaper-name">{{ scope.row.name }}</h3>
              <p class="testpaper-course">{{ scope.row.courseName }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="授课教师" width="120">
          <template #default="scope">
            <span class="teacher-name">{{ scope.row.teacherName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="试卷类型" width="120">
          <template #default="scope">
            <el-tag
              :type="scope.row.type === '手动选题' ? 'success' : 'primary'"
              class="type-tag"
            >
              {{ scope.row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="考试时间" width="200">
          <template #default="scope">
            <div class="time-info">
              <div class="date-range">{{ scope.row.start }} 至 {{ scope.row.end }}</div>
              <div class="duration">时长：{{ scope.row.time }}分钟</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="考试状态" width="100">
          <template #default="scope">
            <el-tag
              :type="getStatusType(scope.row.status)"
              class="status-tag"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <div class="action-column">
              <el-button
                type="danger"
                circle
                class="delete-button"
                :icon="Delete"
                @click="del(scope.row.id)"
              />
            </div>
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

    <!-- 新增试卷对话框 -->
    <el-dialog
      title="试卷信息"
      v-model="data.formVisible"
      width="50%"
      destroy-on-close
      class="apple-dialog"
    >
      <el-form
        ref="form"
        :model="data.form"
        label-width="90px"
        class="apple-form"
      >
        <el-form-item prop="courseId" label="选择课程" required>
          <el-select 
            v-model="data.form.courseId" 
            placeholder="请选择课程" 
            class="apple-select"
            @change="loadQuestion"
          >
            <el-option
              v-for="item in data.courseData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="name" label="试卷名称" required>
          <el-input
            v-model="data.form.name"
            placeholder="请输入试卷名称"
            class="apple-input"
          />
        </el-form-item>
        <el-form-item prop="start" label="开始时间" required>
          <el-date-picker
            v-model="data.form.start"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            class="apple-date-picker"
          />
        </el-form-item>
        <el-form-item prop="end" label="结束时间" required>
          <el-date-picker
            v-model="data.form.end"
            type="date"
            placeholder="请选择日期"
            value-format="YYYY-MM-DD"
            class="apple-date-picker"
          />
        </el-form-item>
        <el-form-item prop="time" label="考试时长" required>
          <el-input-number
            v-model="data.form.time"
            :min="1"
            :max="180"
            placeholder="请输入考试时长（分钟）"
            class="apple-input-number"
          />
        </el-form-item>
        <el-form-item prop="type" label="出题方式" required>
          <el-select 
            v-model="data.form.type" 
            placeholder="请选择出题方式"
            class="apple-select"
          >
            <el-option label="手动选题" value="手动选题" />
            <el-option label="自动组卷" value="自动组卷" />
          </el-select>
        </el-form-item>

        <template v-if="data.form.type === '手动选题'">
          <el-form-item prop="idList" label="选择题目" required>
            <el-select
              v-model="data.form.idList"
              multiple
              placeholder="请选择题目"
              class="apple-select"
            >
              <el-option
                v-for="item in data.questionData"
                :key="item.id"
                :label="item.name + '（' + item.typeName + '）'"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>

        <template v-if="data.form.type === '自动组卷'">
          <el-form-item prop="choiceNum" label="单选题数" required>
            <el-input-number
              v-model="data.form.choiceNum"
              :min="0"
              :max="20"
              placeholder="请输入单选题数量"
              class="apple-input-number"
            />
          </el-form-item>
          <el-form-item prop="multiChoiceNum" label="多选题数" required>
            <el-input-number
              v-model="data.form.multiChoiceNum"
              :min="0"
              :max="10"
              placeholder="请输入多选题数量"
              class="apple-input-number"
            />
          </el-form-item>
          <el-form-item prop="fillInNum" label="填空题数" required>
            <el-input-number
              v-model="data.form.fillInNum"
              :min="0"
              :max="10"
              placeholder="请输入填空题数量"
              class="apple-input-number"
            />
          </el-form-item>
          <el-form-item prop="checkNum" label="判断题数" required>
            <el-input-number
              v-model="data.form.checkNum"
              :min="0"
              :max="10"
              placeholder="请输入判断题数量"
              class="apple-input-number"
            />
          </el-form-item>
          <el-form-item prop="answerNum" label="简答题数" required>
            <el-input-number
              v-model="data.form.answerNum"
              :min="0"
              :max="5"
              placeholder="请输入简答题数量"
              class="apple-input-number"
            />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="add">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Delete,
  Search,
  Refresh,
  Plus
} from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
  courseData: [],
  questionData: []
});

const getStatusType = (status) => {
  const statusMap = {
    '进行中': 'primary',
    '未开始': 'warning',
    '已结束': 'danger'
  };
  return statusMap[status] || 'info';
};

const loadCourse = () => {
  request.get('/course/selectAll', {
    params: {
      teacherId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.courseData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadQuestion = (courseId) => {
  request.get('/question/selectAll', {
    params: {
      courseId: courseId
    }
  }).then(res => {
    if (res.code === '200') {
      data.questionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/testPaper/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const add = () => {
  request.post('/testPaper/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/testPaper/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/testPaper/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.name = null
  load()
}

load()
loadCourse()
</script>

<style scoped>
.apple-testpaper {
  padding: 2rem;
  background-color: #f5f5f7;
  min-height: 100vh;
}

.header-section {
  margin-bottom: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.search-bar {
  display: flex;
  gap: 1rem;
  flex: 1;
  max-width: 500px;
}

.apple-search {
  width: 100%;
}

.apple-search :deep(.el-input__wrapper) {
  background-color: white;
  border-radius: 0.8rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.reset-btn {
  border-radius: 0.8rem;
  background-color: white;
  border: none;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
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

.testpaper-grid {
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

.testpaper-info {
  padding: 0.5rem 0;
}

.testpaper-name {
  font-size: 1rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 0.25rem 0;
}

.testpaper-course {
  font-size: 0.875rem;
  color: #86868b;
  margin: 0;
}

.teacher-name {
  font-size: 0.875rem;
  color: #1d1d1f;
}

.type-tag {
  border-radius: 1rem;
  padding: 0.25rem 0.75rem;
}

.time-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.date-range {
  font-size: 0.875rem;
  color: #1d1d1f;
}

.duration {
  font-size: 0.75rem;
  color: #86868b;
}

.status-tag {
  border-radius: 1rem;
  padding: 0.25rem 0.75rem;
}

.action-column {
  display: flex;
  gap: 0.5rem;
}

.action-column .el-button {
  transition: all 0.3s ease;
}

.action-column .el-button:hover {
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

.apple-form {
  padding: 1.5rem;
}

.apple-input :deep(.el-input__wrapper),
.apple-select :deep(.el-input__wrapper),
.apple-date-picker :deep(.el-input__wrapper),
.apple-input-number :deep(.el-input__wrapper) {
  border-radius: 0.8rem;
  background-color: #f5f5f7;
  border: none;
  box-shadow: none;
}

.apple-input :deep(.el-input__wrapper:hover),
.apple-select :deep(.el-input__wrapper:hover),
.apple-date-picker :deep(.el-input__wrapper:hover),
.apple-input-number :deep(.el-input__wrapper:hover) {
  background-color: #eaeaea;
}

.apple-input :deep(.el-input__wrapper.is-focus),
.apple-select :deep(.el-input__wrapper.is-focus),
.apple-date-picker :deep(.el-input__wrapper.is-focus),
.apple-input-number :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #0071e3;
  background-color: white;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1rem;
  border-top: 1px solid #f5f5f7;
}

.dialog-footer .el-button {
  border-radius: 0.8rem;
  padding: 0.6rem 1.2rem;
}

@media (max-width: 768px) {
  .header-section {
    flex-direction: column;
    align-items: stretch;
  }

  .search-bar {
    max-width: 100%;
  }

  .action-buttons {
    justify-content: space-between;
  }

  .apple-testpaper {
    padding: 1rem;
  }
}
</style>