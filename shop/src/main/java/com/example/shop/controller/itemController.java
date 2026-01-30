package com.example.shop.controller;

import com.example.shop.dto.ItemFormDto;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.entity.Item;
import com.example.shop.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class itemController {

    private final ItemService itemService;

    // 1. 상품 등록 페이지로 이동
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        // 타임리프에서 사용할 빈 DTO 객체를 모델에 담아 전송
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    // 2. 상품 등록 실행 (저장 버튼 클릭 시)
    @PostMapping(value = "/admin/item/new")
    public String itemForm(@Valid ItemFormDto itemFormDto,
                           BindingResult bindingResult,
                           Model model,
                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        log.info("------------------ 상품 등록 프로세스 시작 ---------------------------");

        // 입력 데이터 검증(Validation) 에러 발생 시 등록 페이지로 복귀
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        // 첫 번째 이미지(대표 이미지)가 비어있는지 체크
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        log.info("상품 아이디 확인 (신규면 null): " + itemFormDto.getId());

        try {
            // 실제 상품 저장 서비스 호출 (이미지 파일들과 함께 저장)
            itemService.savedItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            // 저장 중 예외 발생 시 에러 메시지를 담아 등록 페이지로 복귀
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }

        // 성공적으로 저장 완료 시 메인 페이지로 리다이렉트
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "item/itemForm";
        } finally {
            return "item/itemForm";
        }

    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {


        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }


        try {
            itemService.savedItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page")Optional<Integer> page,
                             Model model) {
        // 전달받은 page가 있으면 그 값을 사용하고 없으면 0, 페이지당 3개 상품 가져오기
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);




        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemCtl(@PathVariable("itemId") Long itemId, Model model){

        log.info("itemDtl : " + itemId);

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }
}