package com.example.demo.controller;

import com.example.demo.domain.MemberDTO;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public void list(Model model){
        log.info("List...............");
        List<MemberDTO> getlist = memberService.getList();
        model.addAttribute("list", getlist);
    }


}
