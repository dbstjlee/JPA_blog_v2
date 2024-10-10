package com.tenco.blog_v2.user;

import lombok.Data;

@Data
public class UserDTO {
    // 정적 내부 클래스로 모으자
    @Data
    public static class LoginDTO{

        private String username;
        private String password;

    }

    // controller 에서 이걸로 바인딩 함
    @Data
    public static class JoinDTO{

        private String username;
        private String password;
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();

        }
    }

    @Data
    public static class UpdateDTO {
        private String password;
        private String email;
    }


}
