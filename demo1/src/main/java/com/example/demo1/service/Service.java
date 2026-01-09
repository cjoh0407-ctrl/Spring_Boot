package com.example.demo1.service;

import com.example.demo1.domain.MemberDTO;
import com.example.demo1.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final MemberMapper memberMapper;

    // 전체 데이타 조회
    public List<MemberDTO> getList() {
        return memberMapper.findAll();
    }

    // 한 행 데이타 조회
    public MemberDTO getSelectById(int memberId){
        return memberMapper.selectById(memberId);
    }

    // 추가
    public void insert(MemberDTO dto){
        memberMapper.insert(dto);
    }

    //업데이트
    public void update(MemberDTO dto) {
        memberMapper.update(dto);
    }


}
