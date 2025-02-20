package com.example.entity;

import java.util.List;

public class TestPaper {
    // 数据库的基本字段
    private Integer id;
    private String name;
    private Integer courseId;
    private Integer teacherId;
    private String type;
    private String start;
    private String end;
    private Integer time;
    private String questionIds; // 保存该试卷所有的题目的id的json字符串： "[1,2,3,4,5]"

    // 关联课程信息表和教师信息表查询的额外的字段信息
    private String courseName;
    private String courseImg;
    private String teacherName;
    private String teacherAvatar;
    private String status;

    // 用来接收前台那边传过来的业务字段
    private List<Integer> idList; // 接收前台那边手动选题选中的所有的题目的id
    private Integer choiceNum; // 接收前台那边自动组卷填写的单选题的数量
    private Integer multiChoiceNum; // 接收前台那边自动组卷填写的多选题的数量
    private Integer fillInNum; // 接收前台那边自动组卷填写的填空题的数量
    private Integer checkNum; // 接收前台那边自动组卷填写的判断题的数量
    private Integer answerNum; // 接收前台那边自动组卷填写的简答题的数量

    private List<Question> questions;
    private Integer maxTime; // 存储时长（秒）

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public Integer getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(Integer choiceNum) {
        this.choiceNum = choiceNum;
    }

    public Integer getMultiChoiceNum() {
        return multiChoiceNum;
    }

    public void setMultiChoiceNum(Integer multiChoiceNum) {
        this.multiChoiceNum = multiChoiceNum;
    }

    public Integer getFillInNum() {
        return fillInNum;
    }

    public void setFillInNum(Integer fillInNum) {
        this.fillInNum = fillInNum;
    }

    public Integer getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }

    public Integer getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Integer answerNum) {
        this.answerNum = answerNum;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }
}
