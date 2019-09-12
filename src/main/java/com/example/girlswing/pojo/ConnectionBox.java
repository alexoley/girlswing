package com.example.girlswing.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionBox<String, Connection>{

    private String cursor;
    private List<Connection> connections;
}
