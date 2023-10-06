package com.ldx.blog.pojo.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Uaena
 * @date 2023/8/1 22:44
 */
@Getter
@Setter
@AllArgsConstructor
public class EmailMessage {
    private String receiverEmail;
    private String verificationCode;
}
