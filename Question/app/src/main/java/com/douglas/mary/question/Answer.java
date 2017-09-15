package com.douglas.mary.question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mary on 2017-05-31.
 */

public class Answer {

    private List<String> answre;

    public Answer() {
        this(new ArrayList<String>() );
    }

    public Answer(List<String> answre) {
        this.answre = answre;
    }

    public List<String> getAnswre() {
        return answre;
    }

    public void setAnswre(List<String> answre) {
        this.answre = answre;
    }
    public void add(String string){
        answre.add(string);
    }
    public String getAnswer(int index){
        return answre.get(index);
    }
    public void setAnswer(int index,String string){
        answre.set(index,string);
    }


}
