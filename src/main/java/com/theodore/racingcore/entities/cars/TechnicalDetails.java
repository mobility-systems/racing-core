package com.theodore.racingcore.entities.cars;

import com.theodore.racingcore.entities.enums.DrivetrainPlace;
import com.theodore.racingcore.entities.enums.WheelsDriven;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "technical_details")
public class TechnicalDetails {

    @Id
    @Column(name = "car_id")
    private Long carId;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private CarSpecification carSpecification;

    @Column(name = "zero_to_one_hundred", precision = 4, scale = 2)
    private BigDecimal zeroToOneHundred;

    @Column(name = "top_speed")
    private Integer topSpeed;

    @Column(name = "city_consumption", precision = 4, scale = 2)
    private BigDecimal cityConsumption;

    @Column(name = "highway_consumption", precision = 4, scale = 2)
    private BigDecimal highwayConsumption;

    @Column(name = "electric_range")
    private Integer electricRange;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "wheels_driven")
    private WheelsDriven wheelsDriven;

    @Enumerated(EnumType.STRING)
    @Column(name = "drivetrain_place")
    private DrivetrainPlace drivetrainPlace;

    @Column(name = "total_horsepower")
    private Integer totalHorsePower;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public CarSpecification getCarSpecification() {
        return carSpecification;
    }

    public void setCarSpecification(CarSpecification carSpecification) {
        this.carSpecification = carSpecification;
    }

    public BigDecimal getZeroToOneHundred() {
        return zeroToOneHundred;
    }

    public void setZeroToOneHundred(BigDecimal zeroToOneHundred) {
        this.zeroToOneHundred = zeroToOneHundred;
    }

    public Integer getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(Integer topSpeed) {
        this.topSpeed = topSpeed;
    }

    public BigDecimal getCityConsumption() {
        return cityConsumption;
    }

    public void setCityConsumption(BigDecimal cityConsumption) {
        this.cityConsumption = cityConsumption;
    }

    public BigDecimal getHighwayConsumption() {
        return highwayConsumption;
    }

    public void setHighwayConsumption(BigDecimal highwayConsumption) {
        this.highwayConsumption = highwayConsumption;
    }

    public Integer getElectricRange() {
        return electricRange;
    }

    public void setElectricRange(Integer electricRange) {
        this.electricRange = electricRange;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public WheelsDriven getWheelsDriven() {
        return wheelsDriven;
    }

    public void setWheelsDriven(WheelsDriven wheelsDriven) {
        this.wheelsDriven = wheelsDriven;
    }

    public DrivetrainPlace getDrivetrainPlace() {
        return drivetrainPlace;
    }

    public void setDrivetrainPlace(DrivetrainPlace drivetrainPlace) {
        this.drivetrainPlace = drivetrainPlace;
    }

    public Integer getTotalHorsePower() {
        return totalHorsePower;
    }

    public void setTotalHorsePower(Integer totalHorsePower) {
        this.totalHorsePower = totalHorsePower;
    }

}
