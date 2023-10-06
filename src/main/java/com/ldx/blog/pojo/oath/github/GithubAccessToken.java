package com.ldx.blog.pojo.oath.github;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Uaena
 * @date 2023/6/10 2:04
 */
@ToString
@Getter
@Setter
public class GithubAccessToken {
    private String accessToken;
    private String scope;
    private String tokenType;
}
