package com.example.shop.repository;

import com.example.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JpaRepository<엔티티, ID타입>: 기본적인 CRUD(저장, 조회, 수정, 삭제)를 자동으로 제공함
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * [1] 사용자 주문 목록 조회 (JPQL 사용)
     * :email 은 @Param("email")로 전달받은 변수를 의미함
     * Pageable을 파라미터로 받으면 JPA가 자동으로 'limit'와 'offset'을 계산해줌
     */
    @Query(
            "select o from Order o where o.member.email = :email " +
                    "order by o.orderDate desc " // 최신 주문 날짜순으로 정렬
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    /**
     * [2] 사용자의 전체 주문 개수 조회
     * 페이징 처리 시 하단에 '총 페이지 수'를 계산하기 위해 반드시 필요함
     */
    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);
}