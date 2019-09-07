package com.example.girlswing.services;

import com.example.girlswing.exceptions.GirlswingContextInitializeException;
import com.example.girlswing.external.entities.Operator;
import com.example.girlswing.external.repositories.OperatorRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class OperatorService {

    @Autowired
    ResponseService responseService;

    @Autowired
    OperatorRepository operatorRepository;

    public Operator getOperatorFromLoginResponse(JsonNode jsonNode) throws GirlswingContextInitializeException {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.
                        readValue(jsonNode.findPath("data").findPath("profile").toString(), Operator.class);
            }
            catch(IOException e){
                throw new GirlswingContextInitializeException("Couldn't get operator from json. "
                        + e.getMessage());
            }
    }

    public Operator save(Operator operator){
        Optional<Operator> operator1 = operatorRepository.findById(operator.getId());
        if(operator1.isPresent()){
            return operator1.get();
        }
        return operatorRepository.save(operator);
    }
}
