package com.tenco.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller // IoC
@RequiredArgsConstructor // DI 처리
public class UserController {
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    // 프로필 화면 요청
    // /user/update-form


    @GetMapping("/user/update-form")
    public String updateFormPage(HttpSession session, Model model) {
        // 인증 검사
        User sessionUser = (User)session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        User userEntity = userRepository.findById(sessionUser.getId());
        userEntity.setPassword("");

        // 가방에 데이터 담아서 화면에 값 내려 주기
        model.addAttribute("user", userEntity);
        return "user/update-form";
    }

    // 로그인 화면 요청
    // 주소 설계 - http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginFormPage() {
        return "user/login-form";
    }

    // 로그인 기능 요청
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO) {
        // 1. 유효성 검사
        loginDTO.validate();

        User sessionUser = userRepository
                .findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());

        if(sessionUser == null) {
            // 로그인 실패 (username, password) 불 일치
            throw new IllegalArgumentException("사용자명 또는 비밀번호가 잘못 되었습니다");
        }

        // 여기에 코드가 도달 한다면 우리 DB에 정상 사용자임을 논리적으로 확인 됨.
        httpSession.setAttribute("sessionUser", sessionUser);

        System.out.println("로그인 성공");
        System.out.println("로그인 사용자 : " + sessionUser.getUsername());
        System.out.println("로그인 이메일 : " + sessionUser.getEmail());

        return "redirect:/";
    }


    // 로그아웃 기능 요청

    @GetMapping("/logout")
    public String logout() {
        // 세션 메모리에 내 정보를 없애 버림
        // 로그 아웃
        httpSession.invalidate();

        return "redirect:/";
    }

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
        // 1. 유효성 검사 하기
        joinDTO.validate(); // 유효성 검사 ---> 오류 --> 예외 처리 넘어감
        // 회원가입 요청 전 ==> 중복 username 검사
        User userCheckName = userRepository.findByUsername(joinDTO.getUsername());

        if(userCheckName != null) {
            throw new IllegalArgumentException("이미 사용중인 username 입니다 : "
                    + userCheckName.getUsername());
        }
        userRepository.save(joinDTO.toEntity());
        // TODO
        // 로그인 화면으로 리다이렉트 처리 예정
        return "redirect:/";
    }

}
