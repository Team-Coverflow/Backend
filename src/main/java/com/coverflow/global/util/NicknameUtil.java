package com.coverflow.global.util;

import com.coverflow.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class NicknameUtil {
    
    static MemberRepository memberRepository;

    public static String generateRandomNickname() {
        final AtomicBoolean result = new AtomicBoolean(false);
        String nickname = "";

        while (result.get()) {
            final List<String> firstName = Arrays.asList(
                    "행복한", "슬픈", "게으른", "슬기로운", "수줍은", "아름다운", "귀여운", "멋진", "훌륭한", "행복한", // 10개
                    "큰", "맛있는", "달콤한", "그리운", "더러운", "배고픈", "배부른", "화난", "빠른", "느린", // 20개
                    "뜨거운", "차가운", "높은", "낮은", "부드러운", "딱딱한", "평화로운", "웃고있는", "깨발랄한", "활발한", // 30개
                    "졸린", "적당한", "좋은", "어려운", "무거운", "가벼운", "싱싱한", "신선한", "건조한", "향기로운", // 40개
                    "무취의", "강력한", "강인한", "쎈", "긴", "짧은", "빛나는", "투명한", "날카로운", "신기한",  // 50개
                    "능숙한", "서툰", "유쾌한", "우울한", "재밌는", "친절한", "조용한", "적극적인", "튼튼한", "넓은", // 60개
                    "깊은", "놀라운", "평범한", "섬세한", "거친", "똑똑한", "영리한", "자유로운", "어리석은", "두꺼운", // 70개
                    "얇은", "성공적인", "웃기는", "시끄러운", "부드러운", "강한", "더운", "추운", "현명한", "시원한", // 80개
                    "따뜻한", "풍족한", "희망찬", "무지한", "고요한", "편안한", "불편한", "웅장한", "조그마한", "창창한", // 90개
                    "빈번한", "드물게", "창조적인", "보수적인", "진보적인", "밝은", "어두운", "포근한", "듬직한", "무서운" // 100개
            );
            final List<String> secondName = List.of();
            final List<String> thirdName = List.of();

            Collections.shuffle(firstName);
            Collections.shuffle(secondName);
            Collections.shuffle(thirdName);

            nickname = firstName.get(0) + " " + secondName.get(0) + " " + thirdName.get(0); // 100*100*100 = 1억개 가능

            memberRepository.findByNickname(nickname)
                    .ifPresentOrElse(
                            member -> result.set(false),
                            () -> result.set(true)
                    );
        }

        return nickname;
    }
}
