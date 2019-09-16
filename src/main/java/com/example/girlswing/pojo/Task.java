package com.example.girlswing.pojo;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class Task {

    protected long id;
    protected String name;
    protected String filters;
    protected String text;
    protected List ids;
    protected Calendar time;
    protected JProgressBar progressBar;

    public Task(long id, String name, String filters, String text) {
        this.id=id;
        this.name = name;
        this.filters = filters;
        this.text = text;
    }

    public Task(long id, String name, String filters, String text, JProgressBar progressBar) {
        this.id=id;
        this.name = name;
        this.filters = filters;
        this.text = text;
        this.progressBar=progressBar;
    }

    public Task(long id, String name, String filters, String text, LinkedList<String> ids,
                Calendar time, JProgressBar progressBar) {
        this.id=id;
        this.name = name;
        this.filters = filters;
        this.text = text;
        this.ids = ids;
        this.time = time;
        this.progressBar=progressBar;
    }

    abstract public Set execute(String filters, String text, int chatDelay, String girlId,  Set setIncremented) throws InvocationTargetException, InterruptedException;

    abstract public Set execute(String filters, String text, List<String> ids, int chatDelay,  String girlId,
                                 Set setIncremented) throws InvocationTargetException, InterruptedException;

    abstract public Set execute(String filters, String text, JProgressBar progressBar,
                                long delay, String girlId, Set<Connection> beforeConnections) throws InvocationTargetException, InterruptedException;

    abstract public Set execute(String filters, String text, List<String> ids, JProgressBar progressBar,
                                long delay, String girlId, Set<Connection> beforeConnections) throws InvocationTargetException, InterruptedException;

    abstract public Set executeMail(String filters, String text, int mailDelay, String girlId,  Set setIncremented) throws InvocationTargetException, InterruptedException;

    abstract public Set executeMail(String filters, String text, List<String> ids, int mailDelay,  String girlId,
                                Set setIncremented) throws InvocationTargetException, InterruptedException;

    abstract public Set executeMail(String filters, String text, JProgressBar progressBar,
                                long delay, String girlId, Set<Connection> beforeConnections) throws InvocationTargetException, InterruptedException;

    abstract public Set executeMail(String filters, String text, List<String> ids, JProgressBar progressBar,
                                long delay, String girlId, Set<Connection> beforeConnections) throws InvocationTargetException, InterruptedException;

    public boolean equals(Object obj) {
        return (this.filters.equals(((Task)obj).getFilters()) &&
                this.getClass().getName().equals(((Task)obj).getClass().getName()));
    }

}
