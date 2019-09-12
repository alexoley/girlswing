package com.example.girlswing.pojo;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public abstract class Task {

    protected String filters;
    protected String text;
    protected List ids;
    protected Calendar time;
    protected Task child;
    protected JProgressBar progressBar;

    public Task(String filters, String text) {
        this.filters = filters;
        this.text = text;
    }

    public Task(String filters, String text, JProgressBar progressBar) {
        this.filters = filters;
        this.text = text;
        this.progressBar=progressBar;
    }

    public Task(String filters, String text, LinkedList<String> ids,
                Calendar time, Task child, JProgressBar progressBar) {
        this.filters = filters;
        this.text = text;
        this.ids = ids;
        this.time = time;
        this.child = child;
        this.progressBar=progressBar;
    }

    abstract public void execute(String text, long delay, String girlId, String filters);

    abstract public void execute(String text, long delay, String girlId, String filters, JProgressBar progressBar);

    public boolean equals(Object obj) {
        return (this!=null && obj!=null && this.filters.equals(((Task)obj).getFilters()) &&
                this.getClass().getName().equals(((Task)obj).getClass().getName()));
    }
}
