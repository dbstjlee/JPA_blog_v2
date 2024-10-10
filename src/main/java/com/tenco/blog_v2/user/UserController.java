package com.tenco.blog_v2.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;
    private final HttpSession session;

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
        User updatedUser = userRepository.updateById(sessionUser.getId(), reqDTO.getPassword(), reqDTO.getEmail());

        // 세션 정보 동기화 처리
        session.setAttribute("sessionUser", updatedUser);

        return "redirect:/";
    }




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
        User user = userRepository.findById(sessionUser.getId());
        request.setAttribute("user", user);
        return "user/update-form"; // 템플릿 경로: user/join-Form.mustache
    }

    /**
     * 회원 가입 기능 요청
     * @param reqDto
     * @return
     */
    // 데이터 바인딩 처리
    @PostMapping("/join")
    public String join(@ModelAttribute(name = "joinDTO") UserDTO.JoinDTO reqDto) {

        userRepository.save(reqDto.toEntity());

        return "redirect:/login-form";
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
     * 자원에 요청은 GET 방식이지만 보안에 이유로 예외!
     * 로그인 처리 메서드
     * 요청 주소 POST : http://localhost:8080/login
     * @param dto
     * @param model
     * @return
     */
    @PostMapping("/login")
    public String login(UserDTO.LoginDTO dto, Model model) {
        try {
            User sessionUser = userRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/";
        } catch (Exception e){
            // 로그인 실패
            return "redirect:/login-form";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // 세션을 무효화(로그아웃)
        return "redirect:/";
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


}
