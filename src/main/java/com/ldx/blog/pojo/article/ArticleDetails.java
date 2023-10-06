package com.ldx.blog.pojo.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
* @author Uaena
* @TableName article_details
*/
@Getter
@Setter
@AllArgsConstructor
public class ArticleDetails implements Serializable {

    /**
    * 
    */
    private Long id;
    /**
    * 
    */
    private String content;

}
