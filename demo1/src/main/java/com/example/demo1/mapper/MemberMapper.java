package com.example.demo1.mapper;

import com.example.demo1.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    List<MemberDTO> findAll();
}
