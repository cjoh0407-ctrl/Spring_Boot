package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {

        Item item = new Item();

        item.setItemNm("테스트 상품");
        item.setPrice(1000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException());

        assertEquals(3, savedOrder.getOrderItems().size());

    }
/*
    @Query(
            "select o from Order o where o.member.email = :email " +
                    "order by o.orderDate desc "
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
*/
    @Test
    @DisplayName("회원 이메일로 주문 조회 및 페이징 처리")
    public void findOrderByEmailWithPaging(){
        // [Given] 테스트를 위해 준비하는 단계
        String email = "user@user.com"; // 조회할 대상 사용자의 이메일

        // PageRequest.of(페이지 번호, 한 페이지당 데이터 개수)
        // 0번째 페이지에서 딱 2개만 가져오겠다는 설정을 만듭니다.
        Pageable pageable = PageRequest.of(0, 2);

        // [When] 실제로 기능을 실행해보는 단계
        // 설정한 페이징 정보(0번 페이지, 2개)를 가지고 DB에서 주문 목록을 조회합니다.
        List<Order> orders = orderRepository.findOrders(email, pageable);

        // [Then] 실행 결과가 예상과 맞는지 확인하는 단계
        // 가져온 주문 리스트를 하나씩 꺼내서 로그(Console)에 출력해봅니다.
        // 만약 해당 유저의 주문이 10개 있어도, 위에서 2개로 제한했기 때문에 딱 2개만 출력됩니다.
        orders.forEach(o -> log.info(o));
    }

}