package com.example.girlswing.external.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="man")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Man {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="qid_seq")
    //@SequenceGenerator(name = "qid_seq", sequenceName="questions_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_user")
    private int id_user;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "occupation", columnDefinition="TEXT")
    private String occupation;

    @Column(name = "looking_for")
    private String looking_for;

    @Column(name = "avatar_xxs")
    private String avatar_xxs;

    @Column(name = "avatar_xl")
    private String avatar_xl;

    @Column(name = "avatar_large")
    private String avatar_large;

    @Column(name = "avatar_small")
    private String avatar_small;

    @Column(name = "is_online")
    private boolean is_online;

    @Column(name = "id_partner")
    private String id_partner;

    @Column(name = "last_visit")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_visit;
}
