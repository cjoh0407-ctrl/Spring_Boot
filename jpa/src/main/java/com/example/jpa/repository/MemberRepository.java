package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findMemberByName(String name);
    Member findMemberByPhone(String phone);
    List<Member> findByAge(int age);
    Member findByNameAndAddress(String name, String address);

    List<Member> findByAddressLike(String address);
    List<Member> findByNameOrderByAgeDesc(String address);
    List<Member> name(String name);

/*    select * from member
    where address like "강%"
    order by age desc;

 */

    @Query(
        "select m from Member m " +
                "where m.address like %:address% " +
                "order by m.age desc"
    )
    List<Member> findByAddressLikeByAgeDesc(String address);


    @Query(
           "select m from Member m where m.age >= :age order by m.age desc"
    )
    List<Member> findByAgeGreaterThanEqualOrderByAgeDesc(int age);


    @Query(value = "select * from member where age >= :age order by age desc", nativeQuery = true)
    List<Member> findByAgeGreaterThanEqualOrderByAgeDesc2(int age);
}
