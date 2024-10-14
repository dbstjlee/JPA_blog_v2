package com.tenco.blog_v2.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
// @Data // 런타임 시간에 무한 참조? 때문에 권장하지 않음
@Getter
@Setter // 자신만의 정보를 참조할 수 있도록 변경
@Builder
@Table(name="user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true) // 유니크 제약 조건 설정
    private String username;
    private String password;
    private String email;

    // 역할 추가 - 일반 사용자 = "User" / 관리자 = "Admin"
    @Column(nullable = true)
    private String role;

    
    @CreationTimestamp // 엔티티 생성시 자동으로 현재 시간 입력
    private Timestamp createdAt;
    
    // 양방향 -> 단방향
    

//    // 단방향, 양방향 매핑(mappedBy)
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // 지연 로딩 설정(연관된 엔티티를 필요 시 가져옴)
//   //@OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // 즉시 로딩 설정(연관된 엔티티 즉시 조회)
//    private List<Board> boards;
//
//    public User(Integer id, String username, String password, String email, String role, Timestamp createdAt, List<Board> boards) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.role = role;
//        this.createdAt = createdAt;
//        this.boards = boards;
//    }
  }
