package com.example.jpa.controller;

import com.example.jpa.domain.Member;
import com.example.jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public void getList(Model model) {
        List<Member> memberList = memberService.findByAll();
        model.addAttribute("memberList", memberList);
    }

    @GetMapping("/new")
    public void getNew() {
    }

    @PostMapping("/new")
    public String insert(@ModelAttribute Member member) {
        memberService.insert(member);
        return "redirect:/member/list";
    }

    @GetMapping("/edit/{memberId}")
    public String getEditForm(@PathVariable int memberId, Model model) {
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "member/edit";
    }

    @PostMapping("/edit/{memberId}")
    public String update(@PathVariable int memberId,
            @ModelAttribute Member member){
        //log.info("넘어온 id : " + member.getMemberId());
        Member member1 = memberService.findById(memberId);

        member1.setName(member.getName());
        member1.setAge(member.getAge());
        member1.setAddress(member.getAddress());
        member1.setPhone(member.getPhone());

        memberService.update(member1);
        return "redirect:/member/list";

        /*
        memberService.findById로 받아서
        set으로 하나하나 넣어서
        insert(save)를 시켜준다.

        더티체킹때문에? entity랑 스냅샷이랑 차이가 발생.?????
         */
    }

    @GetMapping("/delete/{memberId}")
    public String delete(@PathVariable int memberId){
        memberService.delete(memberId);
        return "redirect:/member/list";
    }
}
