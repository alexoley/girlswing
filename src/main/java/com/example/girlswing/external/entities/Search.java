package com.example.girlswing.external.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name="search")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "searchId")
    private Long searchId;

    @Column(name = "searchTime")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Calendar searchTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false)
    private Operator operatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false)
    private Girl girlId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchId")
    private List<Man> men;
}
