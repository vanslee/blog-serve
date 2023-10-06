package com.ldx.blog.pojo.oath;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Uaena
 * @date 2023/6/3 22:33
 */
@Getter
@Setter
@ToString
public class GiteeAccessToken {
    private String error;
    private String errorDescription;
    private String accessToken;
    private String tokenType;
    private String expiresIn;
    private String refreshToken;
    private String scope;
    private String createdAt;
}
