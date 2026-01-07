package com.theodore.racingcore.entities.racing;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "driver")
public class Driver {

    @Id
    @Column(name = "id", length = 26, nullable = false)
    private String id;

    @Column(name = "trophies", nullable = false)
    private Integer trophies = 0;

    @OneToMany(mappedBy = "driver")
    private Set<Lap> laps = new HashSet<>();

    @Column(name = "alias")
    private String alias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTrophies() {
        return trophies;
    }

    public void setTrophies(Integer trophies) {
        this.trophies = trophies;
    }

    public Set<Lap> getLaps() {
        return laps;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
