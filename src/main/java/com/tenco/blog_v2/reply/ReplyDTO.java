package com.tenco.blog_v2.reply;

import com.tenco.blog_v2.board.Board;
import com.tenco.blog_v2.user.User;
import lombok.Getter;
import lombok.Setter;

public class ReplyDTO {


    @Getter
    @Setter
    public static class SaveDTO {
        private Integer boardId;
        private String comment;

        // DTO --> JPA 영속성 컨텍스트로 저장한다는 것은 엔티티로 변환해야 한다.
        public Reply toEntity(User sessionUser, Board board) {
            return Reply.builder()
                    .comment(comment)
                    .board(board)
                    .user(sessionUser)
                    .build();
        }

    }


}
