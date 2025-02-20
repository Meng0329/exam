<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入帖子标题查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData">
        <el-table-column prop="img" label="帖子主图" width="100">
          <template v-slot="scope">
            <el-image style="width: 60px; height: 40px; border-radius: 5px; display: block" v-if="scope.row.img"
                      :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="帖子标题" show-overflow-tooltip/>
        <el-table-column prop="studentName" label="发布人" show-overflow-tooltip width="150"/>
        <el-table-column prop="studentAvatar" label="发布人头像" width="150">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.studentAvatar"
                      :src="scope.row.studentAvatar" :preview-src-list="[scope.row.studentAvatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="分享内容" width="120">
          <template v-slot="scope">
            <el-button type="info" @click="viewContent(scope.row.content)">点击预览</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" width="180"/>
        <el-table-column label="操作" width="200" fixed="right">
          <template v-slot="scope">
            <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
    <el-dialog title="帖子信息" v-model="data.viewVisible" width="60%" destroy-on-close>
      <div style="padding: 0 10px" v-html="data.viewContent"></div>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  viewContent: null,
  viewVisible: false
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
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const viewContent = (content) => {
  data.viewContent = content
  data.viewVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/article/delete/' + id).then(res => {
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

const reset = () => {
  data.title = null
  load()
}

load()
</script>