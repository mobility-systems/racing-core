package com.theodore.racingcore.entities.cars;

import jakarta.persistence.*;

@Entity
@Table(name = "car_specifications")
public class CarSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private CarModel car;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drivetrain_id", nullable = false)
    private Drivetrain drivetrain;

    @OneToOne(mappedBy = "carSpecification", cascade = CascadeType.ALL, orphanRemoval = true)
    private TechnicalDetails technicalDetails;

    @Version
    private long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarModel getCar() {
        return car;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public TechnicalDetails getTechnicalDetails() {
        return technicalDetails;
    }

    public void setTechnicalDetails(TechnicalDetails technicalDetails) {
        this.technicalDetails = technicalDetails;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}