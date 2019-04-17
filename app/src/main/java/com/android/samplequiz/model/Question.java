package com.android.samplequiz.model;

import java.util.List;

import javax.inject.Inject;

public class Question {

    private int id;
    private String statement;
    private List<String> options;

    @Inject
    public Question() {
    }

    public Question(int id, String statement, List<String> options) {
        this.id = id;
        this.statement = statement;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", statement='" + statement + '\'' +
                ", options=" + options +
                '}';
    }
}
