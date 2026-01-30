package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.dto.OrderItemDto;
import com.example.shop.entity.*;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional // 로직 처리 중 하나라도 실패하면 DB를 원래대로 되돌림(Rollback)
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemImgRepository itemImgRepository;

    /**
     * [1] 주문하기 로직
     */
    public Long order(OrderDto orderDto, String email){

        // 1. 주문할 상품 조회 (없으면 예외 발생)
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 2. 주문하는 회원 조회 (없으면 예외 발생)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        // 3. 주문 상품 리스트 생성 (하나의 주문에 여러 상품이 들어갈 수 있도록 리스트 구조)
        List<OrderItem> orderItemList = new ArrayList<>();

        // 4. 주문 상품 엔티티 생성 (상품 정보와 수량을 조합)
        // 이 과정에서 상품의 재고가 줄어드는 로직이 내부적으로 실행됨
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // 5. 주문 엔티티 생성 (회원 정보와 주문 상품 리스트를 조합)
        Order order = Order.createOrder(member, orderItemList);

        // 6. DB에 주문 정보 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * [2] 주문 내역 조회 로직
     */
    @Transactional(readOnly = true) // 읽기 전용으로 설정하여 성능 최적화
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 1. 해당 사용자의 주문 목록을 페이징 조건에 맞춰 DB에서 가져옴
        List<Order> orders = orderRepository.findOrders(email, pageable);

        // 2. 사용자의 전체 주문 개수를 조회 (페이징 계산용)
        Long totalCount = orderRepository.countOrder(email);

        // 3. 최종적으로 화면에 보여줄 DTO 리스트 생성
        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        // 4. 주문(Order) 엔티티들을 순회하며 DTO로 변환
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order); // 주문 기본 정보 담기

            // 5. 해당 주문에 포함된 상품들(OrderItem)을 하나씩 확인
            List<OrderItem> orderItemLists = order.getOrderItems();

            for (OrderItem orderItem : orderItemLists) {
                // 6. 상품의 대표 이미지("Y"인 이미지)를 DB에서 찾음
                ItemImg itemImg = itemImgRepository
                        .findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");

                // 7. 상품 정보와 이미지 URL을 합쳐서 OrderItemDto 생성
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());

                // 8. 주문 DTO 안에 상품 DTO를 추가 (한 주문 내 여러 상품)
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            // 9. 완성된 주문 DTO를 리스트에 담기
            orderHistDtoList.add(orderHistDto);
        }

        // 10. 최종 결과(DTO 리스트, 페이징 정보, 전체 개수)를 묶어서 반환
        return new PageImpl<>(orderHistDtoList, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {

        // 로그인 유저
        Member curMember = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // 주문한 유저
        log.info("---------------------Member saveMember = order.getMember();---------------------------");
        Member saveMember = order.getMember();

        if (StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        order.cancelOrder();;
    }
}