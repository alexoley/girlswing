package com.example.girlswing.services;

import com.example.girlswing.external.entities.Man;
import com.example.girlswing.external.repositories.ManRepository;
import com.example.girlswing.external.repositories.SearchRepository;
import com.example.girlswing.local.repositories.LocalManRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
@Slf4j
public class ManService {

    @Value("${site.api.link:}")
    private String siteApiLink;

    @Autowired
    RequestService requestService;

    @Autowired
    ResponseService responseService;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    LocalManRepository localManRepository;

    @Autowired
    ManRepository manRepository;

    @Transactional
    public List<Man> saveAll(Iterable<Man> iterable){
        return manRepository.saveAll(iterable);
    }

    public Queue<String> getMenIds() {
        Queue<String> menIds = new LinkedList<>();
        //TODO: Write that values for loop takes at the beginning of program
        final int limit = 70; //70
        final int pageMax = 80; //143
        int i = 1;
        //mainPage.setBarMax(limit*pageMax);

        for(int search=0; search<15; search++){
            //Search newSearch = searchRepository.save(
             //       Search.builder().);
        for (int page = 1; page < pageMax; page++) {
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

            HttpResponse response = requestService.basicRequest(json,
                    siteApiLink+"/account/search",
                    true, "post");
            if (responseService.getSecondsToWaitUntilRateLimitIncrease(response) == 0) {
                Optional<Object> jsonNodeObject = responseService.returnResponseBodyJsonNode(response);
                if (jsonNodeObject.isPresent()) {
                    JsonNode jsonNode = (JsonNode) jsonNodeObject.get();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode users = jsonNode.get("data").get("users");
                    try {

                        List<Man> menR = objectMapper.readValue(users.toString(), new TypeReference<List<Man>>() {
                        });
                        //menR.forEach(man -> man.setSearchId(newSearch));
                        saveAll(menR);
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.debug("Cannot serialize json to Man");
                    }
                    /*if (users.isArray()) {
                        for (JsonNode user : users) {
                            menIds.add(user.path("id").asText());
                        }
                    }*/
                    log.debug("Number of added men ids: " + i * limit);
                    i++;
                } else {
                    /// break if no response body
                    break;
                }
            } else {
                try {
                    page--;
                    Thread.sleep(70000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
        return menIds;
    }
}
