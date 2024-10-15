package com.tenco.blog_v2.board;

import com.tenco.blog_v2.common.errors.Exception404;
import com.tenco.blog_v2.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService; // BoardService 주입
    private final HttpSession session;

    /**
     * 게시글 수정 폼을 표시하는 메서드
     * 요청 주소: **GET http://localhost:8080/board/{id}/update-form**
     * 글 수정하기 페이지 요청 메서드
     *
     * @param id      수정할 게시글의 ID
     * @param request HTTP 요청 객체
     * @return 게시글 수정 페이지 뷰
     */
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Integer id, HttpServletRequest request) {

        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 게시글 상세 조회 서비스 호출
        Board board = boardService.getBoardDetails(id, sessionUser);
        if (board == null) {
            throw new Exception404("게시글이 존재하지 않습니다");
        }

        // 조회한 게시글을 요청 속성에 추가
        request.setAttribute("board", board);
        // 수정 폼 템플릿 반환
        return "board/update-form";
    }

    /**
     * 게시글 수정 처리 메서드
     * 요청 주소: **POST http://localhost:8080/board/{id}/update**
     *
     * @param id        수정할 게시글의 ID
     * @param updateDTO 수정된 데이터를 담은 DTO
     * @return 게시글 상세보기 페이지로 리다이렉트
     */
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable(name = "id") Integer id, @ModelAttribute(name = "updateDTO") BoardDTO.UpdateDTO updateDTO) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 게시글 수정 서비스 호출
        boardService.updateBoard(id, sessionUser.getId(), updateDTO);

        // 수정 완료 후 게시글 상세보기 페이지로 리다이렉트
        return "redirect:/board/" + id;
    }


    /**
     * 게시글 삭제 처리 메서드
     * 요청 주소: **POST http://localhost:8080/board/{id}/delete**
     *
     * @param id 삭제할 게시글의 ID
     * @return 메인 페이지로 리다이렉트
     */
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인 페이지로 리다이렉트
        }

        // 게시글 삭제 서비스 호출
        boardService.deleteBoard(id, sessionUser.getId());

        // 메인 페이지로 리다이렉트
        return "redirect:/";
    }

    /**
     * 게시글 작성 폼을 표시하는 메서드
     * 요청 주소: **GET http://localhost:8080/board/save-form**
     *
     * @return 게시글 작성 페이지 뷰
     */
    @GetMapping("/board/save-form")
    public String saveForm() {
        // 게시글 작성 폼 템플릿 반환
        return "board/save-form";
    }


    /**
     * 게시글 작성 처리 메서드
     * 요청 주소: **POST http://localhost:8080/board/save**
     *
     * @param dto 게시글 작성 요청 DTO
     * @return 메인 페이지로 리다이렉트
     */
    @PostMapping("/board/save")
    public String save(@ModelAttribute BoardDTO.SaveDTO dto) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인 페이지로 리다이렉트
        }

        // 게시글 작성 서비스 호출
        boardService.createBoard(dto, sessionUser);

        // 메인 페이지로 리다이렉트
        return "redirect:/";
    }

    /**
     * 게시글 상세보기 처리 메서드
     * 요청 주소: **GET http://localhost:8080/board/{id}**
     *
     * @param id      게시글의 ID
     * @param request HTTP 요청 객체
     * @return 게시글 상세보기 페이지 뷰
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.getBoardDetails(id, sessionUser);

        // 현재 사용자가 게시글의 작성자인지 확인하여 isOwner 필드 설정
        boolean isOwner = false;
        if (sessionUser != null) {
            if (Objects.equals(sessionUser.getId(), board.getUser().getId())) {
                isOwner = true;
            }
        }

        // 뷰에 데이터 전달
        request.setAttribute("isOwner", isOwner);
        request.setAttribute("board", board);
        return "board/detail";
    }


    /**
     * 메인 페이지를 표시하는 메서드
     * 요청 주소: **GET http://localhost:8080/**
     *
     * @param model 뷰에 전달할 모델 객체
     * @return 메인 페이지 뷰
     */
    @GetMapping("/")
    public String index(Model model) {
        // 모든 게시글 조회 서비스 호출
        List<Board> boardList = boardService.getAllBoards();
        // 조회한 게시글 목록을 모델에 추가
        model.addAttribute("boardList", boardList);
        // 메인 페이지 템플릿 반환
        return "index";
    }

}