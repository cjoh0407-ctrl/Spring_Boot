package com.example.demo1.service;

import com.example.demo1.domain.MemberDTO;
import com.example.demo1.mapper.MemberMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ServiceTest {

    @Autowired
    private Service service;

    @Test
    void getListTest() {
        //given

        //when
        List<MemberDTO> getlist = service.getList();

        //then
        assertNotNull(getlist);
        getlist.forEach(m -> log.info(m));
    }

    @Test
    void getSelectByIdTest(){
        //given
        int memberId = 2;

        //when
        MemberDTO result = service.getSelectById(memberId);

        //then
        assertNotNull(result);
        log.info("service 한 행 출력 결과 테스트 : " + result);
    }

    @Test
    void insertTest(){
        //given
        MemberDTO dto = MemberDTO.builder()
                .name("서비스테스트")
                .age(30)
                .address("풍납동")
                .phone("010-7777-8888")
                .build();

        //when
        service.insert(dto);

        int memberId = 6;
        MemberDTO result = service.getSelectById(memberId);

        //then
        assertNotNull(result);
        log.info("service test insert : " + result);
    }

    @Test
    void updateTest(){
        //given
        MemberDTO dto = MemberDTO.builder()
                .name("서비스테스트")
                .age(50)
                .address("강서구")
                .phone("010-2222-2222")
                .memberId(6)
                .build();

        //when
        service.update(dto);

        int memberId = 6;
        MemberDTO result = service.getSelectById(memberId);

        //then
        assertNotNull(result);
        log.info("service test update : " + result);
    }



}