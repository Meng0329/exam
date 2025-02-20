package com.example.entity;

import lombok.Data;

@Data
public class Answer {

    private Integer questionId;
    private String questionName;
    private String typeName;
    private Integer score;
    private String answer;
    private String newAnswer;
    private Integer result;
    
    // AI 辅助评分相关字段
    private Integer aiSuggestedScore;
    private String aiFeedback;
    private String scoringDetails;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getNewAnswer() {
        return newAnswer;
    }

    public void setNewAnswer(String newAnswer) {
        this.newAnswer = newAnswer;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }
}
