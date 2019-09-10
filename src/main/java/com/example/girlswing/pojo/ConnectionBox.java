package com.example.girlswing.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionBox<String, Connection>{

    String cursor;
    List<Connection> connections;
        /*ConnectionBox(String cursor, List<Connection> connections){
            this.connections=connections;
            this.cursor=cursor;
        }*/
}
