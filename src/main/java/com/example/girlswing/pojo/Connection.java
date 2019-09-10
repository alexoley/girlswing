package com.example.girlswing.pojo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Connection {
    public String id;
    public String message;
    public String id_user_from;
    public String id_user_to;
    public String type;
    public boolean isFemaleAbused;
    public String state;

}
