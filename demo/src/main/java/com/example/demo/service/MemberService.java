package com.example.demo.service;

import com.example.demo.domain.MemberDTO;
import com.example.demo.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public List<MemberDTO> getList(){
        return memberMapper.findAll();
    }
}
