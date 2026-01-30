package com.example.shop.controller;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@Log4j2
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * [1] 상품 주문 처리
     * @RequestBody: JSON 데이터를 자바 객체(orderDto)로 변환해서 받음
     * @Valid: 입력값 검증 (수량, 상품 ID 등)
     * @ResponseBody: 데이터(ResponseEntity)를 직접 클라이언트(fetch/axios)에 반환
     */
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid OrderDto orderDto,
                                                 BindingResult bindingResult,
                                                 Principal principal){

        // 1. 입력 데이터 검증에 실패한 경우 (예: 수량이 0이거나 없을 때)
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage()); // 에러 메시지들을 하나로 합침
            }
            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST); // 400 에러 반환
        }

        // 2. 현재 로그인한 사용자의 이메일(ID) 정보 가져오기
        String email = principal.getName();

        Long OrderId;
        try {
            // 3. 서비스 계층에 주문 로직 요청 (비즈니스 로직 수행)
            OrderId = orderService.order(orderDto, email);
        } catch (Exception e){
            // 재고 부족 등 예외 발생 시 에러 메시지 반환
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 4. 주문이 성공하면 생성된 주문 번호와 함께 200 OK 상태 반환
        return new ResponseEntity<Long>(OrderId, HttpStatus.OK);
    }

    /**
     * [2] 주문 내역 조회 및 페이징
     * URL 경로가 "/orders" 이거나 "/orders/0" 형태일 때 모두 대응
     */
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page,
                            Principal principal, Model model) {
        // Principal = 사용자가 누구인지 알기위함. 현재 로그인한 사용자의 정보를 담고 있는 열쇠(객체)

        // 1. 페이징 설정: (현재 페이지 번호, 한 페이지당 데이터 개수)
        // 사용자가 넘긴 페이지 번호가 없으면(Optional) 0번 페이지를 기본으로 설정함
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        // 2. 서비스에서 현재 사용자의 이메일과 페이징 정보를 이용해 주문 내역 조회
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        // 3. 뷰(Thymeleaf)로 전달할 데이터 담기
        model.addAttribute("orders", orderHistDtoList); // 조회된 주문 데이터 리스트
        model.addAttribute("page", pageable.getPageNumber()); // 현재 페이지 번호
        model.addAttribute("maxPage", 5); // 하단에 보여줄 최대 페이지 버튼 개수

        // 4. HTML 파일 경로 반환 (templates/order/orderHist.html)
        return "order/orderHist";
    }

    @PostMapping(value = "/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity<?> cancelOrder(
            @PathVariable("orderId") Long orderId, Principal principal){

        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}