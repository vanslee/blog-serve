package com.ldx.blog.pojo.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName comment_record
 */
@TableName(value ="comment_record")
@Data
public class CommentRecord implements Serializable {
    private Long id;

    private Long pid;

    private Long articleid;

    private Integer answererid;

    private Integer respondentid;

    private String commentdate;

    private Integer likes;

    private String commentcontent;

    private Integer isread;

    private static final long serialVersionUID = 1L;
}