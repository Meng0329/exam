# 在线考试系统

一个基于 Vue3 + Spring Boot 的在线考试系统，提供考试管理、题库管理、在线答题等功能。

## 技术栈

### 前端
- Vue 3.4
- Vue Router 4.3
- Element Plus 2.3.8
- Vite 5.3
- Sass
- Axios

### 后端
- Spring Boot 3.2.2
- MySQL
- MyBatis

## 功能特性

- 用户管理（管理员、教师、学生）
- 考试管理
- 题库管理
- 在线答题
- 成绩管理
- 试卷管理
- 交流分享
- 个人中心

## 项目结构

```
xm_examination/
├── springboot/        # 后端项目
│   ├── src/          # 源代码
│   └── pom.xml       # Maven 配置文件
│
├── vue/              # 前端项目
│   ├── src/          # 源代码
│   ├── public/       # 静态资源
│   └── package.json  # npm 配置文件
│
└── README.md         # 项目说明文档
```

## 快速开始

### 前端启动
```bash
cd vue
npm install
npm run dev
```

### 后端启动
```bash
cd springboot
mvn spring-boot:run
```

## 开发环境要求

- Node.js >= 16
- JDK >= 17（必需，Spring Boot 3.x 要求）
- Maven >= 3.6
- MySQL >= 5.7

## 浏览器支持

- Chrome
- Firefox
- Safari
- Edge

## 注意事项

1. 首次运行前请确保已安装所有依赖
2. 确保 MySQL 服务已启动
3. 检查 application.yml 中的数据库配置是否正确
4. 必须使用 JDK 17 或更高版本，因为 Spring Boot 3.x 不支持低版本 JDK

## 常见问题

1. 如果遇到 "UnsupportedClassVersionError"，请检查 JDK 版本是否为 17 或更高
2. 如果前端依赖安装失败，尝试使用 `npm install --force`
3. 如果后端启动失败，检查 Maven 配置和 JDK 版本

## 贡献指南

1. Fork 本仓库
2. 创建新的分支 `git checkout -b feature/your-feature`
3. 提交更改 `git commit -m 'Add some feature'`
4. 推送到分支 `git push origin feature/your-feature`
5. 提交 Pull Request

## 许可证

[MIT License](LICENSE) 