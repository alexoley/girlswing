package com.example.girlswing.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.*;

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

    @Override
    public Set execute(String text, long delay, String girlId, String filters,
                       JProgressBar progressBar, List<String> ids, Set<Connection> beforeConnections) {
        return new HashSet();
    }

    @Override
    public Set execute(String text, int chatDelay, String girlId, String filters, Set setIncremented) {
        return new HashSet();
    }

    @Override
    public Set execute(String text, int chatDelay, String filters, String girlId, List<String> ids, Set setIncremented) {
        return new HashSet();
    }


}
