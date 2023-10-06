package com.ldx.blog.utils;

import java.util.Objects;

/**
 * @author Uaena
 */
public class TextUtil {
    public static boolean isNotEmpty(String str) {
        return !Objects.isNull(str) && str.length() > 0;
    }
}
