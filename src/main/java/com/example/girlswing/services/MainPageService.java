package com.example.girlswing.services;


import com.example.girlswing.UI.MainPage;
import com.example.girlswing.utils.CookieP;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MainPageService {

    @Value("${site.api.link:}")
    private String siteApiLink;

    @Autowired
    CookieP cookieP;

    @Autowired
    RequestService requestService;

    @Autowired
    MainPage mainPage;

    @Autowired
    LoginService loginService;

    @Autowired
    ResponseService responseService;

    @Autowired
    ManService manService;

    public void sendOld(String idTo, String text){
        Date date = new Date();
        Long timeMilis = date.getTime();
        Request request = new Request.Builder().method("POST",
                RequestBody.create(MediaType.parse("application/json"),
                        "{\"idUserTo\":"+idTo+",\"idMale\":36661269,\"idFemale\":20399816," +
                                "\"content\":{\"message\":\""+text+"\",\"id\":"+timeMilis.toString()+"}}" ))
                .url(HttpUrl.parse(siteApiLink+"/operator/add-activity/message/"+idTo))
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
                log.debug(response.body().string());
                //response.body().
            }
            else
            {
                log.info(response.body().string());
            }
        }
        catch (IOException e1)
        {

        }
    }


    public void sendToAll(String text){
        long startTime = System.currentTimeMillis();
        Queue<String> menIds = manService.getMenIds();
        long endTime = System.currentTimeMillis();

        Long duration = (endTime - startTime);
        Long sec = duration/1000;
        Long min = sec/60;
        Long secAtTheEnd = sec % 60;

        log.debug("time of mining: "+min.toString()+"min "+secAtTheEnd.toString()+"sec");
        log.debug("list count:"+ menIds.size());

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

        String responseText = responseService.returnResponseBodyString(requestService.basicRequest(json,
                siteApiLink+"/connections/get",
                true, "post"));

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
            log.debug("Entry of message in chat: "+i);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }




}
