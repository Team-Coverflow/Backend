package com.coverflow.member.dto;

import com.coverflow.member.domain.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpDTO {

    private String email;
    private String nickname;
    private String tag;
    private String age;
    private String gender;
    private String socialId;
    private SocialType socialType;
}
