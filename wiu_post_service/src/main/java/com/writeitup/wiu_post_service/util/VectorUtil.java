package com.writeitup.wiu_post_service.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class VectorUtil {

    public static String generateTsVector(final String title, final String content, final List<String> tags) {
        return String.format("%s %s %s", title, content, String.join(" ", tags));
    }
}
