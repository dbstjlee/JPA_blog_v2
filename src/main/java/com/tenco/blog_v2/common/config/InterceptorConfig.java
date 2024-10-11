package com.tenco.blog_v2.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {

    // 인터셉터를 여기에 전부 만들기(@Bean)

    @Bean // 빈으로 등록 처리 : 로그인 인터셉터를 빈으로 등록
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(); // (@Configuration -> @Bean)스프링 컨테이너 안에 넣어줌 -> @Autowired 블러옴
    }

    // AdminInterceptor 등록


}
