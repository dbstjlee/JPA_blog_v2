package com.tenco.blog_v2.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJPARespository extends JpaRepository<User, Integer> {

    // 사용자 이름과 비밀번호를 조회하는 메서드 이름 기반 쿼리를 작성
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    // 사용자의 이름 기반으로 조회
    // 메서드 이름 기반
    Optional<User> findByUsername(@Param("username") String username); //JPOL 기반으로 쿼리문 자동 생성해줌
}
