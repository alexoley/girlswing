package com.example.girlswing.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Calendar;
import java.util.LinkedList;

@Getter
@Setter
public class SearchTask extends Task {

    public SearchTask(String filters, String text) {
        super(filters, text);
    }

    public SearchTask(String filters, String text, JProgressBar progressBar) {
        super(filters, text, progressBar);
    }
    public SearchTask(String name, String filters, String text, LinkedList<String> ids,
                      Calendar time, Task child, JProgressBar progressBar) {
        super(filters, text, ids, time, child, progressBar);
    }

    public void execute(String text, long delay, String girlId, String filters){
        execute(text, delay, girlId, filters, null);
    }

    public void execute(String text, long delay, String girlId, String filters, JProgressBar progressBar){

    }
}
