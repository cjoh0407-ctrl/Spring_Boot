package com.example.jpa2.service;

import com.example.jpa2.domian.Member;
import com.example.jpa2.dto.MemberDTO;
import com.example.jpa2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    //추가
    @Transactional
    public void insert(MemberDTO memberDTO){
        Member member = memberDTO.toEntity();
        memberRepository.save(member);
    }

    //수정
    @Transactional
    public void update(int id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("수정할 회원이 없습니다."));

        member.updateInfo(
                memberDTO.getName(),
                memberDTO.getAge(),
                memberDTO.getAddress(),
                memberDTO.getPhone()
        );
    }

    //삭제
    @Transactional
    public void delete(int memberId) {
        memberRepository.deleteById(memberId);
    }

    //단건조회
    public MemberDTO findById(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 없습니다."));

        return MemberDTO.fromEntity(member);
    }

    //전체 리스트 조회 (페이징 포함)
    public Page<MemberDTO> findAll(Pageable pageable){
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return memberPage.map(member -> MemberDTO.fromEntity(member));
    }


}
