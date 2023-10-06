package com.ldx.blog.service.impl;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Uaena
 * @date 2023/6/18 2:00
 */
class QiniuUploadServiceImplTest {
    @Test
    public void test() {
        String fileName = "Spin-1s-200px.gif";
        String img_regex = "^.*\\.(?i:png|jpe?g|gif|bmp)$";
        Pattern pattern = Pattern.compile(img_regex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.matches()) {
            System.out.println("================");
        }
    }

}