package com.theodore.racingcore.entities.generalentries;

import com.theodore.racingmodel.entities.AuditableUpdateEntity;
import jakarta.persistence.*;

@Entity(name = "GeneralEntry")
@Table(name = "general_entries", uniqueConstraints = {
        @UniqueConstraint(name = "unique_general_specific_code", columnNames = {"general_code", "specific_code"})
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "general_code")
public abstract class GeneralEntry extends AuditableUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "general_code", nullable = false, insertable = false, updatable = false)
    private GeneralCode generalCode;

    @Column(name = "specific_code", nullable = false, length = 30)
    private String specificCode;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeneralCode getGeneralCode() {
        return generalCode;
    }

    public void setGeneralCode(GeneralCode generalCode) {
        this.generalCode = generalCode;
    }

    public String getSpecificCode() {
        return specificCode;
    }

    public void setSpecificCode(String specificCode) {
        this.specificCode = specificCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}