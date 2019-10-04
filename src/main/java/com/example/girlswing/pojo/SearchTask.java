package com.example.girlswing.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.*;

@Getter
@Setter
@Slf4j
@Component
@Scope("prototype")
public class SearchTask extends Task {

    public SearchTask(){

    }

    public SearchTask(long id, String name, String filters, String text) {
        super(id, name, filters, text);
    }

    public SearchTask(long id, String name, String filters, String text, JProgressBar progressBar) {
        super(id, name, filters, text, progressBar);
    }
    public SearchTask(long id, String name, String filters, String text, LinkedList<String> ids,
                      Calendar time, JProgressBar progressBar) {
        super(id, name, filters, text, ids, time, progressBar);
    }

    @Override
    public Set execute(String filters, String text, int chatDelay, String girlId,  Set setIncremented) {
        return execute(filters, text, (List<String>) null, chatDelay, girlId, setIncremented);
    }

    @Override
    public Set execute(String filters, String text, List<String> ids, int chatDelay,  String girlId,  Set setIncremented) {
        return execute(filters, text, ids, null ,chatDelay, girlId, setIncremented);
    }

    @Override
    public Set execute(String filters, String text, JProgressBar progressBar,
                       long chatDelay, String girlId, Set<Connection> setIncremented){
        return execute(filters, text, null, progressBar ,chatDelay, girlId, setIncremented);
    }

    @Override
    public Set execute(String filters, String text, List<String> ids, JProgressBar progressBar,
                       long delay, String girlId, Set<Connection> beforeConnections) {
        return new HashSet();
    }

    @Override
    public Set executeMail(String filters, String text, int mailDelay, String girlId,  Set setIncremented) {
        return executeMail(filters, text, (List<String>) null, mailDelay, girlId, setIncremented);
    }

    @Override
    public Set executeMail(String filters, String text, List<String> ids, int mailDelay,  String girlId,  Set setIncremented) {
        return executeMail(filters, text, ids, null ,mailDelay, girlId, setIncremented);
    }

    @Override
    public Set executeMail(String filters, String text, JProgressBar progressBar,
                       long mailDelay, String girlId, Set<Connection> setIncremented){
        return executeMail(filters, text, null, progressBar ,mailDelay, girlId, setIncremented);
    }

    @Override
    public Set executeMail(String filters, String text, List<String> ids, JProgressBar progressBar,
                       long mailDelay, String girlId, Set<Connection> beforeConnections) {
        return new HashSet();
    }


}
