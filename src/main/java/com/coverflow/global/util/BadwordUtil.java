package com.coverflow.global.util;

import com.coverflow.global.exception.GlobalException;
import com.vane.badwordfiltering.BadWordFiltering;
import org.springframework.stereotype.Component;

@Component
public class BadwordUtil {

    public static void check(final String word) {
        if (new BadWordFiltering().check(word)) {
            throw new GlobalException.ExistBadwordException();
        }
    }
}
