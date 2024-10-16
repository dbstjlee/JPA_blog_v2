package com.tenco.blog_v2.reply;

import com.tenco.blog_v2.board.Board;
import com.tenco.blog_v2.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 생성자
@Builder
@Entity
@Table(name = "reply_tb")
@ToString(exclude = {"user", "board"}) // 연관된 엔티티를 제외하여 순환 참조 방지 및 보안 강화 때문에 사용한다.
public class Reply {

    // 일반적으로 id는 Long 타입을 사용하는 것을 권장한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // null 값이 들어올 수 없어 - 기본값은 null 허용
    @Column(nullable = false)
    private String comment;

    // 단방향 관계로 설계 -> User 엔티티에는 Reply 정보가 없다.
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "user_id")
    // 한 사람의 사용자는 여러개의 게시글을 작성할 수 있다.
    private User user;

    // Board : 1 - Reply : N
    // 양방향 매핑(FK의 주인은 댓글(reply)이다.)
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "board_id")
    private Board board;

    // JPA 엔티티에서 데이터베이스에 저장할 필요 없이 메모리 상에서 사용할 필드를 정의할 때 사용한다.
    @Transient
    private boolean isReplyOwner;

    @Builder.Default // Default 값을 사용하기 위해 사용(@Builder 선언 필요)
    private String status = "ACTIVE"; // 댓글 상태(ACTIVE, DELETED)

    // RDBMS에 생성
    @CreationTimestamp // 엔티티가 생성될 때 자동으로 현재 시간으로 설정해줌(쿼리문 던질 때 now() 쓸 필요 없음)
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    /**
     * 엔티티가 데이터베이스에 영속화되기 전에 호출되는 메서드가 있다면 사용한다.
     * @PrePersist 어노테이션은 JPA 라이프 사이클 이벤트 중 하나로 엔티티가 영속화 되기 전에 실행된다.
     */
    @PrePersist
    protected void onCreate() {
        if(this.status == null) {
            this.status =  "ACTIVE"; // 기본값 주기
        }
        if(this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

    }



}
