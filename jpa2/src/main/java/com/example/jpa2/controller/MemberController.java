package com.example.jpa2.controller;

import com.example.jpa2.domian.Member;
import com.example.jpa2.dto.MemberDTO;
import com.example.jpa2.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    //전체리스트페이지 (페이징)
    @GetMapping("/list")
    public void findAll(@PageableDefault(size = 3, sort = "memberId",
            direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        Page<MemberDTO> memberPage = memberService.findAll(pageable);

        model.addAttribute("memberList", memberPage.getContent());
        model.addAttribute("page", memberPage);
    }

    //신규회원등록 페이지로 이동
    @GetMapping("/new")
    public void getNew() {

    }

    //신규회원등록하기
    @PostMapping("/new")
    public String insert(@ModelAttribute MemberDTO memberDTO){
        memberService.insert(memberDTO);
        return "redirect:/member/list";
    }

    //수정페이지로
    @GetMapping("/edit/{memberId}")
    public String getEdit(@PathVariable ("memberId") int id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "member/edit";
    }

    //수정 완료 update
    @PostMapping("edit/{memberId}")
    public String update(@PathVariable("memberId") int id,
                         @ModelAttribute MemberDTO memberUpdate){
        memberService.update(id, memberUpdate);
        return "redirect:/member/list";
    }

    //삭제
    @GetMapping("delete/{memberId}")
    public String delete(@PathVariable ("memberId") int id){
        memberService.delete(id);
        return "redirect:/member/list";
    }



}
