package com.tenco.blog_v2.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor// Ioc
public class BoardNativeRepository {
    //DI 처리
    private final EntityManager em;

    /**
     * 새로운 게시글 생성
     *
     * @param title
     * @param content
     */
    @Transactional
    public void save(String title, String content) {
        Query query = em.createNativeQuery(
                "Insert INTO  board_tb(title, content,created_at) values (?, ?,NOW())"
        );

        query.setParameter(1, title);
        query.setParameter(2, content);
        // 실행
        query.executeUpdate();
    }

    /**
     * 특정 ID 의 게시글을 조회 합니다.
     *
     * @param id
     * @return
     */
    public Board findById(int id) {
        Query query = em.createNativeQuery("SELECT * FROM board_tb WHERE id = ?", Board.class);
        query.setParameter(1, id);
        return (Board) query.getSingleResult();

    }

    /**
     * 모든 게시글 조회
     *
     * @return
     */
    public List<Board> findAll() {
        Query query = em.createNativeQuery("SELECT * FROM board_tb ORDER BY  id DESC ", Board.class);
        return query.getResultList();
    }

    /**
     * 특정 ID로 게시글 수정하는 기능
     *
     * @param id
     * @param title
     * @param content
     */
    @Transactional
    public void updateById(int id, String title, String content) {
        Query query = em.createNativeQuery("UPDATE board_tb SET title = ?, content = ? WHERE id = ?");
        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, id);
        query.executeUpdate();
    }

    /**
     * 특정 ID의 게시글 삭제
     *
     * @param id
     */
    @Transactional
    public void delete(int id) {
        Query query = em.createNativeQuery("DELETE FROM board_tb WHERE id = ?", Board.class);
        query.setParameter(1, id);
        query.executeUpdate();
    }
}
