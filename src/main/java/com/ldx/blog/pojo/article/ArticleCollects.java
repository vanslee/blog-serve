package com.ldx.blog.pojo.article;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Uaena
 * @date 2023/8/8 22:04
 */
@Getter
@Setter
public class ArticleCollects {
    private Long id;
    private boolean isDelete;
    @NotBlank(message = "文章ID不能为空")
    private String articleId;
    private String userId;
    private long create_time;

    public ArticleCollects(String articleId, String userId) {
        this.articleId = articleId;
        this.userId = userId;
    }
}
