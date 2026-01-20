package com.example.shop.service;

import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember.isPresent())
            throw new IllegalStateException("이미 가입된 회원입니다.");

        return memberRepository.save(member);
    }


}
