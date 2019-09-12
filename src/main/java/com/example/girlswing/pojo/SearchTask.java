package com.example.girlswing.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@Getter
@Setter
public class SearchTask extends Task {

    public SearchTask(String filters, String text) {
        super(filters, text);
    }

    public SearchTask(String filters, String text, JProgressBar progressBar) {
        super(filters, text, progressBar);
    }
    public SearchTask(String filters, String text, LinkedList<String> ids,
                      Calendar time, JProgressBar progressBar) {
        super(filters, text, ids, time, progressBar);
    }

    public Set execute(String text, long delay, String girlId, String filters){
        return execute(text, delay, girlId, filters, null);
    }

    public Set execute(String text, long delay, String girlId, String filters, JProgressBar progressBar){
        return new HashSet();
    }
}
