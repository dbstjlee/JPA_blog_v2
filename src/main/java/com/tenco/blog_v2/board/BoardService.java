package com.tenco.blog_v2.board;

import com.tenco.blog_v2.common.errors.Exception403;
import com.tenco.blog_v2.common.errors.Exception404;
import com.tenco.blog_v2.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {


    private final BoardJPARepository boardJPARepository;


    /**
     * 
     * 게시글 생성
     * 
     * @param reqDto 게시글 작성 요청 DTO
     * @param sessionUser 현재 세션에 로그인한 사용자
     */
    @Transactional
    public void createBoard(BoardDTO.SaveDTO reqDto, User sessionUser){
        // 요청 DTO를 엔티티로 변환하여 저장
        boardJPARepository.save(reqDto.toEntity(sessionUser));
    }


    /**
     * 게시글 ID로 조회 서비스
     */
    public Board getBoard(int boardId) {
        return boardJPARepository
                .findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없어요"));
    }

    /**
     * 게시글 상세 보기 서비스, 게시글 주인 여부 판별
     */
    public Board getBoardDetails(int boardId, User sessionUser) {
      
        // 전략 2번
        // JPQL JOIN FETCH 사용, 즉 User 엔티티를 한 번에 조인 처리
        Board board = boardJPARepository
                .findByIdJoinUser(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        
        
//        //전략을 1번 JPA가 객체 간의 관계를 통해 직접 쿼리를 만들고 가지고 왔다.
//        Board board = boardJPARepository
//                .findById(boardId)
//                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없어요"));
                
        // 현재 사용자가 게시글을 작성 했는지 여부 판별
        boolean isBoardOwner = false;
        if(sessionUser != null) {
            if(sessionUser.getId().equals(board.getUser().getId())){
                isBoardOwner = true;
            }
        }
        
        
        // 코드 추가
        // 내가 작성한 댓글인가를 구현해야 한다.
        board.getReplies().forEach(reply -> {
            boolean isRelyOwner = false;
            if(sessionUser != null) {
                if(sessionUser.getId().equals(reply.getUser().getId())){
                    isRelyOwner = true; // 댓글의 주인임
                }
            }

            // 객체에서만 존재하는 필드 - 리플 객체 엔티티 상태값 변경 처리
            reply.setReplyOwner(isRelyOwner);
            
            
        });
        
        
        board.setBoardOwner(isBoardOwner); // 값 주입
        // board.isBoardOwner(); // T, F 확인
        return board;
    }

    /**
     * 게시글 삭제 서비스
     */
    public void deleteBoard(int boardId, int sessionUserId) {
        // 1.
        Board board = boardJPARepository
                .findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        // 2. 권한 처리 - 현재 사용자가 게시글 주인이 맞는가?
        if(sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글을 삭제할 권한이 없습니다");
        }

        // 3. 게시글 삭제 하기
        boardJPARepository.deleteById(boardId);
        
    }

    /**
     * 게시글 수정 서비스
     */
    @Transactional
    public void updateBoard(int boardId, int sessionUserId, BoardDTO.UpdateDTO reqDTO) {
        // 1. 게시글 존재 여부 확인
        Board board = boardJPARepository
                .findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        // 2. 수정 권환 확인 -  게시글 작성자가 맞는지
        if(sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글 수정 권한이 없습니다");
        }
        
        // 3. 게시글 수정
        board.setTitle(reqDTO.getTitle());
        board.setContent(reqDTO.getContent());

        // 더티 체킹 처리

    }

    /**
     * 모든 게시글 조회 서비스
     */
    public List<Board> getAllBoards() {
        // 게시글을 ID 기준으로 내림차순으로 정렬해서 조회해라
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return boardJPARepository.findAll(sort);
    }



}
