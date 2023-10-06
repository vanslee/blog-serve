package com.ldx.blog.pojo.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName comment_likes_record
 */
@TableName(value ="comment_likes_record")
@Data
public class CommentLikesRecord implements Serializable {
    private Integer id;

    private Long articleid;

    private Integer pid;

    private Integer likerid;

    private String likedate;

    private static final long serialVersionUID = 1L;
}