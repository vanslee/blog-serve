package com.ldx.blog.pojo.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
* @author Uaena
* @TableName article_tag
 *
*/
@Getter
@Setter
@AllArgsConstructor
public class ArticleTag implements Serializable {

    /**
    * 
    */
    private int articleId;
    /**
    * 
    */
    private Integer tagId;

}
