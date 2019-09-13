package com.example.girlswing.services;

import com.example.girlswing.UI.MainForm;
import com.example.girlswing.exceptions.GirlswingContextInitializeException;
import com.example.girlswing.exceptions.LoginException;
import com.example.girlswing.external.entities.Girl;
import com.example.girlswing.external.entities.Operator;
import com.example.girlswing.utils.CookieP;
import com.example.girlswing.utils.MasterDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class LoginService {

    @Value("${site.api.link:}")
    private String siteApiLink;

    @Autowired
    CookieP cookieP;

    @Autowired
    RequestService requestService;

    @Autowired
    ResponseService responseService;

    @Autowired
    OperatorService operatorService;

    @Autowired
    GirlService girlService;

    @Autowired
    MasterDataLoader masterDataLoader;

    @Autowired
    MainForm mainForm;


    public void applicationLogin(String email, String password)
            throws LoginException, GirlswingContextInitializeException {
        HttpResponse httpResponse = login(email, password);
        Optional<Object> jsonNodeObject =  responseService.returnResponseBodyJsonNode(httpResponse);
        if(!jsonNodeObject.isPresent()){
            throw new LoginException("Empty response body");
        }
        JsonNode loginNode = (JsonNode) jsonNodeObject.get();
        if(!"true".equals(loginNode.findPath("data").findPath("result").asText())){
            throw new LoginException("Response error body");
        }
        //get operator information from login response
        Operator operator = operatorService.getOperatorFromLoginResponse(loginNode);
        operator.setEmail(email);
        masterDataLoader.put("operator",operator);
        operatorService.save((Operator) masterDataLoader.get("operator"));
        StringBuffer space = new StringBuffer(140);
        for (int i = 0; i < 140; i++){
            space.append(" ");
        }
        mainForm.setTitle("Rocketgirls"+space.toString()
                +operator.getId()+" "+operator.getFirstname()+" "+operator.getLastname());

        //request to get girls info
        HttpResponse findFemaleResponse = requestService.findFemale();
        jsonNodeObject = responseService.returnResponseBodyJsonNode(findFemaleResponse);
        if(!jsonNodeObject.isPresent()){
            throw new LoginException("Empty response body");
        }
        List<Girl> girls = girlService.getGirlsFromJsonNode((JsonNode) jsonNodeObject.get());
        girls.forEach(a -> a.setOperatorId(operator));
        //save girls to masterData
        masterDataLoader.put("girls", girls);
        //save girls to remote db
        girlService.saveAll((List<Girl>)masterDataLoader.get("girls"));

        //save x-rate-limit
        masterDataLoader.put("X-Rate-Limit-Reset", responseService.getXRateLimitReset(findFemaleResponse));

        //set girls to left JList
        mainForm.setElementsToListOfGirls(girlService.girlInformationFromList(girls));
    }

    public HttpResponse login(String email, String password){
        return login(email, password, "");
    }



    public HttpResponse login(String email, String password, String userAgent)  {
        //logger.debug("Make logIN");
        String json = new JSONObject().put("email", email)
                .put("password", password).toString();

        return requestService.basicRequest(json,
                siteApiLink+"/auth/login",
                false, userAgent,"post");


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
                siteApiLink+"/auth/logout",
                true, userAgent, "post");
    }
}
