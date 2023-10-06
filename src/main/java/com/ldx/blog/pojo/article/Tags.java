package com.ldx.blog.pojo.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
* @author Uaena
* @TableName tags
*/
@Getter
@Setter
@AllArgsConstructor
public class Tags {

    /**
    * 
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    *
    */
    private String name;
    /**
    * 
    */
    private Long createTime;
    /**
    * 
    */
    private Boolean isDelete;
    /**
    * 
    */
    private Integer categoryId;

    public Tags(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tags(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tags tags = (Tags) o;
        return name.equals(tags.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
