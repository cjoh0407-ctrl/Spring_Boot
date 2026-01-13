package com.example.jpa2.repository;

import com.example.jpa2.domian.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findMemberByName(String name);
    List<Member> findMemberByAge(int age);
    List<Member> findMemberByAgeBetween(int ageAfter, int ageBefore);

    @Query(
            "select m from Member m " +
                    "where m.address like :address% " +
                    "order by m.age desc"
    )
    List<Member> findAddressOrderAgeDesc(String address);

    @Query(
            "select m from Member m where m.age = :age order by m.address"
    )
    List<Member> findAgeOrderAddress(int age);

    @Query(value = "select * from Member " +
            "where address like concat('%', :address, '%') " +
            "order by member_id desc",
            nativeQuery = true)
    List<Member> findAddressOrderByMemberId(String address);

}
