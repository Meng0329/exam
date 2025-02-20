<template>
  <div class="my-share">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">我的分享</h1>
        <p class="page-subtitle">分享你的学习经验和心得体会</p>
        <div class="header-actions">
          <el-button type="primary" class="share-button" @click="initAdd">
            <el-icon><Plus /></el-icon>
            发布新分享
          </el-button>
        </div>
      </div>
    </div>

    <!-- 分享列表 -->
    <div class="share-content">
      <div class="share-grid">
        <div v-for="item in data.tableData" 
             :key="item.id" 
             class="share-card">
          <div class="card-image" 
               :style="{ backgroundImage: `url(${item.img || '/default-cover.jpg'})` }">
            <div class="card-overlay">
              <div class="card-actions">
                <el-button type="primary" link @click="handleEdit(item)" class="action-button">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button type="danger" link @click="del(item.id)" class="action-button">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
          <div class="card-content">
            <h3 class="card-title">{{ item.title }}</h3>
            <div class="card-footer">
              <span class="publish-time">{{ item.time }}</span>
              <el-button type="primary" link @click="viewContent(item.content)" class="preview-button">
                查看内容
              </el-button>
            </div>
          </div>
        </div>
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

    <!-- 发布/编辑对话框 -->
    <el-dialog 
      v-model="data.formVisible" 
      :title="data.form.id ? '编辑分享' : '发布分享'"
      width="70%" 
      destroy-on-close
      class="share-dialog">
      <el-form ref="form" :model="data.form" label-width="80px" class="share-form">
        <el-form-item label="封面图片" prop="img" class="cover-upload">
          <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            list-type="picture-card"
            :limit="1"
            class="apple-upload"
          >
            <el-icon><Plus /></el-icon>
            <div class="upload-text">上传封面</div>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="标题" prop="title">
          <el-input 
            v-model="data.form.title" 
            placeholder="请输入分享标题"
            class="apple-input"
          />
        </el-form-item>
        
        <el-form-item label="内容" prop="content" class="editor-container">
          <div class="editor-wrapper">
            <Toolbar
              :editor="editorRef"
              :mode="mode"
              class="editor-toolbar"
            />
            <Editor
              v-model="data.form.content"
              :mode="mode"
              :defaultConfig="editorConfig"
              @onCreated="handleCreated"
              class="editor-content"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false" class="cancel-button">取消</el-button>
          <el-button type="primary" @click="save" class="confirm-button">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog 
      v-model="data.viewVisible" 
      title="内容预览" 
      width="70%" 
      destroy-on-close
      class="preview-dialog">
      <div class="preview-content" v-html="data.viewContent"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'

const baseUrl = import.meta.env.VITE_BASE_URL
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  viewVisible: false,
  viewContent: null,
})

/* wangEditor5 配置 */
const editorRef = shallowRef()
const mode = 'default'
const editorConfig = {
  MENU_CONF: {
    uploadImage: {
      headers: {
        token: data.user.token,
      },
      server: baseUrl + '/files/wang/upload',
      fieldName: 'file'
    }
  }
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}

const viewContent = (content) => {
  data.viewContent = content
  data.viewVisible = true
}

const load = () => {
  request.get('/article/selectPage', {
    params: {
      studentId: data.user.id,
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

const initAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm(
    '删除后数据无法恢复，确定要删除吗？',
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    request.delete('/article/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

const save = () => {
  if (!data.form.title?.trim()) {
    ElMessage.warning('请输入分享标题')
    return
  }
  if (!data.form.content?.trim()) {
    ElMessage.warning('请输入分享内容')
    return
  }
  
  const saveRequest = data.form.id 
    ? request.put('/article/update', data.form)
    : request.post('/article/add', data.form)
    
  saveRequest.then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleFileUpload = (res) => {
  data.form.img = res.data
}

load()
</script>

<style scoped>
.my-share {
  min-height: 100vh;
  background-color: #ffffff;
}

/* 页面头部 */
.page-header {
  background: linear-gradient(135deg, #ffffff 0%, #f5f5f7 100%);
  padding: 60px 0 40px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
}

.page-title {
  font-size: 48px;
  font-weight: 600;
  margin: 0 0 16px 0;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #1d1d1f 0%, #434343 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: 20px;
  color: #86868b;
  margin: 0 0 32px 0;
}

.header-actions {
  display: flex;
  justify-content: center;
}

.share-button {
  height: 44px;
  padding: 0 24px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 980px;
  background: #0066cc;
  border: none;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.share-button:hover {
  background: #004499;
  transform: translateY(-2px);
}

/* 分享内容区域 */
.share-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.share-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.share-card {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.share-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.card-image {
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.4));
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.share-card:hover .card-overlay {
  opacity: 1;
}

.card-actions {
  display: flex;
  gap: 16px;
}

.action-button {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  transition: all 0.3s ease;
}

.action-button:hover {
  transform: scale(1.1);
}

.card-content {
  padding: 20px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #1d1d1f;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.publish-time {
  font-size: 14px;
  color: #86868b;
}

.preview-button {
  font-size: 14px;
  color: #0066cc;
}

/* 分页样式 */
.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 40px;
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

/* 对话框样式 */
.share-dialog {
  :deep(.el-dialog) {
    border-radius: 20px;
    overflow: hidden;
  }

  :deep(.el-dialog__header) {
    margin: 0;
    padding: 20px 24px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }

  :deep(.el-dialog__body) {
    padding: 24px;
  }

  :deep(.el-dialog__footer) {
    padding: 16px 24px;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
  }
}

.share-form {
  .cover-upload {
    :deep(.el-upload--picture-card) {
      width: 160px;
      height: 160px;
      border-radius: 12px;
    }
  }
}

.editor-container {
  margin-bottom: 0;
}

.editor-wrapper {
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  overflow: hidden;
}

.editor-toolbar {
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.editor-content {
  height: 400px;
}

/* 预览对话框 */
.preview-dialog {
  :deep(.el-dialog) {
    border-radius: 20px;
    overflow: hidden;
  }

  .preview-content {
    padding: 24px;
    font-size: 16px;
    line-height: 1.6;
    color: #1d1d1f;

    :deep(img) {
      max-width: 100%;
      border-radius: 8px;
    }

    :deep(p) {
      margin: 16px 0;
    }
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-title {
    font-size: 36px;
  }

  .page-subtitle {
    font-size: 18px;
  }

  .share-grid {
    grid-template-columns: 1fr;
  }
}
</style>