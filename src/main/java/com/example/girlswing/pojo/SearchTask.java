package com.example.girlswing.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.*;

@Getter
@Setter
public class SearchTask extends Task {

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


}
