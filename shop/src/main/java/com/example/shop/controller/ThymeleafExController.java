package com.example.shop.controller;

import com.example.shop.dto.ItemDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value = "/ex02")
    public String StringthymeleafExample02(Model model) {
        ItemDto item = ItemDto.builder()
                .itemNm("테스트 상품1")
                .itemDetail("상품 상세 설명")
                .price(10000)
                .regTime(LocalDateTime.now())
                .build();
        model.addAttribute("itemDTO", item);

        return "thymeleafEx/thymeleafEx02";

    }

    @GetMapping(value = "/ex03")
    public String StringthymeleafExample03(Model model) {

        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i=1; i<=10; i++){
            ItemDto item = ItemDto.builder()
                    .itemNm("테스트 상품" + i)
                    .itemDetail("상품 상세 설명" + i)
                    .price(10000*i)
                    .regTime(LocalDateTime.now())
                    .build();

            itemDtoList.add(item);
        }

        model.addAttribute("itemDTOList", itemDtoList);

        return "thymeleafEx/thymeleafEx03";

    }

    @GetMapping(value = "/ex04")
    public String StringthymeleafExample04(Model model) {

        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i=1; i<=10; i++){
            ItemDto item = ItemDto.builder()
                    .itemNm("테스트 상품" + i)
                    .itemDetail("상품 상세 설명" + i)
                    .price(10000*i)
                    .regTime(LocalDateTime.now())
                    .build();

            itemDtoList.add(item);
        }

        model.addAttribute("itemDTOList", itemDtoList);

        return "thymeleafEx/thymeleafEx04";

    }

    @GetMapping(value = "/ex05")
    public String StringthymeleafExample05(Model model) {

        return "thymeleafEx/thymeleafEx05";

    }

    @GetMapping(value = "/ex07")
    public String StringthymeleafExample07(Model model) {

        return "thymeleafEx/thymeleafEx07";

    }

}
