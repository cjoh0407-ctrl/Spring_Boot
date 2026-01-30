package com.example.shop.service;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemFormDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 컨테이너를 띄워서 통합 테스트 수행
@Log4j2        // 로그 출력용
class ItemServiceTest {

    @Autowired
    ItemService itemService; // 테스트 대상 서비스

    @Autowired
    ItemRepository itemRepository; // Item 엔티티 검증용

    @Autowired
    ItemImgRepository itemImgRepository; // ItemImg 엔티티 검증용

    /**
     * 테스트용 MultipartFile 리스트 생성 메서드
     * 실제 파일 업로드 대신 MockMultipartFile을 사용
     */
    List<MultipartFile> createMultiPartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        // 테스트용 이미지 파일 5개 생성
        for (int i = 0; i < 5; i++) {
            String path = "c:/shop/item/";
            String imageName = "image" + i + ".jpg";

            // 가짜 MultipartFile 생성
            MockMultipartFile mockMultipartFile = new MockMultipartFile(
                    path,              // 파일 경로
                    imageName,         // 파일 이름
                    "image/jpg",       // Content-Type
                    new byte[]{1, 2, 3, 4, 5} // 파일 내용 (임의의 바이트 배열)
            );
            multipartFileList.add(mockMultipartFile);
        }

        return multipartFileList;
    }

    /**
     * 상품 저장 테스트
     * - 상품 정보 저장
     * - 상품 이미지 저장
     * - 상품과 이미지의 연관관계 검증
     */
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    // Spring Security 적용 환경에서 인증된 사용자로 테스트 실행
    public void saveItem() throws Exception {

        // 상품 등록용 DTO 생성
        ItemFormDto itemFormDto = ItemFormDto.builder()
                .itemNm("테스트 상품")                 // 상품명
                .itemSellStatus(ItemSellStatus.SELL) // 판매 상태
                .itemDetail("테스트 상품입니다.")     // 상품 설명
                .price(1000)                          // 가격
                .stockNumber(100)                     // 재고 수량
                .build();

        // 테스트용 이미지 파일 리스트 생성
        List<MultipartFile> multipartFileList = createMultiPartFiles();

        // 상품 + 이미지 저장 후 상품 ID 반환
        Long itemId = itemService.savedItem(itemFormDto, multipartFileList);

        // 저장된 상품 이미지 목록 조회 (ID 오름차순)
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        log.info(itemImgList);

        // 저장된 상품 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 첫 번째 이미지가 해당 상품과 정상적으로 연관되어 있는지 검증
        assertEquals(itemImgList.get(0).getItem().getId(), item.getId());
//        assertEquals(10, item.getId());
    }
}
