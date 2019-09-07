package com.example.girlswing.external.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="operator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Operator {


    /*@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operatorId")
    private Long operatorId;*/

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "id_role")
    private int id_role;

    @Column(name = "id_agency")
    private int id_agency;

    @Column(name = "id_user")
    private int id_user;

    @Column(name = "legal_firstname")
    private String legal_firstname;

    @Column(name = "legal_lastname")
    private String legal_lastname;

    @Column(name = "id_thread")
    private int id_thread;

    @Column(name = "haveAccessToSend", columnDefinition = "boolean default true")
    private boolean haveAccessToSend;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operatorId")
    private List<Search> search;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operatorId")
    private List<Girl> girls;
}
