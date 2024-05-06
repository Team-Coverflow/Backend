package com.coverflow.global.util;

import com.coverflow.global.exception.GlobalException;
import com.vane.badwordfiltering.BadWordFiltering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class BadWordUtil {

    private static List<String> badWords;

    @Autowired
    public BadWordUtil(BadWordProperties badWordsConfig) {
        BadWordUtil.badWords = Arrays.asList(badWordsConfig.getBadWords().split(","));
    }

    public static void check(final String word) {
        BadWordFiltering badWordFiltering = new BadWordFiltering();
        List<String> removeWords = Arrays.asList("공지", "공지사항");
        badWordFiltering.removeAll(removeWords);
        badWordFiltering.addAll(badWords);

        if (badWordFiltering.check(word)) {
            throw new GlobalException.ExistBadwordException();
        }
    }
}
