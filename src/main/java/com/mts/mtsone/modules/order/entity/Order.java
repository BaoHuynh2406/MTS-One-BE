package com.mts.mtsone.modules.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ecom_name")
    private Ecom ecom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivery_at")
    private Date deliveryAt;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "customer_id", nullable = true)
    private UUID customerId;

    @Column(name = "order_image")
    private String orderImage;

    @Column(name = "delivery_photo")
    private String deliveryPhoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
}
