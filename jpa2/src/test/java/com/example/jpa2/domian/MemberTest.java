package com.example.jpa2.domian;

import com.example.jpa2.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MemberTest {

    @Autowired private MemberRepository memberRepository;

    //추가
    @Test
    public void insertTest(){
        Member member = Member.builder()
                        .name("test1")
                        .age(10)
                        .address("강남")
                        .phone("010-1111-1111")
                        .build();


        Member result = memberRepository.save(member);
        log.info("추가 행 확인 : " + result);
    }

    //수정
    @Test
    public void updateTest(){
        Optional<Member> byId = memberRepository.findById(3);
        Member member = byId.get();

        member.setName("test1수정");
        member.setAge(15);
        member.setAddress("성동구");
        member.setPhone("010-2222-2222");

        memberRepository.save(member);

        Optional<Member> result = memberRepository.findById(3);
        log.info("update 확인 : " + result);
    }

    //삭제
    @Test
    public void deleteTest(){
        memberRepository.deleteById(3);

        Optional<Member> result = memberRepository.findById(3);
        log.info("delete 확인 : " + result);
    }

    //전체데이터 조회
    @Test
    public void selectAllTest(){
        List<Member> memberList = memberRepository.findAll();

        memberList.forEach(member -> log.info(member));
    }

    //골라서 조회
    @Test
    public void selectTest(){
//        Member member = memberRepository.findMemberByName("기린");
//        List<Member> memberList = memberRepository.findMemberByAge(13);
        List<Member> memberList = memberRepository.findMemberByAgeBetween(13, 50);
        memberList.forEach(member -> log.info(member));


    }

    @Test
    public void findAddressOrderAgeTest(){
        List<Member> memberList = memberRepository.findAddressOrderAgeDesc("강%");
        memberList.forEach(member -> log.info(member));
    };

    @Test
    public void findAgeOrderAddressTest(){
        List<Member> memberList = memberRepository.findAgeOrderAddress(13);
        memberList.forEach(member -> log.info(member));
    }

    @Test
    public void findAddressOrderByMemberIdTest(){
        List<Member> memberList = memberRepository.findAddressOrderByMemberId("강");
        memberList.forEach(member -> log.info(member));
    }
}