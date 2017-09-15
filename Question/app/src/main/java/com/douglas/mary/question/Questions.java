package com.douglas.mary.question;

/**
 * Created by Mary on 2017-05-06.
 */

public class Questions {


    private String lable;
    private String question;
    private String answer;
    private String type;
    private String nameQuestion;
    private String necessary;

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public String getNecessary() {return necessary;}

    public void setNecessary(String necessary) { this.necessary = necessary;}

    @Override
    public String toString() {
        return
                "lable='" + lable + '\'' +
                        ", question='" + question + '\'' +
                        ", answer='" + answer + '\'' +
                        ", type='" + type + '\'' +
                        ", nameQuestion='" + nameQuestion + '\'' +
                        ", necessary='" + necessary + '\'' +
                        "\t"
                ;
    }

}
