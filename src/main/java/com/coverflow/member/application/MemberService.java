package com.coverflow.member.application;

import com.coverflow.member.dto.request.FindMemberAdminRequest;
import com.coverflow.member.dto.request.SaveMemberRequest;
import com.coverflow.member.dto.request.UpdateMemberRequest;
import com.coverflow.member.dto.response.FindAllMembersResponse;
import com.coverflow.member.dto.response.FindMemberResponse;
import com.coverflow.member.dto.response.UpdateNicknameResponse;

public interface MemberService {

    /**
     * [특정 회원 조회 메서드]
     */
    FindMemberResponse findMyMember(final String memberId);

    /**
     * [관리자 - 회원 조회 메서드]
     * 회원을 필터링해서 조회하는 메서드
     */
    FindAllMembersResponse find(
            final int pageNo,
            final String criterion,
            final FindMemberAdminRequest request
    );

    /**
     * [회원 추가 정보 등록 메서드]
     */
    void save(final String memberId, final SaveMemberRequest request);

    /**
     * [회원 정보 수정 메서드]
     */
    void update(final String memberId, final UpdateMemberRequest request);

    /**
     * [닉네임 변경 메서드]
     */
    UpdateNicknameResponse updateNickname(final String memberId);

    /**
     * [질문 작성 시 화폐 감소 메서드]
     * 질문 작성 시 화폐 10 감소
     */
    void writeQuestion(final String memberId, final int currency);

    /**
     * [로그아웃 메서드]
     */
    void logout(final String memberId);

    /**
     * [회원 탈퇴 메서드]
     * 30일 동안 유예 상태 및 GUEST 권한으로 전환(모든 데이터 보존)
     * 30일 이후에 탈퇴(모든 데이터 소멸)
     */
    void delete(final String memberId);
}
