package com.tenco.blog.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller // IoC
public class UserController {

    // 회원 가입 화면 요청
    // 주소 설계 - http://localhost:8080/join-form
    @GetMapping("/join-form")
    public String joinFormPage() {

        return "user/join-form";
    }

    // 회원 가입 기능 요청
    // 주소 설계 - http://localhost:8080/join
    // 파싱 전략 1 - key=value 구조 (@RequestParam 사용)
    // 파싱 전략 2 - Object DTO 설계
    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {

        log.info("username " + joinDTO.getUsername());
        log.info("password " + joinDTO.getPassword());
        log.info("email " + joinDTO.getEmail());

        return "";
    }

}
