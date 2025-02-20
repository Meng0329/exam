/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : xm_examination

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 12/10/2024 22:52:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/download/1721114905635-柴犬.jpeg', 'ADMIN', '18899990011', 'admin2@xm.com');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '帖子主图',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '帖子内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  `student_id` int(10) NULL DEFAULT NULL COMMENT '学生ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (2, '关于学习Java枚举的笔记经验分享', 'http://localhost:9090/files/download/1728563732106-2.png', '<p><strong>基于Springboot3+Vue3的在线考试系统</strong></p><p>角色：管理员、教师、学生</p><p>功能模块：</p><p>登录注册：（已完成）</p><p>个人中心：（已完成）</p><p>修改密码：（已完成）</p><p>管理员信息：（已完成）</p><p>教师信息：（已完成）</p><p>学生信息：（已完成）</p><p>考试安排：（已完成）</p><p>题型管理：包括单选、多选、判断、填空、简答（已完成）</p><p>课程管理：（已完成）</p><p>学习交流管理：</p><p>题库管理：题库里的题目需要根据课程进行分类，不同的课程都得有对应的题库，教师可以给自己的课程题库里新增试题（已完成）</p><p>组卷：选择课程出卷时，支持随机和人工从题库里抽选题目，组成试卷</p><p>试卷预览：</p><p>成绩管理：</p><p>自动阅卷：客观题自动阅卷，主观题（简答）可以支持手动给分</p><p>前台学生：</p><p>查看考试安排、查看试卷</p><p>在线答题：试卷每种题型在线展示，供学生答题</p><p>学习交流：发布学习心得</p><p><img src=\"http://localhost:9090/files/download/1728566947212-柴犬.jpeg\" alt=\"\" data-href=\"\" style=\"\"/></p>', '2024-10-10 20:35:34', 1);
INSERT INTO `article` VALUES (3, '武哥10万字Springboot经典教程学习笔记', 'http://localhost:9090/files/download/1728564558044-9.png', '66666', '2024-10-10 20:49:20', 2);
INSERT INTO `article` VALUES (5, '在线考试系统功能点描述分享', 'http://localhost:9090/files/download/1728564833337-10.png', '<p>基于Springboot3+Vue3的在线考试系统</p><p>角色：管理员、教师、学生</p><p>功能模块：</p><p>登录注册：（已完成）</p><p>个人中心：（已完成）</p><p>修改密码：（已完成）</p><p>管理员信息：（已完成）</p><p>教师信息：（已完成）</p><p>学生信息：（已完成）</p><p>考试安排：（已完成）</p><p>题型管理：包括单选、多选、判断、填空、简答（已完成）</p><p>课程管理：（已完成）</p><p>学习交流管理：</p><p><br></p><p>题库管理：题库里的题目需要根据课程进行分类，不同的课程都得有对应的题库，教师可以给自己的课程题库里新增试题（已完成）</p><p>组卷：选择课程出卷时，支持随机和人工从题库里抽选题目，组成试卷</p><p>试卷预览：</p><p>成绩管理：</p><p>自动阅卷：客观题自动阅卷，主观题（简答）可以支持手动给分</p><p>前台学生：</p><p>查看考试安排、查看试卷</p><p>在线答题：试卷每种题型在线展示，供学生答题</p><p>学习交流：发布学习心得</p><p><br></p>', '2024-10-10 20:53:55', 1);
INSERT INTO `article` VALUES (6, 'Vue3基础知识学习笔记', 'http://localhost:9090/files/download/1728568543079-7.png', '<p>哈哈哈</p><p>呵呵呵</p><p>嘿嘿嘿</p><p>嘎嘎嘎</p><p>啦啦啦</p>', '2024-10-10 21:55:52', 1);
INSERT INTO `article` VALUES (7, '前端三件套html，css，js学习心得', 'http://localhost:9090/files/download/1728568566399-right.jpg', '<p>哈哈哈</p><p>哈哈哈</p><p>嘿嘿黑</p><p>呵呵呵</p><p>嘎嘎嘎</p>', '2024-10-10 21:56:13', 1);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程封面',
  `score` int(10) NULL DEFAULT NULL COMMENT '课程学分',
  `teacher_id` int(10) NULL DEFAULT NULL COMMENT '教师ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (2, 'Java基础入门课', 'http://localhost:9090/files/download/1728545691210-Java基础入门课程.jpg', 3, 1);
INSERT INTO `course` VALUES (3, 'Vue最新实战教程', 'http://localhost:9090/files/download/1728545950698-Vue最新教程.jpg', 3, 1);
INSERT INTO `course` VALUES (4, 'Java入学课程', 'http://localhost:9090/files/download/1728545989297-Java入学课程.jpg', 3, 2);

-- ----------------------------
-- Table structure for exam_plan
-- ----------------------------
DROP TABLE IF EXISTS `exam_plan`;
CREATE TABLE `exam_plan`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '考试安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_plan
-- ----------------------------
INSERT INTO `exam_plan` VALUES (1, '关于第一学期期末考试的安排', '考试时间：xxxx-xx-xx，考试地点：xxxx。注意事项：xxxx', '2024-10-10 14:21:13');
INSERT INTO `exam_plan` VALUES (2, '关于考试考场纪律和作弊处罚措施', '本着公平公正原则，考试过程严禁作弊，请监考老师以及同学严格遵守，一旦发现，会取消该门课的考试资格，等待补考，如果补考作弊，该门课需要重修！', '2024-10-10 14:21:28');
INSERT INTO `exam_plan` VALUES (3, '信息工程学院期末考试安排', '本着公平公正原则，考试过程严禁作弊，请监考老师以及同学严格遵守，一旦发现，会取消该门课的考试资格，等待补考，如果补考作弊，该门课需要重修！', '2024-10-10 14:21:40');
INSERT INTO `exam_plan` VALUES (4, '软件学院期末考试安排', '考试时间：xxxx-xx-xx，考试地点：xxxx。注意事项：xxxx', '2024-10-10 14:21:51');
INSERT INTO `exam_plan` VALUES (5, '经济管理学期末考试安排', '考试时间：xxxx-xx-xx，考试地点：xxxx。注意事项：xxxx', '2024-10-10 14:22:05');
INSERT INTO `exam_plan` VALUES (6, '物流工程学院考试安排', '考试时间：xxxx-xx-xx，考试地点：xxxx。注意事项：xxxx', '2024-10-12 22:35:08');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '今天天气真的不错', '今天好开心呀！因为天气很好，我又可以肆无忌惮的跟着武哥和青哥学习了！！真的好开心！', '2024-07-16 15:51:17');
INSERT INTO `notice` VALUES (2, '项目所有功能开发完毕！', '我的项目所有的功能都开发完啦！真的很有成就感！', '2024-07-16 15:52:22');
INSERT INTO `notice` VALUES (3, '项目功能都测试完成，准备上线！', '经过半个月的学习和练习，终于把这个项目完成了，可以打包上线了！', '2024-07-16 15:52:56');

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '题目名称',
  `course_id` int(10) NULL DEFAULT NULL COMMENT '课程ID',
  `teacher_id` int(10) NULL DEFAULT NULL COMMENT '教师ID',
  `type_id` int(10) NULL DEFAULT NULL COMMENT '题型ID',
  `option_a` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '选项A',
  `option_b` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '选项B',
  `option_c` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '选项C',
  `option_d` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '选项D',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '题目答案',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '题目信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (2, 'Java是世界上最好的语言！', 2, 1, 3, NULL, NULL, NULL, NULL, '正确');
INSERT INTO `question` VALUES (3, '下面哪一项不是Java基本数据类型？', 2, 1, 1, 'int', 'int', 'float', 'let', 'D');
INSERT INTO `question` VALUES (4, '下面哪些选项是Java的特点？', 2, 1, 2, 'Java是后端语言', 'Java是前端语言', 'Java是强类型语言', 'Java是弱类型语言', 'A,C');
INSERT INTO `question` VALUES (5, 'Java语言的创造者是______。', 2, 1, 4, NULL, NULL, NULL, NULL, 'James Gosling');
INSERT INTO `question` VALUES (6, '请写一个冒泡排序算法。', 2, 1, 5, NULL, NULL, NULL, NULL, 'public void bubbleSort(int[] source) {\n    for(int i = source.length - 1; i > 0; i--) {\n        for(int j = 0; j < i; j++) {\n            if(a[j] > a[j+1])\n                swap(source, j, j+1); \n        }\n    }\n}');
INSERT INTO `question` VALUES (7, '所有的Java项目里都有一个启动的______方法。', 4, 2, 4, NULL, NULL, NULL, NULL, 'main');

-- ----------------------------
-- Table structure for question_type
-- ----------------------------
DROP TABLE IF EXISTS `question_type`;
CREATE TABLE `question_type`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '题型名称',
  `score` int(10) NULL DEFAULT NULL COMMENT '题型分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '题型信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_type
-- ----------------------------
INSERT INTO `question_type` VALUES (1, '单选题', 4);
INSERT INTO `question_type` VALUES (2, '多选题', 5);
INSERT INTO `question_type` VALUES (3, '判断题', 2);
INSERT INTO `question_type` VALUES (4, '填空题', 4);
INSERT INTO `question_type` VALUES (5, '简答题', 20);

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `course_id` int(10) NULL DEFAULT NULL COMMENT '课程ID',
  `teacher_id` int(10) NULL DEFAULT NULL COMMENT '教师ID',
  `student_id` int(10) NULL DEFAULT NULL COMMENT '学生ID',
  `paper_id` int(10) NULL DEFAULT NULL COMMENT '试卷ID',
  `score` int(10) NULL DEFAULT NULL COMMENT '成绩得分',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '提交结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '成绩信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO `score` VALUES (2, 'Java基础入门课小测验2', 2, 1, 1, 2, 13, '已阅卷', '[{\"typeName\":\"单选题\",\"questionId\":3,\"score\":4,\"answer\":\"D\",\"newAnswer\":\"D\"},{\"typeName\":\"多选题\",\"questionId\":4,\"score\":5,\"answer\":\"A,C\",\"newAnswer\":\"A,C\"},{\"typeName\":\"判断题\",\"questionId\":2,\"score\":2,\"answer\":\"正确\",\"newAnswer\":\"正确\"},{\"typeName\":\"填空题\",\"questionId\":5,\"score\":4,\"answer\":\"James Gosling\",\"newAnswer\":\"张三\"},{\"typeName\":\"简答题\",\"questionId\":6,\"score\":20,\"answer\":\"public void bubbleSort(int[] source) {\\n    for(int i = source.length - 1; i > 0; i--) {\\n        for(int j = 0; j < i; j++) {\\n            if(a[j] > a[j+1])\\n                swap(source, j, j+1); \\n        }\\n    }\\n}\",\"newAnswer\":\"public static void main() \"}]');
INSERT INTO `score` VALUES (3, 'Java基础入门课小测验3', 2, 1, 1, 3, NULL, '待阅卷', '[{\"typeName\":\"单选题\",\"questionId\":3,\"score\":4,\"answer\":\"D\",\"newAnswer\":\"D\"},{\"typeName\":\"多选题\",\"questionId\":4,\"score\":5,\"answer\":\"A,C\",\"newAnswer\":\"A,C\"},{\"typeName\":\"简答题\",\"questionId\":6,\"score\":20,\"answer\":\"public void bubbleSort(int[] source) {\\n    for(int i = source.length - 1; i > 0; i--) {\\n        for(int j = 0; j < i; j++) {\\n            if(a[j] > a[j+1])\\n                swap(source, j, j+1); \\n        }\\n    }\\n}\",\"newAnswer\":\"public void bubbleSort(int[] source) {\\n    for(int i = source.length - 1; i > 0; i--) {\\n        for(int j = 0; j < i; j++) {\\n            if(a[j] > a[j+1])\\n                swap(source, j, j+1); \\n        }\\n    }\\n}\"}]');
INSERT INTO `score` VALUES (4, 'Java基础入门课小测验1', 2, 1, 1, 1, 20, '已阅卷', '[{\"typeName\":\"判断题\",\"questionId\":2,\"score\":2,\"answer\":\"正确\",\"newAnswer\":\"正确\"},{\"typeName\":\"单选题\",\"questionId\":3,\"score\":4,\"answer\":\"D\",\"newAnswer\":\"D\"},{\"typeName\":\"多选题\",\"questionId\":4,\"score\":5,\"answer\":\"A,C\",\"newAnswer\":\"C,A\"},{\"typeName\":\"填空题\",\"questionId\":5,\"score\":4,\"answer\":\"James Gosling\",\"newAnswer\":\"James Gosling\"},{\"typeName\":\"简答题\",\"questionId\":6,\"score\":20,\"answer\":\"public void bubbleSort(int[] source) {\\n    for(int i = source.length - 1; i > 0; i--) {\\n        for(int j = 0; j < i; j++) {\\n            if(a[j] > a[j+1])\\n                swap(source, j, j+1); \\n        }\\n    }\\n}\",\"newAnswer\":\"public static void main( ) {\\n   System.print......\\n   老师我不会了，给我点辛苦分吧~\\n}\"}]');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, 'zhangsan', '123456', '张三', 'http://localhost:9090/files/download/1728486694570-柴犬.jpeg', 'STUDENT', '18899990000', 'zhangsan@xm.com', '审核通过');
INSERT INTO `student` VALUES (2, 'lisi', '123456', '李四', 'http://localhost:9090/files/download/1728486728196-拉布拉多.jpeg', 'STUDENT', '18899995555', 'lisi@xm.com', '审核通过');
INSERT INTO `student` VALUES (3, 'wangwu', '123456', '王五', 'http://localhost:9090/files/download/1728486753434-柯基.jpeg', 'STUDENT', '18844445555', 'wangwu@xm.com', '审核通过');
INSERT INTO `student` VALUES (4, 'zhaoliu', '123456', 'zhaoliu', NULL, 'STUDENT', NULL, NULL, '待审核');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'zhang', '123456', '张老师', 'http://localhost:9090/files/download/1728484762449-柴犬.jpeg', 'TEACHER', '18800001111', 'zhang@xm.com');
INSERT INTO `teacher` VALUES (2, 'li', '123456', '李老师', 'http://localhost:9090/files/download/1728484862680-柯基.jpeg', 'TEACHER', '18877776666', 'li@xm.com');
INSERT INTO `teacher` VALUES (3, 'zhao', '123456', 'zhao', NULL, 'TEACHER', NULL, NULL);

-- ----------------------------
-- Table structure for test_paper
-- ----------------------------
DROP TABLE IF EXISTS `test_paper`;
CREATE TABLE `test_paper`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `course_id` int(10) NULL DEFAULT NULL COMMENT '课程ID',
  `teacher_id` int(10) NULL DEFAULT NULL COMMENT '教师ID',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结束时间',
  `time` int(10) NULL DEFAULT NULL COMMENT '考试时长',
  `question_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '题目IDs',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '试卷信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_paper
-- ----------------------------
INSERT INTO `test_paper` VALUES (1, 'Java基础入门课小测验1', 2, 1, '手动选题', '2024-10-11', '2024-10-26', 60, '[2,3,4,5,6]');
INSERT INTO `test_paper` VALUES (2, 'Java基础入门课小测验2', 2, 1, '自动组卷', '2024-10-11', '2024-10-26', 60, '[3,4,2,5,6]');
INSERT INTO `test_paper` VALUES (3, 'Java基础入门课小测验3', 2, 1, '自动组卷', '2024-10-11', '2024-10-24', 60, '[3,4,6]');
INSERT INTO `test_paper` VALUES (4, 'Java基础入门小测验4', 2, 1, '手动选题', '2024-10-12', '2025-11-13', 60, '[3,4,2,6]');
INSERT INTO `test_paper` VALUES (5, 'Java基础入门小测验5', 2, 1, '自动组卷', '2024-10-12', '2025-10-09', 60, '[3,4,2,5,6]');

SET FOREIGN_KEY_CHECKS = 1;
