package com.tenco.blog_v2.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em;

    // 두 가지 방식으로 연습 - JPQL 사용, JPA API
    @Transactional
    public void updateByIdJPQL(int id, String title, String content) {
        // JPQL 쿼리 작성
        String jpql = " UPDATE Board b SET b.title = :title, b.content = :content WHERE b.id = :id ";
        Query query = em.createQuery(jpql);
        query.setParameter("title", title);
        query.setParameter("content", content);
        query.setParameter("id", id);
        query.executeUpdate(); // 실행
    }

    @Transactional
    public void updateByIdJPA(int id, String title, String content) {
        Board board = em.find(Board.class, id); // PK인 id값을 1차 캐시에서 찾아서 Board 에 담음
        if(board != null) {
            board.setTitle(title);
            board.setContent(content);
        }
        // flush 명령, commit 명령 할 필요 없이
        // 트랜잭션을 선언하면 --> 더티 체킹
    }

    /**
     * 게시글 조회 메서드
     *
     * @param id 조회할 게시글 ID
     * @return 조회된 Board 엔티티, 존재하지 않으면 null 반환
     */
  public Board findById(int id) {
      return em.find(Board.class, id);
  }

    /**
     * JPQL 의 FETCH 조인 사용 - 성능 최적화
     * 한방에 쿼리를 사용해서 즉, 직접 조인해서 데이터를 가져 옵니다.
     * @param id
     * @return
     */
    public Board findByIdJoinUser(int id) {
        // JPQL -> Fetch join 을 사용해 보자.(게시글과 연관된 유저 정보를 함께 가져옴)
        String jpql = " SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id ";
        return em.createQuery(jpql, Board.class) // 특정 게시글 조회
                .setParameter("id", id) // id 값 바인딩함
                .getSingleResult(); // 단일 게시글(하나의 Board 객체)
    }

    /**
     * 모든 게시글 조회
     * @return 게시글 리스트
     */
    public List<Board> findAll() {
        TypedQuery<Board> jpql =
                em.createQuery("SELECT b FROM Board b ORDER BY  b.id desc ", Board.class);
    return jpql.getResultList(); // 결과를 리스트 형식으로 반환
    }
    
    // em.persist(board); -> 비영속 상태인 엔티티를 영속성 상태로 전환
    @Transactional
    public Board save(Board board) {
        em.persist(board);
        return board;
    }
    //DELETE JPA API 메서드를 활용(영속성 컨택트), JPQL --> QDSL
    @Transactional // 트랜젝션 내에서 실행되도록 보장
    public void delete(Integer id) {
        Query jpql = em.createQuery("DELETE FROM Board b WHERE b.id = :id");
        jpql.setParameter("id", id);
        jpql.executeUpdate(); // 쿼리 실행 후 영향을 받은 행의 수를 반환
    }

    /**
     * JPA API 만들어보세요
     * @param id
     */
    //DELETE JPA API 메서드를 활용(영속성 컨택트), JPQL --> QDSL
    @Transactional // 트랜젝션 내에서 실행되도록 보장
    public void deleteIdJPA(Integer id) {
        Board board = em.find(Board.class, id);
        if (board != null) {
            em.remove(board);
        }
    }


}


