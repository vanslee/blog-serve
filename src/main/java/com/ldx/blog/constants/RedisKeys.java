package com.ldx.blog.constants;

/**
 * @author Uaena
 * @date 2023/7/31 19:50
 */
public  class RedisKeys {
    private static String PREFIX = "Blog:";
    public static String TEST_KEY = PREFIX.concat("Test");
    public static String LOCK = PREFIX.concat("LOCK:");
    public static final String  SIGN_IN_EMAIL =  PREFIX.concat("Sign-In:"); //asd
    public static final String ARTICLE_VIEW = PREFIX.concat("hasViewed:");
    public static final String ARTICLE_VIEW_COUNT = PREFIX.concat("viewCounts:");
    public static final String ARTICLE_LIKE = PREFIX.concat("hasLiked:");
    public static final String ARTICLE_LIKE_COUNT = PREFIX.concat("likeCounts:");
    public static final String ARTICLE_COMMENTS_COUNT = PREFIX.concat("commentsCounts:");
    public static final String ARTICLE_COLLECT_COUNT = PREFIX.concat("collectCounts:");
}
