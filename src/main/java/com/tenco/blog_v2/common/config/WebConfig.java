package com.tenco.blog_v2.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Component // Ioc
@Configuration // 여러 개 내부 메서드에 Bean을 해야 할 때 사용(Bean 공장)
public class WebConfig implements WebMvcConfigurer {

    @Autowired // DI 처리
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    /**
     * 인터셉터를 등록하고 적용할 URL 패턴을 설정하는 메서드이다.
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/board/**", "/user/**", "/reply/**") // 인터셉터를 적용할 경로 패턴 설정
//                .excludePathPatterns("/public/**", "/login", "/logout"); // 인터셉터를 제외할 경로 패턴 설정 --> 상세보기(로그인 안 되어 있어도 볼 수 있게)
                .excludePathPatterns("/board/{id:\\d+}"); 
                // 인터셉터 적용해서 제외할 URL 패턴 적용
                // /board/1, /board/33 <-- 로그인 인터셉터에서 제외됨.
                // \d+ : 숫자 하나 이상을 의미하는 정규 표현식 패턴
                // TODO - 이 부분 인터셉터 적용 후 로그인 후 댓글 작성 부분에서 막힘


        // 관리자 인터셉터 적용
        registry.addInterceptor(adminInterceptor)
            .addPathPatterns("/admin/**"); // /admin/** 경로에만 관리자 인터셉터 적용}
    }
}
