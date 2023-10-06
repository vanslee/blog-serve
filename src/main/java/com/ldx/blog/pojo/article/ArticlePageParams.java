package com.ldx.blog.pojo.article;

import lombok.Data;

@Data
public class ArticlePageParams {
    private int current;
    private int page;
    private int size;
    private String sort;
    private int total;
    private String uid;
    private String keyword;
}
