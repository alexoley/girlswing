package com.example.girlswing.pojo;

import com.example.girlswing.exceptions.ConnectionEmptyException;
import com.example.girlswing.services.ConnectionService;
import com.example.girlswing.services.MainFormService;
import com.example.girlswing.utils.SpringUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class DialogTask extends Task {

    public DialogTask(long id, String name, String filters, String text) {
        super(id, name, filters, text);
    }

    public DialogTask(long id, String name, String filters, String text, JProgressBar progressBar) {
        super(id, name, filters, text, progressBar);
    }

    public DialogTask(long id, String name, String filters, String text, LinkedList<String> ids,
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
                       long delay, String girlId, Set<Connection> beforeConnections){

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
                EventQueue.invokeLater(() -> {
                    progressBar.setMinimum(0);
                    progressBar.setMaximum(max-1);
                });
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
                oneRun.removeAll(beforeConnections);

                //set new value to progress bar
                final int j = i;
                if (progressBar != null) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            progressBar.setValue(j);
                        }
                    });
                }

                mainFormService.sendToAllFromList(oneRun.stream().filter(conn -> Objects.nonNull(conn.getIdMale()))
                                .map(conn -> conn.getIdMale()).collect(Collectors.toList()),
                        text, girlId, null);
                uniqueConnections.addAll(oneRun); //add this
                oneRun.clear();
            }
            if (progressBar != null) {
                EventQueue.invokeLater(() -> progressBar.setValue(progressBar.getMaximum()));
            }

            log.debug(uniqueConnections.size()+"");
            uniqueConnections.forEach(conn -> log.debug(conn.toString()));
            return uniqueConnections;
        }
        else{
            if (progressBar != null) {
                EventQueue.invokeLater(() -> {
                    progressBar.setMinimum(0);
                    progressBar.setMaximum(ids.size());
                });
            }
            mainFormService.sendToAllFromList(ids,text, girlId, progressBar);
            if (progressBar != null) {
                EventQueue.invokeLater(() -> progressBar.setValue(progressBar.getMaximum()));
            }
            return uniqueConnections;
        }
    }
}
