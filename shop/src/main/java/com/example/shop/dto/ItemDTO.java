package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Long id;    //상품 코드
    private String itemNm;  // 상품 명
    private int price;  // 가격
    private String itemDetail; // 상품 상세 설명
    private String sellstatCd;
    private LocalDateTime regTime;  // 등록 시간
    private LocalDateTime updateTime;   // 수정 시간
}
