package com.example.girlswing.external.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="girl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Girl {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "girlId")
    private Long girlId;*/

    @Id
    @Column(name="id")
    private long id;

    @Column(name="id_user")
    private long id_user;

    @Column(name="name")
    private String name;

    @Column(name="age")
    private int age;

    @Column(name="country")
    private String country;

    @Column(name="city")
    private String city;

    @Column(name = "gender")
    private String gender;

    @Column(name = "occupation", columnDefinition="TEXT")
    private String occupation;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Column(name = "looking_for", columnDefinition="TEXT")
    private String looking_for;

    @Column(name = "avatar_xxs")
    private String avatar_xxs;

    @Column(name = "avatar_xl")
    private String avatar_xl;

    @Column(name = "avatar_large")
    private String avatar_large;

    @Column(name = "avatar_small")
    private String avatar_small;

    @Column(name = "avatar_original")
    private String avatar_original;

    @Column(name = "audioEnabled")
    private int audioEnabled;

    @Column(name = "count_photos")
    private int count_photos;

    @Column(name = "count_videos")
    private String count_videos;

    @Column(name = "is_online")
    private boolean is_online;

    @Column(name = "id_partner")
    private String id_partner;

    @Column(name = "can_receive_gift")
    private int can_receive_gift;

    @Column(name = "imbra_verification")
    private int imbra_verification;

    @Column(name = "mirrorName")
    private String mirrorName;

    @Column(name = "last_visit")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_visit;

    @Column(name = "is_real_female")
    private boolean is_real_female;

    @Column(name = "haveAccessToSend", columnDefinition = "boolean default true")
    private boolean haveAccessToSend;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "girlId")
    private List<Search> search;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false)
    private Operator operatorId;
}
