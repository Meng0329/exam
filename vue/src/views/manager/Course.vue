<template>
  <div class="apple-course">
    <!-- 顶部搜索和操作区 -->
    <div class="header-section">
      <div class="search-bar">
        <el-input
          v-model="data.name"
          placeholder="搜索课程..."
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
          <el-icon><Plus /></el-icon>新增课程
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

    <!-- 课程卡片网格 -->
    <div class="course-grid">
      <el-table
        :data="data.tableData"
        @selection-change="handleSelectionChange"
        class="apple-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="img" label="课程封面" width="180">
          <template #default="scope">
            <div class="course-cover">
              <el-image
                v-if="scope.row.img"
                :src="scope.row.img"
                :preview-src-list="[scope.row.img]"
                preview-teleported
                fit="cover"
              />
              <div v-else class="course-cover-placeholder">
                <el-icon><Picture /></el-icon>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称">
          <template #default="scope">
            <div class="course-info">
              <h3 class="course-name">{{ scope.row.name }}</h3>
              <p class="course-teacher">授课教师：{{ scope.row.teacherName }}</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="学分" width="100">
          <template #default="scope">
            <div class="course-credit">
              {{ scope.row.score }} 学分
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
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
      :title="data.form.id ? '编辑课程' : '新增课程'"
      v-model="data.formVisible"
      width="500px"
      destroy-on-close
      class="apple-dialog"
    >
      <el-form
        ref="form"
        :model="data.form"
        label-width="80px"
        class="apple-form"
      >
        <el-form-item label="课程封面" prop="img">
          <div class="upload-section">
            <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture-card"
              :limit="1"
              class="apple-upload"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="课程名称" prop="name" required>
          <el-input
            v-model="data.form.name"
            placeholder="请输入课程名称"
            class="apple-input"
          />
        </el-form-item>
        <el-form-item label="课程学分" prop="score" required>
          <el-input-number
            v-model="data.form.score"
            :min="0"
            :max="10"
            class="apple-input-number"
          />
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
  Plus,
  Picture
} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL;

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
});

const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
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
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/course/add', data.form).then(res => {
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
  request.put('/course/update', data.form).then(res => {
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
    request.delete('/course/delete/' + id).then(res => {
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
    request.delete("/course/delete/batch", {data: data.ids}).then(res => {
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

const handleFileUpload = (res) => {
  data.form.img = res.data
}

load()
</script>

<style scoped>
.apple-course {
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

.course-grid {
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

.course-cover {
  width: 160px;
  height: 90px;
  border-radius: 0.8rem;
  overflow: hidden;
  background-color: #f5f5f7;
}

.course-cover .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #86868b;
  font-size: 2rem;
}

.course-info {
  padding: 0.5rem 0;
}

.course-name {
  font-size: 1rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 0.25rem 0;
}

.course-teacher {
  font-size: 0.875rem;
  color: #86868b;
  margin: 0;
}

.course-credit {
  font-size: 0.875rem;
  color: #1d1d1f;
  background: #f5f5f7;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  display: inline-block;
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
  padding: 1rem;
}

.upload-section {
  display: flex;
  justify-content: center;
}

.apple-upload :deep(.el-upload--picture-card) {
  border-radius: 0.8rem;
  border: 2px dashed #e5e5e5;
  background-color: #f5f5f7;
  transition: all 0.3s ease;
}

.apple-upload :deep(.el-upload--picture-card:hover) {
  border-color: #0071e3;
  background-color: #f0f7ff;
}

.apple-input :deep(.el-input__wrapper) {
  border-radius: 0.8rem;
  background-color: #f5f5f7;
}

.apple-input-number :deep(.el-input__wrapper) {
  border-radius: 0.8rem;
  background-color: #f5f5f7;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1rem;
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
}
</style>