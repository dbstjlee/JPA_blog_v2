package com.tenco.blog_v2.reply;

import com.tenco.blog_v2.board.Board;
import com.tenco.blog_v2.board.BoardJPARepository;
import com.tenco.blog_v2.common.errors.Exception403;
import com.tenco.blog_v2.common.errors.Exception404;
import com.tenco.blog_v2.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final BoardJPARepository boardJPARepository;
    private final ReplyJPARepository replyJPARepository;
    
    
    // 댓글 쓰기
    @Transactional
    public void saveReply(ReplyDTO.SaveDTO reDto, User sessionUser) {
        // 댓글 작성 시 게시글 존재 여부 반드시 확인
        Board board = boardJPARepository
                .findById(reDto.getBoardId())
                .orElseThrow(() -> new Exception404("없는 게시글에 댓글을 작성 못해요"));

        Reply reply = reDto.toEntity(sessionUser, board);
        replyJPARepository.save(reply);
    }
    
    // 댓글 삭제
    @Transactional
    public void deleteReply(Integer replyId, Integer sessionUserId, Integer boardId) {
        // 댓글 존재 여부 확인
        Reply reply = replyJPARepository
                .findById(replyId)
                .orElseThrow(() -> new Exception404("없는 댓글을 삭제 못해요"));
        
        // 권한 처리 확인
        if(!reply.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("댓글 삭제 권한이 없어요");
        }

        if(!reply.getBoard().getId().equals(boardId)) {
            throw new Exception403("해당 게시글의 댓글이 아닙니다");
        }

        replyJPARepository.deleteById(replyId);

    }
    
}
