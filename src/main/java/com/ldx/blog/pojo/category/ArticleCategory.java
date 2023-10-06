package com.ldx.blog.pojo.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
* @author Uaena
* @TableName article_category
*/
@Getter
@Setter
@AllArgsConstructor
public class ArticleCategory implements Serializable {

    /**
    * 
    */
    private int articleId;
    /**
    * 
    */
    private long categoryId;

}
