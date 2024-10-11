package com.tenco.blog_v2.common.config;

import com.tenco.blog_v2.common.errors.Exception401;
import com.tenco.blog_v2.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// IoC를 안 한 상태이다.(스프링 서버가 실행될 때 메모리에 안 올림)
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 컨트롤러 메서드 호출 전에 실행되는 메서드이다.
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LoginIntercepter.preHandle 실행");
        // 로그인 여부 판단
        HttpSession session = request.getSession(false); // 기존 세션이 없다면 null 반환 한다.
        // 세션 객체 존재 여부 
        if(session != null) {
            throw new Exception401("로그인이 필요합니다");
        }

        // 키 - 값 -> 세션 메모리지의 저장 방식은 map 구조 저장(sessionUser) 뮨자열 사용 중
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null) {
            throw new Exception401("로그인이 필요합니다");
        }

        
        
        // return false <- 이면 컨트롤러로 들어가지 않는다.
        return true;
//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 컨트롤러 실행 후 뷰가 렌더링되기 전에 실행되는 메서드
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    /**
     * 뷰가 렌더링 된 후 실행되는 메서드
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
