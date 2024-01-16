package com.coverflow.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSignUpDTO {
    private String email;
    private String password;
    private String nickname;
    private int age;
}
