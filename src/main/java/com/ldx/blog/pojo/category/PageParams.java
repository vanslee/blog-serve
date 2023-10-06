package com.ldx.blog.pojo.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Uaena
 * @date 2023/7/19 20:30
 */
@Getter
@Setter
@NoArgsConstructor
public class PageParams {
    private int pageNo;
    private int pageSize;
    private int status;
    private String keyword;
    private boolean isDesc;
}
