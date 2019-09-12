package com.example.girlswing.pojo;

import com.example.girlswing.exceptions.ConnectionEmptyException;
import com.example.girlswing.services.ConnectionService;
import com.example.girlswing.services.MainFormService;
import com.example.girlswing.utils.SpringUtils;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class DialogTask extends Task {

    public DialogTask(String filters, String text) {
        super(filters, text);
    }

    public DialogTask(String filters, String text, JProgressBar progressBar) {
        super(filters, text, progressBar);
    }

    public DialogTask(String filters, String text, LinkedList<String> ids,
                      Calendar time, JProgressBar progressBar) {
        super(filters, text, ids, time, progressBar);
    }

    @Override
    public Set execute(String text, long delay, String girlId, String filters){
        return execute(text, delay, girlId, filters, null, null);
    }

    @Override
    public Set execute(String text, long delay, String girlId, String filters, JProgressBar progressBar) {
        return execute(text, delay, girlId, filters, progressBar, null);
    }

    @Override
    public Set execute(String text, long delay, String girlId, String filters,
                         List<String> ids){
        return execute(text, delay, girlId, filters, null, ids);
    }

    public Set execute(String text, long delay, String girlId, String filters,
                        JProgressBar progressBar, List<String> ids){

        /*TODO: Come up with, how to delete this SpringUtils calls*/
        ConnectionService connectionService = SpringUtils.getBean(ConnectionService.class);
        MainFormService mainFormService = SpringUtils.getBean(MainFormService.class);


        Set<Connection> uniqueConnections = new HashSet<>();

        if(ids==null || ids.isEmpty()) {
            //get filters from string
            String[] filtersMass = filters.split(",", 0);
            int onliners = 0, bookmarked = 0, nomessages = 0;
            for (String str : filtersMass) {
                if (str.equals("onliners")) {
                    onliners = 1;
                }
                if (str.equals("bookmarked")) {
                    bookmarked = 1;
                }
                if (str.equals("nomessages")) {
                    nomessages = 1;
                }
            }
            final int max = 5;
            if (progressBar != null) {
                progressBar.setMinimum(1);
                progressBar.setMaximum(max);
            }
            ConnectionBox cb;
            Set<Connection> oneRun = new HashSet<>();
            for (int i = 0; i < max; i++) {
                try {
                    cb = connectionService.getConnectionBox(onliners, bookmarked, nomessages, i);
                    //uniqueConnections.addAll(cb.getConnections()); //delete this
                    oneRun.addAll(cb.getConnections());
                } catch (ConnectionEmptyException e) {
                    break;
                }
                do {
                    try {
                        cb = connectionService.getConnectionBox(onliners, bookmarked, nomessages, i, (String) cb.getCursor());
                        //uniqueConnections.addAll(cb.getConnections()); //delete this
                        oneRun.addAll(cb.getConnections());
                    } catch (ConnectionEmptyException e) {
                        break;
                    }
                }
                while (true);
                oneRun.removeAll(uniqueConnections);
                mainFormService.sendToAllFromList(oneRun.stream().filter(conn -> Objects.nonNull(conn.getIdMale()))
                                .map(conn -> conn.getIdMale()).collect(Collectors.toList()),
                        text, girlId);
                uniqueConnections.addAll(oneRun); //add this
                oneRun.clear();
                if (progressBar != null) {
                    progressBar.setValue(i);
                }
            }

            log.debug(uniqueConnections.size()+"");
            uniqueConnections.forEach(conn -> log.debug(conn.toString()));
            return uniqueConnections;
        }
        else{
            mainFormService.sendToAllFromList(ids,text, girlId);
            return uniqueConnections;
        }
    }
}
