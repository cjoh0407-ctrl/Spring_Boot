package com.example.demo1.mapper;

import com.example.demo1.domain.MemberDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Log4j2
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("전체 데이터 조회")
    void findAllTest(){
        //given 현재 상황은 이렇고

        //when 이거를 통해서
        List<MemberDTO> list = memberMapper.findAll(); //findall 메서드를 통해서 전체 호출

        //then 이 결과를 도출해야한다.
        assertNotNull(list);    // 비어있지 않은지 확인.
        list.forEach(m -> log.info(m)); // 출력 테스트
    }

    @Test
    @DisplayName("한 행 데이터 조회")
    void selectByIdTest(){
        //given
        int memberId = 2;

        //when
        MemberDTO dto = memberMapper.selectById(memberId);

        //then
        assertNotNull(dto);
        log.info("한 행 데이터 조회 결과 : " + dto);
    }

    @Test
    @DisplayName("한 행 추가")
    void InsertTest(){
        //given
        MemberDTO dto = MemberDTO.builder()
                            .name("테스트")
                            .age(20)
                            .address("테스트")
                            .phone("010-6666-7777")
                            .build();

        //when
        memberMapper.insert(dto);

        int memberId = 5;
        MemberDTO list = memberMapper.selectById(memberId);

        //then
        assertNotNull(list);
        log.info("insert 한거 확인용 테스트 : " + list);
    }

    @Test
    @DisplayName("update테스트")
    void updateTest(){
        //given
        MemberDTO dto = MemberDTO.builder()
                .name("업데이트테스트")
                .age(10)
                .address("천호동")
                .phone("010-1111-2222")
                .memberId(6)
                .build();

        //when
        memberMapper.update(dto);

        int memberId = 6;
        MemberDTO result = memberMapper.selectById(memberId);

        //then
        assertNotNull(result);
        log.info(result);

    }

    @Test
    @DisplayName("삭제 테스트")
    void delete(){
        //given
        int memberId = 6;

        //when
        memberMapper.delete(memberId);

        List<MemberDTO> list = memberMapper.findAll();

        //then
        assertNotNull(list);
        list.forEach(m->log.info(list));
    }


}
















