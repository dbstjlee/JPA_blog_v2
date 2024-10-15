package com.tenco.blog_v2.board;

import com.tenco.blog_v2.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "board_tb")
//@Data
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor -> Builder 만들어 놓아서 필요X
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 전략 db 위임
    private Integer id;
    private String title;
    @Lob // 대용량 데이터 저장 가능 (스마트 에디터의 블롭)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 게시글 작성자 정보

    // created_at 컬럼과 매핑하며, 이 필드는 데이터 저장시 자동으로 설정 됨
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    // 코드 추가
    // 해당 테이블에 컬럼을 만들지 마
    // 즉, JPA 메모리 상에서만 활용 가능한 필드이다.
    @Transient
    boolean isBoardOwner;


    @Builder
    public Board(Integer id, String title, String content, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }




}
