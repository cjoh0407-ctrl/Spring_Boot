package com.example.shop.service;

import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional // 로직 수행 중 에러 발생 시 롤백을 보장함
public class ItemImgService {

    // application.properties(또는 yml)에 설정된 이미지 저장 경로를 읽어옴
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    // DB 테이블(item_img)에 이미지 정보를 저장하기 위한 리포지토리
    private final ItemImgRepository itemImgRepository;

    // 실제 파일(byte)을 SSD(하드디스크)에 저장하거나 삭제하는 공통 서비스
    private final FileService fileService;

    /**
     * 상품 이미지 정보를 저장하는 메서드
     * @param itemImg 상품 이미지 엔티티 (이미지 정보가 담길 객체)
     * @param itemImgFile 사용자가 화면에서 업로드한 실제 이미지 파일
     */
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        // 1. 업로드된 파일의 원본 파일명 (예: puppy.jpg)
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = ""; // 서버에 저장될 고유 파일명 (UUID 기반)
        String imgUrl = "";  // 웹 브라우저에서 접근할 경로

        // 2. 파일 업로드 처리 (파일이 비어있지 않은 경우에만 실행)
        if(!StringUtils.isEmpty(oriImgName)){

            // FileService를 통해 파일을 실제 경로에 물리적으로 저장하고, 생성된 파일명을 반환받음
            // 예: itemImgLocation에 파일 데이터를 byte 단위로 저장
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            // 웹상에서 이 이미지에 접근할 때 사용할 URL 경로 설정
            // 예: /images/item/550e8400...jpg
            imgUrl = "/images/item/" + imgName;
        }

        // 3. 상품 이미지 엔티티 정보 업데이트 (DB에 들어갈 정보 세팅)
        // 원본 파일명, 서버 저장 파일명, 이미지 URL 경로를 엔티티에 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);

        // 4. DB에 이미지 정보 저장 (JPA 호출)
        itemImgRepository.save(itemImg);
    }

    // 변경한 이미지가 있으면, 기존 이미지 삭제하고, 변경한 이미지를 저장.
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            // 변경감지 -> 자동 저장
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}