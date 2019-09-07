package com.example.girlswing.services;

import com.example.girlswing.exceptions.GirlswingContextInitializeException;
import com.example.girlswing.external.entities.Girl;
import com.example.girlswing.external.repositories.GirlRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class GirlService {

    @Autowired
    ResponseService responseService;

    @Autowired
    GirlRepository girlRepository;

    public List<Girl> getGirlsFromJsonNode(JsonNode jsonNode) throws GirlswingContextInitializeException {
        ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.
                        readValue(jsonNode.findPath("data").findPath("list").toString(),
                                new TypeReference<List<Girl>>() {});
            }
            catch(IOException e){
                throw new GirlswingContextInitializeException("Couldn't get girls from json");
            }
    }

    public List<Girl> saveAll(List<Girl> girls){
        return girlRepository.saveAll(girls);
    }

    public DefaultListModel girlInformationFromList(List<Girl> girls){
        DefaultListModel elements = new DefaultListModel();
        girls.forEach(girl -> elements.addElement(girl.getName()+", age:"+girl.getAge()+", id:"+girl.getId()));
        return elements;
    }

}
