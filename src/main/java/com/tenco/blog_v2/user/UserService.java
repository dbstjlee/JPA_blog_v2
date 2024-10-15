package com.tenco.blog_v2.user;

import com.tenco.blog_v2.common.errors.Exception400;
import com.tenco.blog_v2.common.errors.Exception401;
import com.tenco.blog_v2.common.errors.Exception404;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service // IoC
public class UserService {

//    @Autowired
    private final UserJPARespository userJPARepository;

//    public UserService(UserJPARespository userJPARepository) {
//        this.userJPARepository = userJPARepository;
//    }

    // final 선언과 생성자 == @Autowired + @RequiredArgsConstructor --> 이게 성능 상 더 느림

    /**
     * 회원 가입 서비스
     */
    @Transactional
    public void signUp(UserDTO.JoinDTO reqDto) {
        // 1. username 기반 인데 unique 확인
        Optional<User> userOp = userJPARepository.findByUsername(reqDto.getUsername());
        // true = isPresent(값이 있으면)
        if (userOp.isPresent()) {
            throw new Exception400("중복된 유저 네임입니다");
        }

        // 회원가입
        userJPARepository.save(reqDto.toEntity());
    }

    /**
     * 로그인 서비스
     */
    public User signIn(UserDTO.LoginDTO reqDto) {
        User sessionUser = userJPARepository
                .findByUsernameAndPassword(reqDto.getUsername(), reqDto.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));
        // orElseThrow() : 없으면 던진다.(익명 클래스 - 람다)
        // () : 넘겨받을 인수값(없음)
        return sessionUser;
    }

    /**
     * 회원 정보 조회 서비스
     * 
     * @param id 조회할 사용자 ID
     * @return 조회될 사용자 객체
     * @throws Exception404 사용자를 찾을 수 없는 경우 발생
     */
    public User readUser(int id) {
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다"));
        return user;
    }

    /**
     * 회원 정보 수정 서비스
     *
     * @param id 수정할 사용자 ID
     * @param reqDto 수정된 사용자 정보 DTO
     * @return 수정된 사용자 객체
     * @throws Exception404 사용자를 찾을 수 없는 경우 발생
     */
    @Transactional
    public User updateUser(int id, UserDTO.UpdateDTO reqDto) {
        // 1. 사용자 조회 및 예외 처리
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다"));

        // 2. 사용자 정보 수정
        user.setPassword(reqDto.getPassword());
        user.setEmail(reqDto.getEmail());

        // 더티 체킹을 통해 변경 사항이 자동으로 반영된다.
        return user;
    }
}
