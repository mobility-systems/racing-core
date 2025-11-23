package com.theodore.racingcore.entities.cars;

import com.theodore.racingcore.entities.generalentries.BatteryType;
import jakarta.persistence.*;

@Entity
@Table(name = "electric_motor")
public class ElectricMotor extends MotorDetail {

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battery_type", nullable = false)
    private BatteryType batteryType;

    @Version
    private long version;

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public BatteryType getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(BatteryType batteryType) {
        this.batteryType = batteryType;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}