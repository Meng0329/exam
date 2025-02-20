<template>
  <div class="main-content">
    <div style="margin: 30px 0; text-align: center">
      <el-input size="large" clearable @clear="reset" style="width: 500px; margin-right: 5px" v-model="data.courseName" placeholder="请输入课程名称查询"></el-input>
      <el-button size="large" type="primary" @click="load">查询</el-button>
    </div>
    <div>
      <el-row :gutter="10">
       <el-col :span="6" v-for="item in data.examData" style="margin-bottom: 15px">
         <div class="card" style="cursor: pointer" @click="navTo(item)">
           <img :src="item.courseImg" alt="" style="width: 100%; height: 150px">
           <div class="overflowShow" style="margin-top: 5px; font-size: 15px; color: #333333">{{ item.name }} </div>
           <div style="margin-top: 10px; display: flex; align-items: center">
             <img :src="item.teacherAvatar" alt="" style="width: 25px; height: 25px; border-radius: 50%">
             <div style="flex: 1; margin: 0 10px">{{ item.teacherName }}</div>
             <div style="width: 150px; text-align: right">
               <el-tag v-if="item.status === '进行中'" type="success">{{ item.status }}</el-tag>
               <el-tag v-if="item.status === '未开始'" type="warning">{{ item.status }}</el-tag>
               <el-tag v-if="item.status === '已结束'" type="danger">{{ item.status }}</el-tag>
             </div>
           </div>
           <div style="margin-top: 10px">课程名称：{{ item.courseName }}</div>
           <div style="margin-top: 10px">开放时间：{{ item.start }} ~ {{ item.end }}</div>
         </div>
       </el-col>
      </el-row>
    </div>
    <div v-if="data.total" style="margin: 20px 0">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  courseName: null,
  pageNum: 1,
  pageSize: 8,
  total: 0,
  examData: []
})

const load = () => {
  request.get('/testPaper/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      courseName: data.courseName
    }
  }).then(res => {
    if (res.code === '200') {
      data.examData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const reset = () => {
  data.courseName = null
  load()
}
const navTo = (item) => {
  if (item.status === '未开始') {
    ElMessage.warning('该考试还未开放')
    return
  }
  if (item.status === '已结束') {
    ElMessage.warning('该考试已结束')
    return;
  }
  request.get('/testPaper/check/' + item.id).then(res => {
    if (res.code === '200') {
      location.href = '/front/testPaper?id=' + item.id
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>
<style scope>
.overflowShow {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>