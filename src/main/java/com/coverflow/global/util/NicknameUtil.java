package com.coverflow.global.util;

import com.coverflow.member.domain.Member;
import com.coverflow.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NicknameUtil {

    public final MemberRepository memberRepository;

    public String generateRandomNickname() {
        List<String> firstName = Arrays.asList(
                "행복한", "슬픈", "게으른", "슬기로운", "수줍은", "아름다운", "귀여운", "멋진", "훌륭한", "행복한", // 10개
                "큰", "맛있는", "달콤한", "그리운", "더러운", "배고픈", "배부른", "화난", "빠른", "느린", // 20개
                "뜨거운", "차가운", "높은", "낮은", "부드러운", "딱딱한", "평화로운", "웃고있는", "깨발랄한", "활발한", // 30개
                "졸린", "적당한", "좋은", "어려운", "무거운", "가벼운", "싱싱한", "신선한", "건조한", "향기로운", // 40개
                "무취의", "강력한", "강인한", "쎈", "긴", "짧은", "빛나는", "투명한", "날카로운", "신기한",  // 50개
                "능숙한", "서툰", "유쾌한", "우울한", "재밌는", "친절한", "조용한", "적극적인", "튼튼한", "넓은", // 60개
                "깊은", "놀라운", "평범한", "섬세한", "거친", "똑똑한", "영리한", "자유로운", "어리석은", "두꺼운", // 70개
                "얇은", "성공적인", "웃기는", "시끄러운", "부드러운", "강한", "더운", "추운", "현명한", "시원한", // 80개
                "따뜻한", "풍족한", "희망찬", "무지한", "고요한", "편안한", "불편한", "웅장한", "조그마한", "창창한", // 90개
                "빈번한", "드물게", "창조적인", "보수적인", "진보적인", "밝은", "어두운", "포근한", "듬직한", "무서운", // 100개
                "건방진", "장난하는", "" // 110개
        );
        List<String> secondName = Arrays.asList(
                "치즈피자", "파스타", "스테이크", "샐러드", "불고기버거", "초밥", "카레", "치즈김밥", "만두", "라면", // 10개
                "볶음밥", "타코", "샌드위치", "치킨", "수프", "스시", "닭갈비", "갈비찜", "곱창", "떡볶이", // 20개
                "부대찌개", "치즈카츠", "삼겹살", "라멘", "김치찌개", "감자튀김", "생선구이", "불고기", "오므라이스", "카레라이스", // 30개
                "스파게티", "해물찜", "갈비탕", "새우튀김", "매운탕", "베이컨", "소시지", "콩나물국밥", "김치볶음밥", "해물파전", // 40개
                "샤브샤브", "훈제오리", "감자전", "삼계탕", "제육볶음", "누룽지", "동태찌개", "불닭볶음면", "잔치국수", "꼬치구이",  // 50개
                "새우볶음밥", "닭볶음탕", "냉면", "팟타이", "찜닭", "고등어조림", "해물탕", "쭈꾸미볶음", "소고기국밥", "버팔로윙", // 60개
                "라볶이", "떡국", "새우깡", "감자탕", "떡갈비", "부타동", "돼지불백", "김치전", "탕수육", "김치찜", // 70개
                "순두부찌개", "된장찌개", "닭꼬치", "치즈돈까스", "왕돈까스", "등심돈까스", "안심돈까스", "등심카츠", "안심카츠", "고구마튀김", // 80개
                "김말이", "쫄면", "참치김밥", "삼겹살김밥", "순대국밥", "육개장", "치즈케이크", "미역국", "수육국밥", "갈치조림", // 90개
                "대창전골", "곱창전골", "낙곱새", "대창", "모츠나베", "교동", "우동", "치즈버거", "불고기피자", "마라탕", // 100개
                "부채살", "목살", "오겹살", "흑임자호빵", "고구마호빵", "팥호빵", "피자호빵", "팥붕어빵", "슈붕어빵", "호떡" // 110개
        );
        String nickname;
        Optional<Member> member;

        do {
            Collections.shuffle(firstName);
            Collections.shuffle(secondName);

            nickname = firstName.get(0) + " " + secondName.get(0);
            System.out.println("Generated nickname = " + nickname);

            member = memberRepository.findByNickname(nickname);

        } while (member.isPresent()); // 해당 닉네임을 가진 회원이 존재하면 계속 반복

        System.out.println("Final nickname = " + nickname);
        return nickname; // 유일한 닉네임 반환
    }
}
