package com.example.demo1.mapper;

import com.example.demo1.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    List<MemberDTO> findAll(); //전체 조회

    MemberDTO selectById(int MemberId); // 한개의 행 조회

    void insert(MemberDTO dto); // 추가

    void update(MemberDTO dto); // 업데이트

    void delete(int memberId); // 삭제


}
