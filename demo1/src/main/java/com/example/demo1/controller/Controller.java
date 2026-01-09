package com.example.demo1.controller;

import com.example.demo1.domain.MemberDTO;
import com.example.demo1.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class Controller {

    private final Service service;


    @GetMapping("/list")
    public String getList(Model model){
        List<MemberDTO> dto = service.getList();
        model.addAttribute("members", dto);
        return "/list";
    }

    @GetMapping("/modify")
    public String getModify(
            @RequestParam("memberId") int memberId, Model model){
        MemberDTO dto = service.getSelectById(memberId);
        model.addAttribute("member", dto);
        return "/modify";
    }

    @PostMapping("/register")
    public String register(MemberDTO dto
//                        @RequestParam("name") String name,
//                        @RequestParam("age") int age,
//                        @RequestParam("address") String address,
//                        @RequestParam("phone") String phone
    ){
//        MemberDTO dto = MemberDTO.builder()
//                .name(name)
//                .age(age)
//                .address(address)
//                .phone(phone)
//                .build();

        service.insert(dto);

        return "redirect:/member/list";
    }

    @PostMapping("/update")
    public String update(MemberDTO dto){
        service.update(dto);

        return "redirect:/member/list";
    }


}
