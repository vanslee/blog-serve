package com.ldx.blog.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Uaena
 * @date 2023/5/20 13:10
 */
public class StringUtil {
    public static final String BLANK = "";

    public static final int USERNAME_MAX_LENGTH = 35;

    /**
     * 文章点赞标志
     */
    public static final String ARTICLE_THUMBS_UP = "articleThumbsUp";

    public static final String VISITOR = "visitor:";

    public static boolean isEmpty(String str) {
        if (Objects.isNull(str)) {
            return true;
        }
        return str.isEmpty();
    }

    public static String arrToInClause(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "()";
        }
        String inClause = list.stream().map(String::valueOf).collect(Collectors.joining(", "));
        return "(" + inClause + ")";
    }

}
