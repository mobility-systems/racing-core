package com.theodore.racingcore.entities.racing;

import com.theodore.infrastructure.common.entities.AuditableUpdateEntity;
import com.theodore.racingcore.entities.cars.CarSpecification;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lap")
public class Lap extends AuditableUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private CarSpecification car;

    @Column(name = "lap_date", nullable = false)
    private LocalDateTime lapDate;

    @Column(name = "lap_time", nullable = false, precision = 9, scale = 6)
    private BigDecimal lapTime;

    @Column(name = "special_tires", nullable = false)
    private Boolean specialTires = false;

    @Column(name = "lap_approved")
    private Boolean lapApproved = false;

    @Column(name = "comments", length = 500)
    private String comments;

    @Column(name = "admin_comments", length = 500)
    private String adminComments;

    @Column(name = "highest_recorded_speed")
    private Integer highestRecordedSpeed = 0;

    @Version
    private long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public CarSpecification getCar() {
        return car;
    }

    public void setCar(CarSpecification car) {
        this.car = car;
    }

    public LocalDateTime getLapDate() {
        return lapDate;
    }

    public void setLapDate(LocalDateTime lapDate) {
        this.lapDate = lapDate;
    }

    public BigDecimal getLapTime() {
        return lapTime;
    }

    public void setLapTime(BigDecimal lapTime) {
        this.lapTime = lapTime;
    }

    public Boolean getSpecialTires() {
        return specialTires;
    }

    public void setSpecialTires(Boolean specialTires) {
        this.specialTires = specialTires;
    }

    public Boolean getLapApproved() {
        return lapApproved;
    }

    public void setLapApproved(Boolean lapApproved) {
        this.lapApproved = lapApproved;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public Integer getHighestRecordedSpeed() {
        return highestRecordedSpeed;
    }

    public void setHighestRecordedSpeed(Integer highestRecordedSpeed) {
        this.highestRecordedSpeed = highestRecordedSpeed;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}