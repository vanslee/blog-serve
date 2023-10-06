package com.ldx.blog.pojo.oath.qq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Uaena
 * @date 2023/6/10 0:25
 */

@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Getter
@Setter
@ToString
public class QQAccessToken {
    private String accessToken;
    private String expiresIn;
    private String refreshToken;
}
