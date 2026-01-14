package com.example.jpa2.service;

import com.example.jpa2.domian.Member;
import com.example.jpa2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //추가
    public void insert(Member member){
        memberRepository.save(member);
    }

    //수정
    public void update(Member member) {
        memberRepository.save(member);
    }

    //삭제
    public void delete(int memberId) {
        memberRepository.deleteById(memberId);
    }

    //단건조회
    public Member findById(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 없습니다."));

        return member;
    }

    //전체 리스트 조회 (페이징 포함)
    public Page<Member> findAll(Pageable pageable){
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return memberPage;
    }


}
