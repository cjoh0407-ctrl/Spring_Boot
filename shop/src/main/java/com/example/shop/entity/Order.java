package com.example.shop.entity;

import com.example.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;    // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문 상태

    @OneToMany(mappedBy = "order",      // 이 필드는 연관관계의 주인이 아니니, 데이터베이스 외래 키(FK)를 관리하지 마라
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private List<OrderItem> orderItem = new ArrayList<>();

    private LocalDateTime regTime;      // 주문 시간
    private LocalDateTime updateTime;   // 수정 시간

}
