package com.example.demo1.domain;

/*
create table member(
	member_id int auto_increment primary key,
    name varchar(50) not null,
    age int,
    address varchar(100),
    phone varchar(20)
);
 */

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {
    private int memberId;
    private String name;
    private int age;
    private String address;
    private String phone;
}
