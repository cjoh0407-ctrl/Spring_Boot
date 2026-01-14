package com.example.jpa2.dto;

import com.example.jpa2.domian.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class MemberDTO {

    private Integer memberId;
    private String name;
    private int age;
    private String address;
    private String phone;

    // DTO -> Entity 변환 메서드
    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .age(this.age)
                .address(this.address)
                .phone(this.phone)
                .build();
    }

    // Entity에서 DTO로 변환 메소드
    public static MemberDTO fromEntity(Member member){
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .age(member.getAge())
                .address(member.getAddress())
                .phone(member.getPhone())
                .build();
    }

}
