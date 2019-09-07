package com.example.girlswing.services;

import com.example.girlswing.utils.CookiePrime;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    @Autowired
    CookiePrime cookiePrime;

    @Autowired
    RequestService requestService;

    Logger logger = LoggerFactory.getLogger(LoginService.class);

    public HttpResponse login(String username, String password){
        return login(username, password, "");
    }

    public HttpResponse login(String username, String password, String userAgent)  {
        //logger.debug("Make logIN");
        String json = new JSONObject()
                .put("email", "programmersndc@gmail.com")
                .put("password", "nv837hnch8c7c3hi47").toString();

        return requestService.basicRequest(json,
                "https://api.prime.date/auth/login",
                false, userAgent);


        /*StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(),
                        StandardCharsets.UTF_8))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            logger.debug(stringBuilder.toString());
        }
        catch(IOException e) {
            e.printStackTrace();
            logger.debug("Exception in login method");
            logger.debug(stringBuilder.toString());
        }*/

    }

    public HttpResponse logout(){
        return logout("");
    }

    public HttpResponse logout(String userAgent){
        //logger.debug("Make logOUT");
        return requestService.basicRequest("",
                "https://api.prime.date/auth/logout",
                true, userAgent);
    }
}
