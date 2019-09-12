package com.example.girlswing.pojo;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
public class Connection {
    private String id;
    private String message;
    private String id_user_from;
    private String id_user_to;
    private String type;
    private boolean isFemaleAbused;
    private String state;
    private String idMale;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return idMale.equals(that.idMale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMale);
    }
}
