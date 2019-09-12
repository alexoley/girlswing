package com.example.girlswing.services;

import com.example.girlswing.exceptions.ConnectionEmptyException;
import com.example.girlswing.pojo.Connection;
import com.example.girlswing.pojo.ConnectionBox;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ConnectionService {

    @Autowired
    ResponseService responseService;

    @Autowired
    RequestService requestService;



    public ConnectionBox getConnectionBox(int onliners, int bookmarked, int nomessages , int offset) throws ConnectionEmptyException {
        return getConnectionBox(onliners, bookmarked, nomessages, offset,"");
    }
    public ConnectionBox getConnectionBox(int onliners, int bookmarked, int nomessages , int offset, String cursor) throws ConnectionEmptyException {
        String cursorNew = null;
        List<Connection> connectionList=null;
        Optional jsonNode = cursor.isEmpty()?responseService.returnResponseBodyJsonNode(
                requestService.getConnections(onliners, bookmarked, nomessages, offset))
                :responseService.returnResponseBodyJsonNode(
                requestService.getConnections(onliners, bookmarked, nomessages, offset, cursor));
        if (jsonNode.isPresent()) {
            JsonNode connections = ((JsonNode) jsonNode.get()).findPath("data").findPath("connections");
            cursorNew = ((JsonNode) jsonNode.get()).findPath("data").findPath("cursor").toString();
            if (connections.isArray()) {
                if (connections.isEmpty(null)) throw new ConnectionEmptyException();
                connectionList = StreamSupport.stream(connections.spliterator(), false)
                        //.filter(conn -> "active".equals(conn.findPath("state").toString().replace("\"","")))
                        .map(conn -> Connection.builder()
                                .id(conn.findPath("id").toString())
                                .state(conn.findPath("state").toString())
                                .idMale(conn.findPath("idMale").toString())
                                .isFemaleAbused(conn.findPath("isFemaleAbused").isBoolean() ?
                                        Boolean.valueOf(conn.findPath("isFemaleAbused").toString()) : false)
                                .id_user_from(conn.findPath("chat").findPath("id_user_from").toString())
                                .id_user_to(conn.findPath("chat").findPath("id_user_to").toString())
                                .type(conn.findPath("chat").findPath("type").toString())
                                .message(conn.findPath("chat").findPath("message").toString())
                                .build()).collect(Collectors.toList());

            }
        }

        return new ConnectionBox(cursorNew, connectionList);
    }

}
