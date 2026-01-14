package com.example.jpa2.domian;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@Builder
@ToString
public class Member {

    @Id //primarykey?
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int memberId;

    @Column(nullable = false, length = 50)
    private String name;
    private int age;

    @Column(nullable = false, length = 200)
    private String address;
    private String phone;


    public void updateInfo(String name, int age, String address, String phone){
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }
}
