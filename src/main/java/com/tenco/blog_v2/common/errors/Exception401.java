package com.tenco.blog_v2.common.errors;

public class Exception401 extends RuntimeException {

    // 생성자 필요
    // throw new Exception400("잘못 던졌습니다."); <-- 사용하는 시점에 호출 모습
    public Exception401(String msg) {
        super(msg);

    }


}
