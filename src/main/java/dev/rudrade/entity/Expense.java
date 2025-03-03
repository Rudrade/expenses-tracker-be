package dev.rudrade.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "EXPENSE")
@Getter
@Setter
@ToString
public class Expense extends PanacheEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(
        name = "DATE_OF_CREATION",
        nullable = false
    )
    private LocalDate dateOfCreation;

    @Column(
        name = "DESCRIPTION",
        nullable = false
    )
    private String description;

    @Column(
        name = "AMOUNT",
        nullable = false
    )
    private double amount;

    @Column(
        name = "CATEGORY"
    )
    private String category;

    @Column(
        name = "NECESSITY"
    )
    private String necessity;
}
