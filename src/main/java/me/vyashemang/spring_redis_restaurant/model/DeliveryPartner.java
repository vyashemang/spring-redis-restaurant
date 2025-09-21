package me.vyashemang.spring_redis_restaurant.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_partners")
@Data
@Accessors(chain = true)
public class DeliveryPartner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 15, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "int2")
    private DeliveryPartnerStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // One-to-Many relationship with OrderAssignment
    @OneToMany(mappedBy = "deliveryPartner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderAssignment> orderAssignments = new ArrayList<>();

    // Default constructor
    public DeliveryPartner() {}

    // Constructor with required fields
    public DeliveryPartner(String name, String phoneNumber, DeliveryPartnerStatus status) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }


}
