package com.tenco.blog_v2.common;

import com.tenco.blog_v2.common.errors.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice // IoC 대상(뷰 렌더링) // 모든 에러 담당
// @ExceptionHandler // 데이터, 화면 중 화면으로 내릴 때 사용
public class GlobalExceptionHandler {

    /**
     * 400 Bad Request 예외 처리
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Exception400.class)
    public ModelAndView handleExcption400(Exception400 ex, Model model) {
        System.out.println("111111111111111");

        // templates/error/400.mustache
        ModelAndView mav = new ModelAndView("err/400");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 401  예외 처리
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Exception401.class)
    public ModelAndView handleExcption401(Exception401 ex, Model model) {
        ModelAndView mav = new ModelAndView("err/401");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 403 예외 처리
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Exception403.class)
    public ModelAndView handleExcption403(Exception403 ex, Model model) {
        ModelAndView mav = new ModelAndView("err/403");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 404 예외 처리
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Exception404.class)
    public ModelAndView handleExcption404(Exception404 ex, Model model) {
        ModelAndView mav = new ModelAndView("err/404");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 500  예외 처리
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Exception500.class)
    public ModelAndView handleExcption500(Exception500 ex, Model model) {
        ModelAndView mav = new ModelAndView("err/500");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }
}
