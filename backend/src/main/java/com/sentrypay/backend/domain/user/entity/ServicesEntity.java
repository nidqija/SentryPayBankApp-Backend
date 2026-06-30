package com.sentrypay.backend.domain.user.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity // declares that this class is a JPA entity and will be mapped to a database table
@Table(name = "services") // define the table name
@Data // generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // generates a no-argument constructor

public class ServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String servicesId;

    @Column(nullable = false, unique = true)
    private String serviceName;

    @Column(nullable = false)
    private String serviceDescription;

    @Column(nullable = false)
    private double servicePrice;

    @Column(nullable = false)
    private String serviceType;

    @Column(nullable = false)
    private String renewalPeriod;

    @Column(nullable = false)
    private String currency = "USD"; // default currency set to USD

    
}
