package com.example.demo.controller;

import com.example.demo.domain.MemberDTO;
import com.example.demo.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public void list(Model model) {
        log.info("List...............");
        List<MemberDTO> getlist = memberService.getList();
        model.addAttribute("list", getlist);
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        log.info("id : " + id);

        MemberDTO dto = memberService.findById(id);
        model.addAttribute("member", dto);

        return "member/updateForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO dto) {
        memberService.update(dto);

        return "redirect:/member/list";
    }

    @GetMapping("/insertForm")
    public String insertForm(Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/insertForm";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute MemberDTO dto) {
        memberService.insert(dto);
        return "redirect:/member/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        log.info("id : " + id);

        memberService.delete(id);

        return "redirect:/member/list";
    }


}