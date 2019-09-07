package com.example.girlswing.services;

import com.example.girlswing.UI.MainPage;
import com.example.girlswing.external.entities.Man;
import com.example.girlswing.external.repositories.ManRepository;
import com.example.girlswing.local.entities.LocalMan;
import com.example.girlswing.local.repositories.LocalManRepository;
import com.example.girlswing.utils.CookiePrime;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class RequestService {

    Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    CookiePrime cookiePrime;

    @Autowired
    MainPage mainPage;

    @Autowired
    LocalManRepository localManRepository;

    @Autowired
    ManRepository manRepository;

    public HttpResponse basicRequest(String bodyJson, String url, boolean setCookie){
        return basicRequest(bodyJson, url, setCookie, "");
    }

    public HttpResponse basicRequest(String bodyJson, String url, boolean setCookie, String userAgent){
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            if(!userAgent.isEmpty() || userAgent!="") {
                httpClient = HttpClientBuilder.create().setUserAgent(userAgent).build();
            }
            HttpPost request = new HttpPost(url);
            StringEntity entity = new StringEntity(bodyJson);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            if(setCookie) {
                request.setHeader("Cookie", "__cfduid=" + (String) cookiePrime.get("__cfduid") +
                        ";token=" + (String) cookiePrime.get("token") +
                        ";_csrf=" + (String) cookiePrime.get("_csrf"));
            }
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);



            getHttpResponseHeaders(response).get("Set-Cookie").forEach((key,value) -> cookiePrime.put(key, value));
            return response;

        } catch (Exception e) {
            logger.debug("Exception in basic method");
            logger.debug(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }




    public Queue<String> getMenIds(){
        Queue<String> menIds = new LinkedList<>();
        //TODO: Write that values for loop takes at the beginning of program
        final int limit=70; //70
        final int pageMax=81; //143
        int i=1;
        mainPage.setBarMax(limit*pageMax);
        for(int page=80; page<pageMax; page++){
            //mainPage.updateBar(page*limit);
            String json = new JSONObject()
                    .put("filters", new JSONObject()
                            .put("ageFrom", "null")
                            .put("ageTo", "null")
                            .put("countries", new JSONArray())
                            .put("withPhoto", false)
                            .put("moreChildren", false))
                    .put("limit", limit)
                    .put("page", page).toString();

            HttpResponse response = basicRequest(json,
                    "https://api.prime.date/account/search",
                    true,"");
            if(getSecondsToWaitUntilRateLimitIncrease(response)==0){
                Optional<Object>  jsonNodeObject = returnResponseBodyJsonNode(response);
                if(jsonNodeObject.isPresent()) {
                    JsonNode jsonNode = (JsonNode) jsonNodeObject.get();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode users = jsonNode.get("data").get("users");
                    try {
                        List<LocalMan> men = objectMapper.readValue(users.toString(), new TypeReference<List<LocalMan>>() {});
                        localManRepository.saveAll(men);
                        List<Man> menR = objectMapper.readValue(users.toString(), new TypeReference<List<Man>>() {});
                        manRepository.saveAll(menR);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                        logger.debug("Cannot serialize json to Man");
                    }
                    if (users.isArray()) {
                        for (JsonNode user : users) {
                            menIds.add(user.path("id").asText());
                        }
                    }
                    logger.debug("Number of added men ids: " + i * limit);
                    i++;
                }
                else{
                    /// break if no response body
                    break;
                }
            }
            else {
                try {
                    page--;
                    Thread.sleep(70000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return menIds;
    }

    public String returnResponseBodyString(HttpResponse response){
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(),
                        "UTF-8"))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch(Exception e) {
            logger.debug("Exception in getMenIds buffer reader");
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public Optional<Object> returnResponseBodyJsonNode(HttpResponse response) {
        try {
            return Optional.of(new ObjectMapper().readTree(returnResponseBodyString(response)));
        }
        catch (IOException e){
            logger.error("Message body not in json type");
            logger.debug(returnResponseBodyString(response));
            return Optional.of(null);
        }
    }


    public boolean isMessageResponseOk(HttpResponse response){
        Optional<Object>  jsonNodeObject =  returnResponseBodyJsonNode(response);
        if(jsonNodeObject.isPresent()) {
            JsonNode jsonNode = (JsonNode) jsonNodeObject.get();
            JsonNode responseTextMessage = jsonNode.findPath("data").findPath("type");
            JsonNode responseTextLogout = jsonNode.findPath("data").findPath("status");
            JsonNode responseTextLogin = jsonNode.findPath("data").findPath("result");
            if ("success".equals(responseTextMessage.asText()) ||
                    "true".equals(responseTextLogout.asText()) ||
                    "true".equals(responseTextLogin.asText())) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, HashMap<String,String>> getHttpResponseHeaders(HttpResponse response)
    {
        Header[] headers = response.getAllHeaders();
        HashMap<String, HashMap<String,String>> headersMap = new HashMap<>();
        for (Header a: headers) {
            if(headersMap.get(a.getName())!=null) {
                headersMap.get(a.getName()).put(a.getElements()[0].getName(),
                        a.getElements()[0].getValue());
            }
            else {
                HashMap<String, String> elements = new HashMap();
                elements.put(a.getElements()[0].getName(),
                        a.getElements()[0].getValue());
                headersMap.put(a.getName(),elements);
            }
        }
        return headersMap;
    };

    /***
     * Return 0 if limit hasn't finished
     * Return above 0 if limit has finished. And number is seconds to wait.
     * @param response
     * @return
     */
    public long getSecondsToWaitUntilRateLimitIncrease(HttpResponse response){
        HashMap<String, HashMap<String,String>> headers = getHttpResponseHeaders(response);
        String xRateLimitRemaining=null;
        String xRateLimitReset=null;
        for ( String key : headers.get("X-Rate-Limit-Remaining").keySet() ) {
            xRateLimitRemaining = key;
        }
        for ( String key : headers.get("X-Rate-Limit-Reset").keySet() ) {
            xRateLimitReset = key;
        }
        if(headers.get("X-Rate-Limit-Remaining")!=null &&
                xRateLimitRemaining != null &&
                xRateLimitReset != null &&
                Long.parseLong(xRateLimitRemaining)==0){
            return Long.parseLong(xRateLimitReset);
        }
        return 0;
    }

    public long getXRateLimitRemaining(HttpResponse response){
        HashMap<String, HashMap<String,String>> headers = getHttpResponseHeaders(response);
        String xRateLimitRemaining=null;
        for ( String key : headers.get("X-Rate-Limit-Remaining").keySet() ) {
            xRateLimitRemaining=key;
        }
        return Long.parseLong(xRateLimitRemaining);
    }

    public long getXRateLimitReset(HttpResponse response){
        HashMap<String, HashMap<String,String>> headers = getHttpResponseHeaders(response);
        String xRateLimitReset=null;
        for ( String key : headers.get("X-Rate-Limit-Reset").keySet() ) {
            xRateLimitReset=key;
        }
        return Long.parseLong(xRateLimitReset);
    }

    // TODO: finished this method. Play with Future<Response>.
    public Response asyncRequest(String bodyJson, String url, boolean setCookie) throws InterruptedException,
            ExecutionException {

        //DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
        //        .setConnectTimeout(10);
        AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
        /*List<io.netty.handler.codec.http.cookie.Cookie> cookieList =
                new ArrayList<io.netty.handler.codec.http.cookie.Cookie>() ;
        if(setCookie) {
            DefaultCookie c1 = new DefaultCookie("__cfduid", "");
            cookieList.add(new DefaultCookie("__cfduid", ""));
            cookieList.add(new DefaultCookie("__cfduid", ""));
            cookieList.add(new DefaultCookie("__cfduid", ""));
        }*/
        Request getRequest = new RequestBuilder(HttpConstants.Methods.POST)
                .setUrl("http://www.baeldung.com")
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .setHeader("Origin", "https://prime.date")
                //.setCookies(cookieList)
                .setBody(bodyJson)
                .build();
        Future<Response> responseFuture =  asyncHttpClient.executeRequest(getRequest);
        while(!responseFuture.isDone()) {
            Thread.sleep(300);
        }
        return responseFuture.get();

    }

}
