package com.example.girlswing.services;

import com.example.girlswing.UI.MainPage;
import com.example.girlswing.utils.CookieP;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.asynchttpclient.*;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.util.HttpConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class RequestService {

    @Value("${site.api.link:}")
    private String siteApiLink;

    @Autowired
    CookieP cookieP;

    @Autowired
    MainPage mainPage;

    @Autowired
    ResponseService responseService;


    public HttpResponse basicRequest(String bodyJson, String url, boolean setCookie, String method){
        return basicRequest(bodyJson, url, setCookie, "", method);
    }

    public HttpResponse basicRequest(String url, boolean setCookie, String method){
        return basicRequest("", url, setCookie, "", method);
    }

    public HttpResponse basicRequest(String bodyJson, String url, boolean setCookie, String userAgent, String method){
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            if(!userAgent.isEmpty() || !"".equals(userAgent)) {
                httpClient = HttpClientBuilder.create().setUserAgent(userAgent).build();
            }
            HttpUriRequest request=null;
            if("".equals(method)){
                log.error("Forget give method name parameter(for example GET, POST, PUT...) in method basicRequest");
            }
            if("get".equalsIgnoreCase(method)){
                request = new HttpGet(url);
            }
            if("post".equalsIgnoreCase(method) || "put".equalsIgnoreCase(method) || "delete".equalsIgnoreCase(method)){
                if("post".equalsIgnoreCase(method))
                    request = new HttpPost(url);
                if("put".equalsIgnoreCase(method))
                    request = new HttpPut(url);
                if("delete".equalsIgnoreCase(method))
                    request = new HttpDelete(url);
                if(!"".equals(bodyJson)) {
                    StringEntity entity = new StringEntity(bodyJson);
                    ((HttpEntityEnclosingRequestBase)request).setEntity(entity);
                }
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
            }

            if(setCookie) {
                request.setHeader("Cookie", "__cfduid=" + (String) cookieP.get("__cfduid") +
                        ";token=" + (String) cookieP.get("token") +
                        ";_csrf=" + (String) cookieP.get("_csrf"));
            }
            HttpResponse response = httpClient.execute(request);


            HashMap<String, HashMap<String,String>> headers = responseService.getHttpResponseHeaders(response);
            if(headers.get("Set-Cookie")!=null) {
                headers.get("Set-Cookie").
                        forEach((key, value) -> cookieP.put(key, value));
            }
            return response;

        } catch (Exception e) {
            log.debug("Exception in basic method");
            log.debug(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public HttpResponse send(String idTo, String text, String girlId){
        return send(idTo, text, "", girlId);
    }

    //@Async
    public HttpResponse send(String idTo, String text, String userAgent, String girlId){
        Date date = new Date();
        Long timeMilis = date.getTime();
        String json = new JSONObject()
                .put("idUserTo", idTo)
                .put("idMale", idTo)
                .put("idFemale", girlId)
                .put("content", new JSONObject().put("message", text).put("id", timeMilis.toString())).toString();

        return basicRequest(json,
                siteApiLink+"/operator/add-activity/message/"+idTo,
                true, userAgent, "post");
    }

    public HttpResponse findFemale(){
        return basicRequest(
                siteApiLink+"/operator/find-females",
                true, "post");
    }

    public HttpResponse getDictionaries(){
        return basicRequest(
                siteApiLink+"/system/dictionary?dictionary=1,2,3,4,5,6,7,8,9,10,11,13,14,15,16,17,18,19,21",
                true, "get");
    }

    public HttpResponse getConnections(int onliners, int bookmarked, int nomessages , int offset){
        return getConnections(onliners, bookmarked, nomessages ,  offset, "");
    }

    public HttpResponse getConnections(int onliners, int bookmarked, int nomessages , int offset, String cursor){
        JSONObject jsonObject = new JSONObject()
                .put("limit", 50)
                .put("offset", offset)
                .put("type", "operatorchat")
                .put("criteria", new JSONObject().put("filters",
                        new JSONObject().put("id_dialog", 0)
                                .put("id_female", "null")
                                .put("bookmarked", bookmarked)
                                .put("nomessages", nomessages)
                                .put("unanswered", 0)
                                .put("onliners", onliners)));
        if(!cursor.isEmpty()){
            jsonObject.put("cursor",cursor);
        }
        return basicRequest(jsonObject.toString(),
                siteApiLink+"/connections/get",
                true, "post");
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
                .setUrl("http://www.mysite.com")
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
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
