package com.tenco.blog_v2.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// @Repository 어노테이션 생략 -> IoC 처리됨
public interface ReplyJPARepository extends JpaRepository<Reply, Integer> {
    
    // 기본적인 주요 메서드 제공 받음(구현체를 만들어 준다.)

    // 1. 커스텀 쿼리를 만들어 본다. 어노테이션 사용
    // boardId를 통해서 리플 정보를 조회하는 기능
    @Query("select r from Reply r where r.board.id = :boardId")
    // 다중행
    List<Reply> findByBoardId(@Param("boardId") Integer boardId); // 알아서 메서드의 바디를 만들어 준다.

    
    
}
