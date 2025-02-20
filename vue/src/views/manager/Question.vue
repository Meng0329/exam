<template>
  <div class="apple-question">
    <!-- 顶部搜索和操作区 -->
    <div class="header-section">
      <div class="search-bar">
        <el-input
          v-model="data.name"
          placeholder="搜索题目..."
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
          <el-icon><Plus /></el-icon>新增题目
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

    <!-- 题目列表 -->
    <div class="question-grid">
      <el-table
        :data="data.tableData"
        @selection-change="handleSelectionChange"
        class="apple-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="题目名称" min-width="200">
          <template #default="scope">
            <div class="question-info">
              <h3 class="question-name">{{ scope.row.name }}</h3>
              <p class="question-course">{{ scope.row.courseName }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="授课教师" width="120">
          <template #default="scope">
            <span class="teacher-name">{{ scope.row.teacherName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="typeName" label="题型" width="100">
          <template #default="scope">
            <el-tag
              :type="getQuestionTypeTag(scope.row.typeName)"
              class="question-type-tag"
            >
              {{ scope.row.typeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="typeScore" label="分数" width="80">
          <template #default="scope">
            <div class="score-badge">{{ scope.row.typeScore }}</div>
          </template>
        </el-table-column>
        <el-table-column label="选项" min-width="250">
          <template #default="scope">
            <div class="options-container" v-if="scope.row.typeName === '单选题' || scope.row.typeName === '多选题'">
              <div class="option-item" v-for="(option, key) in getOptions(scope.row)" :key="key">
                <span class="option-key">{{ key }}.</span>
                <span class="option-value">{{ option }}</span>
              </div>
            </div>
            <span v-else class="no-options">无选项</span>
          </template>
        </el-table-column>
        <el-table-column prop="answer" label="答案" min-width="150">
          <template #default="scope">
            <div class="answer-text">{{ scope.row.answer }}</div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <div class="action-column">
              <el-button
                v-if="data.user.role === 'TEACHER'"
                type="primary"
                circle
                class="edit-button"
                :icon="Edit"
                @click="handleEdit(scope.row)"
              />
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="data.form.id ? '编辑题目' : '新增题目'"
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
          <el-select v-model="data.form.courseId" placeholder="请选择课程" class="apple-select">
            <el-option
              v-for="item in data.courseData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="name" label="题目名称" required>
          <el-input
            v-model="data.form.name"
            placeholder="请输入题目名称"
            class="apple-input"
          />
        </el-form-item>
        <el-form-item prop="typeId" label="选择题型" required>
          <el-select v-model="data.form.typeId" placeholder="请选择题型" class="apple-select">
            <el-option
              v-for="item in data.typeData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <template v-if="data.form.typeId === 1 || data.form.typeId === 2">
          <el-form-item
            v-for="option in ['A', 'B', 'C', 'D']"
            :key="option"
            :prop="'option' + option"
            :label="'选项' + option"
            required
          >
            <el-input
              v-model="data.form['option' + option]"
              :placeholder="'请输入选项' + option"
              class="apple-input"
            />
          </el-form-item>
        </template>

        <el-form-item prop="answer" label="题目答案" required>
          <template v-if="data.form.typeId === 1 || data.form.typeId === 2">
            <el-input
              v-model="data.form.answer"
              placeholder="请输入题目答案（多选答案用英文逗号隔开）"
              class="apple-input"
            />
          </template>
          <template v-else-if="data.form.typeId === 3">
            <el-select v-model="data.form.answer" placeholder="请选择答案" class="apple-select">
              <el-option label="正确" value="正确" />
              <el-option label="错误" value="错误" />
            </el-select>
          </template>
          <template v-else-if="data.form.typeId === 4">
            <el-input
              v-model="data.form.answer"
              placeholder="请输入题目答案"
              class="apple-input"
            />
          </template>
          <template v-else-if="data.form.typeId === 5">
            <el-input
              type="textarea"
              :rows="8"
              v-model="data.form.answer"
              placeholder="请输入题目答案"
              class="apple-input"
            />
          </template>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
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
  Edit,
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
  typeData: [],
});

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

const getOptions = (row) => {
  return {
    A: row.optionA,
    B: row.optionB,
    C: row.optionC,
    D: row.optionD
  };
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
const loadType = () => {
  request.get('/questionType/selectAll').then(res => {
    if (res.code === '200') {
      data.typeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/question/selectPage', {
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
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/question/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/question/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/question/delete/' + id).then(res => {
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
    request.delete("/question/delete/batch", {data: data.ids}).then(res => {
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
loadType()
</script>

<style scoped>
.apple-question {
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

.question-grid {
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

.question-info {
  padding: 0.5rem 0;
}

.question-name {
  font-size: 1rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 0.25rem 0;
}

.question-course {
  font-size: 0.875rem;
  color: #86868b;
  margin: 0;
}

.teacher-name {
  font-size: 0.875rem;
  color: #1d1d1f;
}

.question-type-tag {
  border-radius: 1rem;
  padding: 0.25rem 0.75rem;
}

.score-badge {
  background: #f5f5f7;
  color: #1d1d1f;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  display: inline-block;
  font-weight: 500;
}

.options-container {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.option-item {
  display: flex;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.option-key {
  color: #86868b;
  font-weight: 500;
}

.option-value {
  color: #1d1d1f;
}

.no-options {
  color: #86868b;
  font-style: italic;
}

.answer-text {
  color: #1d1d1f;
  font-size: 0.875rem;
  line-height: 1.4;
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
.apple-select :deep(.el-input__wrapper) {
  border-radius: 0.8rem;
  background-color: #f5f5f7;
  border: none;
  box-shadow: none;
}

.apple-input :deep(.el-input__wrapper:hover),
.apple-select :deep(.el-input__wrapper:hover) {
  background-color: #eaeaea;
}

.apple-input :deep(.el-input__wrapper.is-focus),
.apple-select :deep(.el-input__wrapper.is-focus) {
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

  .apple-question {
    padding: 1rem;
  }
}
</style>