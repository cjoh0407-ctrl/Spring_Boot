package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    //추가
    @Test
    public void insertTest() {
        Member member = Member.builder()
                .name("길동")
                .age(2)
                .phone("111")
                .address("서초구")
                .build();

        memberRepository.save(member);
    }

    //수정
    @Test
    public void updateTest() {
        Optional<Member> optMember = memberRepository.findById(1);
        Member member = optMember.get();

        member.setName("강산");
        member.setAge(5);
        member.setAddress("천호동");

        memberRepository.save(member);
    }

    //삭제
    @Test
    public void deleteTest(){
        memberRepository.deleteById(1);
    }

    //전체 데이터 조회
    @Test
    public void selectAllTest(){
        List<Member> memberList = memberRepository.findAll();

        memberList.forEach(member -> log.info(member));

    }

    //단건 조회
    @Test
    public void selectTest(){
//        Member member = memberRepository.findMemberByName("기린");
//        Member member = memberRepository.findMemberByName("010-1111-7777");
//        List<Member> member = memberRepository.findByAge(13);
//        Member member = memberRepository.findByNameAndAddress("길동", "강동구");
//        List<Member> byAgeGreaterThanEqual = memberRepository.findByAgeGreaterThanEqual(13, Limit.unlimited());
//        List<Member> byAgeGreaterThanEqual = memberRepository.findByAgeGreaterThanEqual(13);
        List<Member> byAddressLike = memberRepository.findByAddressLike("강%");

        byAddressLike.forEach(member -> log.info(member));
    }

    @Test
    public void orderAge(){

        List<Member> memberList = memberRepository.findByAddressLikeByAgeDesc("강%");
        memberList.forEach(member -> log.info(member));

    }

    @Test
    public void equalAge(){
        List<Member> memberList = memberRepository.findByAgeGreaterThanEqualOrderByAgeDesc2(13);
        memberList.forEach(member -> log.info(member));
    }
}