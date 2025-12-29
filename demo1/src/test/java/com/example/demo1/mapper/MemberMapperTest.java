package com.example.demo1.mapper;

import com.example.demo1.domain.MemberDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("전체 데이타 조회")
    void findAll() {

        //given

        //when
        List<MemberDTO> list = memberMapper.findAll();

        //then
        Assertions.assertNotNull(list);
        list.forEach(m -> log.info(m));

    }
}