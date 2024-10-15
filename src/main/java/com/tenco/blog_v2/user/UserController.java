package com.tenco.blog_v2.user;

import com.tenco.blog_v2.common.errors.Exception401;
import com.tenco.blog_v2.common.errors.Exception500;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@RequiredArgsConstructor
@Slf4j
@Controller
public class UserController {
    //DI 처리
    // private final UserRepository userRepository; (제거 후)
    private final UserService userService; // (UserService로 접근하도록 함)
    private final HttpSession session;


    /**
     * 회원 정보 수정 페이지 요청
     * 주소 설계: http://localhost:8080/user/update-form
     *
     * @return 문자열
     * 반환되는 문자열을 뷰 리졸버 처리하며
     * 머스테치 템플릿 엔진을 통해서 뷰 파일을 렌더링 합니다.
     */
    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest request){
        log.info("회원가입 페이지");

        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null) {
            return "redirect:/login-form";
        }
        User user = userService.readUser(sessionUser.getId());
        request.setAttribute("user", user);
        return "user/update-form"; // 템플릿 경로: user/join-Form.mustache
    }


    /**
     * 사용자 정보 수정
     * @param reqDTO
     * @return 메인 페이지
     */
    // 데이터 바인딩 전략
    @PostMapping("/user/update")
    public String update(@ModelAttribute(name = "updateDTO") UserDTO.UpdateDTO reqDTO) {

        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 없다면 로그인 페이지 이동
        if(sessionUser == null) {
            return "redirect:/login-form";
        }
        // 유효성 검사 생략 X
        // 사용자 정보 수정(세션 아이디 사용)
        User updatedUser = userService.updateUser(sessionUser.getId(), reqDTO);

        // 세션 정보 동기화 처리
        session.setAttribute("sessionUser", updatedUser);

        return "redirect:/";
    }

    /**
     * 회원가입 페이지 요청
     * 주소 설계: http://localhost:8080/login-form
     *
     * @param model
     * @return
     * 반환되는 문자열을 뷰 리졸버 처리하며
     * 머스테치 템플릿 엔진을 통해서 뷰 파일을 렌더링 합니다.
     */
    @GetMapping("/join-form")
    public String joinForm(Model model){
        log.info("회원가입 페이지");
        model.addAttribute("name","회원가입 페이지");
        return "user/join-Form"; // 템플릿 경로: user/join-Form.mustache
    }

    /**
     * 로그인 페이지 요청
     * 주소 설계: http://localhost:8080/login-form
     *
     * @param model
     * @return
     * 반환되는 문자열을 뷰 리졸버 처리하며
     * 머스테치 템플릿 엔진을 통해서 뷰 파일을 렌더링 합니다.
     */
    @GetMapping("/login-form")
    public String loginForm(Model model){
        log.info("회원가입 페이지");
        model.addAttribute("name","로그인 페이지");
        return "user/login-Form"; // 템플릿 경로: user/join-Form.mustache
    }


    /**
     * 회원 가입 기능 요청
     * @param reqDto
     * @return
     */
    // 데이터 바인딩 처리
    @PostMapping("/join")
    public String join(@ModelAttribute(name = "joinDTO") UserDTO.JoinDTO reqDto) {

        // 유효성 검사 생략

        // 예외 처리
        try {
            userService.signUp(reqDto);
        } catch (DataIntegrityViolationException e) {
            throw new Exception500("동일한 유저 네임이 존재합니다");
        }
        return "redirect:/login-form";
    }


    /**
     * 자원에 요청은 GET 방식이지만 보안에 이유로 예외!
     * 로그인 처리 메서드
     * 요청 주소 POST : http://localhost:8080/login
     * @param dto
     * @return
     */
    @PostMapping("/login")
    public String login(UserDTO.LoginDTO dto) {
        try {
            User sessionUser = userService.signIn(dto);
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/";
        } catch (Exception e){
            // 로그인 실패
            throw new Exception401("유저 이름 또는 비밀번호가 틀렸습니다");
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // 세션을 무효화(로그아웃)
        return "redirect:/";
    }

}
