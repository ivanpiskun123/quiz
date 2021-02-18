package com.example.quizz;

import java.util.ArrayList;
import java.util.List;

public class Record {

    static public List<Record> recordsList = new ArrayList<Record>();

    public String email;
    public int record;
    public QuizThemes theme;

    public Record(String email, int record, QuizThemes theme){
        this.email = email;
        this.record = record;
        this.theme = theme;
        recordsList.add(this);
    }

}
