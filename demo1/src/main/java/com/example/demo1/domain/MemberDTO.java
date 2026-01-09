package com.example.demo1.domain;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private int memberId;
    private String name;
    private int age;
    private String address;
    private String phone;
}
