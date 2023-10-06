package com.ldx.blog.utils;

import java.util.Random;

/**
 * @author Uaena
 * @date 2023/8/1 22:42
 */
public class GenerateRandCipherUtil {
        private static final int CODE_LENGTH = 6; // 验证码长度为6位
        public static String generateCode() {
            Random random = new Random();
            StringBuilder codeBuilder = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                int digit = random.nextInt(10); // 生成0到9之间的随机数
                codeBuilder.append(digit); // 将随机数添加到验证码中
            }
            return codeBuilder.toString();
        }
}
