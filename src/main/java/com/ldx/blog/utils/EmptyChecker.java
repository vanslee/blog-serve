package com.ldx.blog.utils;

import java.util.Map;

/**
 * @author Uaena
 * @date 2023/6/3 21:17
 */
public class EmptyChecker {
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null) {
            return true;
        }
        for (Object value : map.values()) {
            if (value == null || "".equals(value)) {
                return false;
            }
        }
        return true;
    }
}
