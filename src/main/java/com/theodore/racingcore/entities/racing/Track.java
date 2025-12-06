package com.theodore.racingcore.entities.racing;

import com.theodore.infrastructure.common.entities.AuditableUpdateEntity;
import com.theodore.infrastructure.common.enums.Country;
import jakarta.persistence.*;

@Entity
@Table(name = "track")
public class Track extends AuditableUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "track_name", nullable = false, length = 150)
    private String trackName;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "track_length", nullable = false)
    private Integer trackLength;

    @Column(name = "racing_approved", nullable = false)
    private Boolean racingApproved = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(Integer trackLength) {
        this.trackLength = trackLength;
    }

    public Boolean getRacingApproved() {
        return racingApproved;
    }

    public void setRacingApproved(Boolean racingApproved) {
        this.racingApproved = racingApproved;
    }

}