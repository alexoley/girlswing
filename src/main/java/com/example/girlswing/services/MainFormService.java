package com.example.girlswing.services;


import com.example.girlswing.UI.ChatSendForm;
import com.example.girlswing.UI.MailSendForm;
import com.example.girlswing.UI.MainForm;
import com.example.girlswing.pojo.Connection;
import com.example.girlswing.pojo.Task;
import com.example.girlswing.utils.CookieP;
import com.example.girlswing.utils.MasterDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MainFormService {

    @Value("${site.api.link:}")
    private String siteApiLink;

    @Autowired
    CookieP cookieP;

    @Autowired
    RequestService requestService;

    @Autowired
    MainForm mainForm;

    @Autowired
    LoginService loginService;

    @Autowired
    ResponseService responseService;

    @Autowired
    ManService manService;

    @Autowired
    MasterDataLoader masterDataLoader;

    @Autowired
    ChatSendForm chatSendForm;

    @Autowired
    MailSendForm mailSendForm;

    boolean suspended=false;
    boolean stopped=false;

    public void sendOld(String idTo, String text) {
        Date date = new Date();
        Long timeMilis = date.getTime();
        Request request = new Request.Builder().method("POST",
                RequestBody.create(MediaType.parse("application/json"),
                        "{\"idUserTo\":" + idTo + ",\"idMale\":36661269,\"idFemale\":20399816," +
                                "\"content\":{\"message\":\"" + text + "\",\"id\":" + timeMilis.toString() + "}}"))
                .url(HttpUrl.parse(siteApiLink + "/operator/add-activity/message/" + idTo))
                .build();
        //OkHttpClient httpClient = new OkHttpClient();
        try {
            Response response = new OkHttpClient().newBuilder()
                    .readTimeout(2, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .execute();
            if (response.isSuccessful()) {
                log.debug(response.body().string());
                //response.body().
            } else {
                log.info(response.body().string());
            }
        } catch (IOException e1) {

        }
    }


    public void sendToAll(String text) {
        long startTime = System.currentTimeMillis();
        Queue<String> menIds = manService.getMenIds();
        long endTime = System.currentTimeMillis();

        Long duration = (endTime - startTime);
        Long sec = duration / 1000;
        Long min = sec / 60;
        Long secAtTheEnd = sec % 60;

        log.debug("time of mining: " + min.toString() + "min " + secAtTheEnd.toString() + "sec");
        log.debug("list count:" + menIds.size());

        /*startTime = System.currentTimeMillis();
        //mainPage.setBar2Max(menIds.size());
        //menIds.forEach(manId->send(manId.toString(), text));
        //menIds.parallelStream().forEach(manId->send(manId.toString(), text));

        int i = 0;
        long finishTenTime;
        String manId;
        HttpResponse response;
        long secondsLimitReset;

        while(!menIds.isEmpty()) {
            manId = menIds.poll();
            response = send(manId, text);
            //secondsLimitReset = requestService.getSecondsToWaitUntilRateLimitIncrease(response);

            //if(secondsLimitReset==0 && requestService.isMessageResponseOk(response)){
            if(requestService.getXRateLimitRemaining(response)>0 && requestService.isMessageResponseOk(response)){
                i++;
                if (i % 10 == 0) {
                    finishTenTime = System.currentTimeMillis();
                    Long durationTen = (finishTenTime-startTime);
                    Long secTen = durationTen/1000;
                    Long minTen = secTen/60;
                    Long secAtTheEndTen = secTen % 60;
                    log.debug("Time of sending first "+i+" :"
                            +minTen.toString()+"min "
                            +secAtTheEndTen.toString()+"sec");
                }
            }
            else {
                menIds.add(manId);
                try {
                    Thread.sleep(3L * 1000L);
                }
                catch (InterruptedException e){
                    log.error("InterruptedException while thread wait for rate limit increase");
                }
                //requestService.isMessageResponseOk(loginService.logout());
                //requestService.isMessageResponseOk(loginService.login("", ""));
            }
        }
        endTime = System.currentTimeMillis();

        duration = (endTime - startTime);
        sec = duration/1000;
        min = sec/60;
        secAtTheEnd = sec % 60;

        log.debug("time of sending: "+min.toString()+"min "+secAtTheEnd.toString()+"sec");*/

    }

    public void sendToAllChatFromList(List<String> ids, String text, String girlId, JProgressBar progressBar) throws InvocationTargetException, InterruptedException {
        final long xRateLimitReset = ((long) masterDataLoader.get("X-Rate-Limit-Reset") + 1L) * 1000L;
        HttpResponse response;
        int i = 0;
        for (String id : ids) {

            /*synchronized (this) {
                while (suspended)
                    wait();
                if (stopped)
                    break;
            }*/
            i++;
            final int j = i;
            response = requestService.send(id, text, girlId);
            if (responseService.getSecondsToWaitUntilRateLimitIncrease(response) > 0) {
                try {
                    Thread.sleep(xRateLimitReset);
                } catch (InterruptedException e) {
                    log.error("Interrupted exception in send to all from list");
                    e.printStackTrace();
                }
            }
            if (!responseService.isMessageResponseOk(response)) {
                try {
                    Thread.sleep(xRateLimitReset);
                } catch (InterruptedException e) {
                    log.error("Interrupted exception in send to all from list");
                    e.printStackTrace();
                }
                requestService.send(id, text, girlId);
            }
            if (progressBar != null) {
                EventQueue.invokeAndWait(() -> progressBar.setValue(j));
            }
        }
    }

    public void sendToAllMailFromList(List<String> ids, String text, String girlId, JProgressBar progressBar) throws InvocationTargetException, InterruptedException {
        final long xRateLimitReset = ((long) masterDataLoader.get("X-Rate-Limit-Reset") + 1L) * 1000L;
        HttpResponse response;
        int i = 0;
        for (String id : ids) {
            i++;
            final int j = i;
            response = requestService.sendMail(id, text, girlId);
            if (responseService.getSecondsToWaitUntilRateLimitIncrease(response) > 0) {
                try {
                    Thread.sleep(xRateLimitReset);
                } catch (InterruptedException e) {
                    log.error("Interrupted exception in send to all from list");
                    e.printStackTrace();
                }
            }
            if (!responseService.isMessageResponseOk(response)) {
                try {
                    Thread.sleep(xRateLimitReset);
                } catch (InterruptedException e) {
                    log.error("Interrupted exception in send to all from list");
                    e.printStackTrace();
                }
                requestService.sendMail(id, text, girlId);
            }
            if (progressBar != null) {
                EventQueue.invokeAndWait(() -> progressBar.setValue(j));
            }
        }
    }

    public void countEntryOfMessage(String message) {
        String json = new JSONObject()
                .put("criteria", new JSONObject().put("filters", new JSONObject()
                        .put("id_dialog", 0)
                        .put("id_female", "null")
                        .put("bookmarked", 0)
                        .put("nomessages", 0)
                        .put("unanswered", 0)
                        .put("onliners", 0)))
                .put("limit", 10000)
                .put("offset", 0)
                .put("type", "operatorchat").toString();

        String responseText = responseService.returnResponseBodyString(requestService.basicRequest(json,
                siteApiLink + "/connections/get",
                true, "post"));

        try {
            int i = 0;
            JsonNode jsonNode = new ObjectMapper().readTree(responseText);
            JsonNode connections = jsonNode.findPath("data").findPath("connections");
            if (connections.isArray()) {
                for (JsonNode conn : connections) {
                    if (message.equals(conn.findPath("chat").findPath("message").asText())) {
                        i++;
                    }
                }
            }
            log.debug("Entry of message in chat: " + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChatMessagesToAllTasks(int chatDelay, String girlId, List<JButton> buttons) throws InvocationTargetException, InterruptedException {
        Set setIncremented = new HashSet();
        Set setForEachIteration;
        for (List taskList : chatSendForm.getTaskList()) {
            setForEachIteration = ((Task) taskList.get(0)).execute(((Task) taskList.get(0)).getFilters(),
                    ((Task) taskList.get(0)).getText(), ((Task) taskList.get(0)).getProgressBar(), chatDelay,
                    girlId, setIncremented);
            List<String> ids = new LinkedList<>();
            for (Object conn : setForEachIteration) {
                ids.add(((Connection) conn).getIdMale());
            }

            /*TODO: Make lambda that can cast and collect man ids to list*/
            /*List<String> ids = setForEachIteration.stream()
             *//* .filter(conn -> conn instanceof Connection)
                    .map(Connection.class::cast)*//*
                    //.filter(conn -> Objects.nonNull(conn.getIdMale()))
                    .map(conn -> ((Connection)conn).getIdMale()).collect(Collectors.toList());*/
            Iterator it = taskList.iterator();
            it.next();
            while (it.hasNext()) {
                Object task = it.next();
                if (!ids.isEmpty()) {
                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                    Runnable runnableTask = () -> {
                        try {
                            ((Task) task).execute(((Task) task).getFilters(),
                                    ((Task) task).getText(), ids, ((Task) task).getProgressBar(), chatDelay,
                                    girlId, setIncremented);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                    ScheduledFuture<?> future = executor.schedule(runnableTask, chatDelay * 60L, TimeUnit.SECONDS);
                    try {
                        executor.shutdown();
                        executor.awaitTermination(1, TimeUnit.HOURS);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (((Task) task).getProgressBar() != null) {
                        EventQueue.invokeAndWait(() ->
                        {
                            ((Task) task).getProgressBar().setMaximum(1);
                            ((Task) task).getProgressBar().setValue(1);
                        });
                    }
                }
            }
            setIncremented.addAll(setForEachIteration);
        }
        buttons.forEach(button -> button.setEnabled(true));
    }

    public void sendMailMessagesToAllTasks(int mailDelay, String girlId, List<JButton> buttons) throws InvocationTargetException, InterruptedException {
        Set setIncremented = new HashSet();
        Set setForEachIteration;
        for (List taskList : mailSendForm.getTaskList()) {
            setForEachIteration = ((Task) taskList.get(0)).executeMail(((Task) taskList.get(0)).getFilters(),
                    ((Task) taskList.get(0)).getText(), ((Task) taskList.get(0)).getProgressBar(), mailDelay,
                    girlId, setIncremented);
            List<String> ids = new LinkedList<>();
            for (Object conn : setForEachIteration) {
                ids.add(((Connection) conn).getIdMale());
            }

            /*TODO: Make lambda that can cast and collect man ids to list*/
            /*List<String> ids = setForEachIteration.stream()
             *//* .filter(conn -> conn instanceof Connection)
                    .map(Connection.class::cast)*//*
                    //.filter(conn -> Objects.nonNull(conn.getIdMale()))
                    .map(conn -> ((Connection)conn).getIdMale()).collect(Collectors.toList());*/
            Iterator it = taskList.iterator();
            it.next();
            while (it.hasNext()) {
                Object task = it.next();
                if (!ids.isEmpty()) {
                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                    Runnable runnableTask = () -> {
                        try {
                            ((Task) task).executeMail(((Task) task).getFilters(),
                                    ((Task) task).getText(), ids, ((Task) task).getProgressBar(), mailDelay,
                                    girlId, setIncremented);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                    ScheduledFuture<?> future = executor.schedule(runnableTask, mailDelay * 60L, TimeUnit.SECONDS);
                    try {
                        executor.shutdown();
                        executor.awaitTermination(1, TimeUnit.HOURS);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (((Task) task).getProgressBar() != null) {
                        EventQueue.invokeAndWait(() ->
                        {
                            ((Task) task).getProgressBar().setMaximum(1);
                            ((Task) task).getProgressBar().setValue(1);
                        });
                    }
                }
            }
            setIncremented.addAll(setForEachIteration);
        }
        buttons.forEach(button -> button.setEnabled(true));
    }
}
