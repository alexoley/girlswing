package com.example.girlswing.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
public class ResponseService {

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
            log.debug("Exception in returnResponseBodyString method buffer reader");
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public Optional<Object> returnResponseBodyJsonNode(HttpResponse response) {
        try {
            JsonNode jn = new ObjectMapper().readTree(returnResponseBodyString(response));
            return Optional.of(jn);
        }
        catch (IOException e){
            log.error("Message body not in json type");
            log.debug(returnResponseBodyString(response));
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
}
