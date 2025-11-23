package com.theodore.racingcore.entities.cars;

import com.theodore.racingcore.entities.enums.CarType;
import com.theodore.racingcore.entities.generalentries.HybridType;
import com.theodore.racingcore.entities.generalentries.TransmissionType;
import jakarta.persistence.*;

@Entity
@Table(name = "drivetrain")
public class Drivetrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "electric_motor_id")
    private ElectricMotor electricMotor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hybrid_type")
    private HybridType hybridType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transmission_type")
    private TransmissionType transmissionType;

    @Column(name = "number_of_gears")
    private Integer numberOfGears;

    @Version
    private long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public ElectricMotor getElectricMotor() {
        return electricMotor;
    }

    public void setElectricMotor(ElectricMotor electricMotor) {
        this.electricMotor = electricMotor;
    }

    public HybridType getHybridType() {
        return hybridType;
    }

    public void setHybridType(HybridType hybridType) {
        this.hybridType = hybridType;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    public Integer getNumberOfGears() {
        return numberOfGears;
    }

    public void setNumberOfGears(Integer numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public CarType getCarType() {
        if (hybridType != null) {
            return CarType.HYBRID;
        }
        if (electricMotor == null) {
            return CarType.ICE;
        }
        return CarType.BEV;
    }

}