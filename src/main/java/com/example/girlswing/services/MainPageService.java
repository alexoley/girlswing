package com.example.girlswing.services;


import com.example.girlswing.UI.MainPage;
import com.example.girlswing.utils.CookiePrime;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MainPageService {

    Logger logger = LoggerFactory.getLogger(MainPageService.class);

    @Autowired
    CookiePrime cookiePrime;

    @Autowired
    RequestService requestService;

    @Autowired
    MainPage mainPage;

    @Autowired
    LoginService loginService;

    public void sendOld(String idTo, String text){
        Date date = new Date();
        Long timeMilis = date.getTime();
        Request request = new Request.Builder().method("POST",
                RequestBody.create(MediaType.parse("application/json"),
                        "{\"idUserTo\":"+idTo+",\"idMale\":36661269,\"idFemale\":20399816," +
                                "\"content\":{\"message\":\""+text+"\",\"id\":"+timeMilis.toString()+"}}" ))
                .url(HttpUrl.parse("https://api.prime.date/operator/add-activity/message/"+idTo))
                .build();
        //OkHttpClient httpClient = new OkHttpClient();
        try
        {
            Response response = new OkHttpClient().newBuilder()
                    .readTimeout(2, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .execute();
            if(response.isSuccessful())
            {
                logger.debug(response.body().string());
                //response.body().
            }
            else
            {
                logger.info(response.body().string());
            }
        }
        catch (IOException e1)
        {

        }
    }

    public HttpResponse send(String idTo, String text){
        return send(idTo, text, "");
    }

    //@Async
    public HttpResponse send(String idTo, String text, String userAgent){
        Date date = new Date();
        Long timeMilis = date.getTime();
        String json = new JSONObject()
                .put("idUserTo", idTo)
                .put("idMale", idTo)
                .put("idFemale", "20399816")
                .put("content", new JSONObject().put("message", text).put("id", timeMilis.toString())).toString();

        return requestService.basicRequest(json,
                "https://api.prime.date/operator/add-activity/message/"+idTo,
                true, userAgent);
    }

    public void sendToAll(String text){
        long startTime = System.currentTimeMillis();
        Queue<String> menIds = requestService.getMenIds();
        long endTime = System.currentTimeMillis();

        Long duration = (endTime - startTime);
        Long sec = duration/1000;
        Long min = sec/60;
        Long secAtTheEnd = sec % 60;

        logger.debug("time of mining: "+min.toString()+"min "+secAtTheEnd.toString()+"sec");
        logger.debug("list count:"+ menIds.size());

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
                    logger.debug("Time of sending first "+i+" :"
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
                    logger.error("InterruptedException while thread wait for rate limit increase");
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

        logger.debug("time of sending: "+min.toString()+"min "+secAtTheEnd.toString()+"sec");*/

    }

    public void countEntryOfMessage(String message){
        String json = new JSONObject()
                .put("criteria", new JSONObject().put("filters", new JSONObject()
                        .put("id_dialog", 0)
                        .put("id_female","null")
                        .put("bookmarked", 0)
                        .put("nomessages", 0)
                        .put("unanswered", 0)
                        .put("onliners", 0)))
                .put("limit", 10000)
                .put("offset", 0)
                .put("type", "operatorchat").toString();

        String responseText = requestService.returnResponseBodyString(requestService.basicRequest(json,
                "https://api.prime.date/connections/get",
                true));

        try {
            int i=0;
            JsonNode jsonNode = new ObjectMapper().readTree(responseText);
            JsonNode connections = jsonNode.findPath("data").findPath("connections");
            if (connections.isArray()) {
                for (JsonNode conn : connections) {
                    if(message.equals(conn.findPath("chat").findPath("message").asText())){
                        i++;
                    }
                }
            }
            logger.debug("Entry of message in chat: "+i);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }




}
