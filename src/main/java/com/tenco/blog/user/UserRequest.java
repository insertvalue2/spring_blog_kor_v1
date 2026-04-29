package com.tenco.blog.user;

import lombok.Data;

public class UserRequest {

    // 회원가입 DTO
    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
    }

}
