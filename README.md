# 在线考试系统

基于 Java + Vue + DeepSeek-R1 的在线考试系统

## 项目介绍

本项目是一个现代化的在线考试系统，采用前后端分离架构，具有以下特点：

- 前端：Vue3 + Element Plus + Vite
- 后端：Spring Boot + MyBatis + MySQL
- AI 助手：DeepSeek-R1

## 功能特性

### 考生端
- 在线考试
- 查看成绩
- 个人中心
- 考试记录
- 在线讨论

### 教师端
- 试题管理
- 试卷管理
- 考试计划
- 成绩管理
- 学生管理

### 管理员端
- 用户管理
- 权限管理
- 系统设置
- 公告管理

## 项目结构

```
xm_examination/
├── springboot/        # 后端项目
│   ├── src/          # 源代码
│   └── pom.xml       # Maven 配置文件
│
├── vue/              # 前端项目
│   ├── src/         # 源代码
│   └── package.json # npm 配置文件
│
└── xm_examination.sql # 数据库文件
```

## 开发环境

- JDK 1.8+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+
- IDE: IntelliJ IDEA、VS Code

## 快速开始

1. 克隆项目
```bash
git clone https://github.com/Meng0329/exam.git
```

2. 导入数据库
```bash
mysql -u root -p < xm_examination.sql
```

3. 启动后端
```bash
cd springboot
mvn spring-boot:run
```

4. 启动前端
```bash
cd vue
npm install
npm run dev
```

5. 访问系统
- 前端页面：http://localhost:5173
- 后端接口：http://localhost:8080

## 贡献指南

1. Fork 本仓库
2. 创建新的功能分支
3. 提交代码
4. 创建 Pull Request

## 许可证

本项目采用 [Apache 2.0 许可证](LICENSE) 