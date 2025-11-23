package com.theodore.racingcore.entities.cars;

import jakarta.persistence.*;

@MappedSuperclass
public class MotorDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "horsepower", nullable = false)
    private Integer horsepower;

    @Column(name = "torque", nullable = false)
    private Integer torque;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "motor_code", length = 100)
    private String motorCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

    public Integer getTorque() {
        return torque;
    }

    public void setTorque(Integer torque) {
        this.torque = torque;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMotorCode() {
        return motorCode;
    }

    public void setMotorCode(String motorCode) {
        this.motorCode = motorCode;
    }

}