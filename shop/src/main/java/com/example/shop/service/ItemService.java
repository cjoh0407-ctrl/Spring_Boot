package com.example.shop.service;

import com.example.shop.dto.ItemFormDto;
import com.example.shop.dto.ItemImgDto;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.dto.MainItemDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;    // 상품 정보를 저장하는 리포지토리
    private final ItemImgService itemImgService;    // 상품 이미지 저장을 처리하는 서비스
    private final ItemImgRepository itemImgRepository; // 상품 이미지 정보를 저장하는 리포지토리

    /**
     * 상품 등록 로직
     * @param itemFormDto 화면에서 입력받은 상품 데이터 (이름, 가격, 상세설명 등)
     * @param itemImgFileList 화면에서 넘어온 여러 개의 이미지 파일 리스트
     * @return 등록된 상품의 ID
     */
    public Long savedItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 1. 상품 등록: DTO 객체를 엔티티로 변환하여 DB에 먼저 저장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 2. 이미지 등록: 넘겨받은 파일 리스트의 개수만큼 반복문 실행
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();

            // 이미지 엔티티와 상품 엔티티를 연결 (어떤 상품의 이미지인지 설정)
            itemImg.setItem(item);

            // 3. 대표 이미지 여부 설정
            // i가 0이면(첫 번째 이미지이면) 대표 이미지로 설정
            if (i == 0) {
                itemImg.setRepimgYn("Y"); // 대표 이미지임
            } else {
                itemImg.setRepimgYn("N"); // 대표 이미지 아님
            }

            // 4. 실제로 이미지 파일을 SSD에 저장하고 DB 정보를 기록함 (ItemImgService 호출)
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        // 저장된 상품의 ID를 반환
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 저장한 이미지 가져옴
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 저장된 상품정보 가져오기
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // itemImgDtoList + item -> itemFormDto
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDto);

        //이미지 수정 파트
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }


}