package com.example.girlswing.pojo;

import com.example.girlswing.exceptions.ConnectionEmptyException;
import com.example.girlswing.services.ConnectionService;
import com.example.girlswing.services.MainFormService;
import com.example.girlswing.utils.SpringUtils;
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

    public DialogTask(String name, String filters, String text, LinkedList<String> ids,
                      Calendar time, Task child, JProgressBar progressBar) {
        super(filters, text, ids, time, child, progressBar);
    }
    public void execute(String text, long delay, String girlId, String filters){
        execute(text, delay, girlId, filters, null);
    }

    public void execute(String text, long delay, String girlId, String filters, JProgressBar progressBar){
        ConnectionService connectionService = SpringUtils.getBean(ConnectionService.class);
        MainFormService mainFormService = SpringUtils.getBean(MainFormService.class);
        //get filters from string
        String[] filtersMass = filters.split(",",0);
        int onliners=0,bookmarked=0,nomessages=0;
        for(String str:filtersMass){
            if(str.equals("onliners")){
                onliners=1;
            }
            if(str.equals("bookmarked")){
                bookmarked=1;
            }
            if(str.equals("nomessages")){
                nomessages=1;
            }
        }
        final int max=5;
        if(progressBar!=null){
            progressBar.setMinimum(1);
            progressBar.setMaximum(max);
        }
        ConnectionBox cb;
        Set<Connection> uniqueConnections = new HashSet<>();
        Set<Connection> oneRun = new HashSet<>();
        for (int i = 0; i < max; i++) {
            try{
                cb=connectionService.getConnectionBox(onliners,bookmarked,nomessages,i);
                //uniqueConnections.addAll(cb.getConnections()); //delete this
                oneRun.addAll(cb.getConnections());}
            catch(ConnectionEmptyException e){
                break;
            }
            do{
                try {
                    cb = connectionService.getConnectionBox(onliners, bookmarked, nomessages, i, (String) cb.getCursor());
                    //uniqueConnections.addAll(cb.getConnections()); //delete this
                    oneRun.addAll(cb.getConnections());
                }
                catch(ConnectionEmptyException e){
                    break;
                }
            }
            while(true);
            oneRun.removeAll(uniqueConnections);
            mainFormService.sendToAllFromList(oneRun.stream().filter(conn -> Objects.nonNull(conn.getIdMale()))
                            .map(conn -> conn.getIdMale()).collect(Collectors.toList()),
                    text, girlId);
            //add this
            uniqueConnections.addAll(oneRun);
            oneRun.clear();
            if(progressBar!=null){
                progressBar.setValue(i);
            }
        }
        log.debug(uniqueConnections.size()+"");
        uniqueConnections.forEach(conn -> log.debug(conn.toString()));
        if(this.child!=null){
            this.child.setIds(uniqueConnections.stream().filter(conn->conn.getIdMale()!=null)
                    .map(conn -> conn.getIdMale()).collect(Collectors.toList()));
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable task = () -> this.child.execute(this.child.text, delay, filters, girlId);
            ScheduledFuture<?> future = executor.schedule(task, delay*60L, TimeUnit.SECONDS);
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
