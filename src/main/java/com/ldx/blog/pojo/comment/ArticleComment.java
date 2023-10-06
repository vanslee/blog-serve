package com.ldx.blog.pojo.comment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* @author Uaena
* @TableName article_comment
*/
@Data
public class ArticleComment implements Serializable {

    /**
    * 评论id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
    * 评论的文章id
    */
    private Long articleId;
    /**
    * 评论内容
    */
    private String content;
    /**
    * 评论时间
    */
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    /**
    * 是否删除
    */
    @TableField(fill =FieldFill.INSERT)
    private Boolean isDelete;
    /**
    * 用户id
    */
    private Long userId;
    /**
    * 评论点赞数
    */
    private Integer likes;
    /**
    * 根评论ID
    */
    private Long rootCommentId;
    /**
    * 回复评论id
    */
    private Long replyCommentId;
    /**
     * 属地
     */
    private String location;
    /**
    * 评论修改时间
    */
    @TableField(fill =FieldFill.INSERT_UPDATE)
    private Long updateTime;
    /**
    * 是否置顶
    */
    private Integer isTop;
    /**
    * 评论人昵称
    */
    private String userNick;
    /**
     * 评论人头像
     */
    @TableField(fill = FieldFill.INSERT)
    private String userAvatar;
    /**
     * 子评论列表
     */
    @TableField(exist = false)
    private List<ArticleComment> childrens;


}
