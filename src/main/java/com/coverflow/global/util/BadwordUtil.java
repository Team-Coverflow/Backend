package com.coverflow.global.util;

import com.coverflow.global.exception.GlobalException;
import com.vane.badwordfiltering.BadWordFiltering;
import org.springframework.stereotype.Component;

@Component
public class BadwordUtil {

    public static void check(final String word) {
        BadWordFiltering badWordFiltering = new BadWordFiltering();
        badWordFiltering.remove("공지");
        badWordFiltering.remove("공지사항");

        if (badWordFiltering.check(word)) {
            throw new GlobalException.ExistBadwordException();
        }
    }
}
