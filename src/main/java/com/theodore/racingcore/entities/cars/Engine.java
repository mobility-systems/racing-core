package com.theodore.racingcore.entities.cars;

import com.theodore.racingcore.entities.enums.EngineType;
import com.theodore.racingcore.entities.enums.FuelType;
import jakarta.persistence.*;

@Entity
@Table(name = "engine")
public class Engine extends MotorDetail {

    @Column(name = "displacement")
    private Integer displacement;

    @Enumerated(EnumType.STRING)
    @Column(name = "engine_type", nullable = false)
    private EngineType engineType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;

    @Version
    private long version;

    public Integer getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Integer displacement) {
        this.displacement = displacement;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}