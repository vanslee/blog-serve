package com.ldx.blog.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Uaena
 * @date 2023/8/1 21:42
 */
@Component
@Slf4j
public class EmailSenderService {
        @Resource
        private JavaMailSender emailSender;
//        @Bean
//        public JavaMailSenderImpl mailSender() {
//            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//            javaMailSender.setProtocol("SMTP");
//            javaMailSender.setHost("127.0.0.1");
//            javaMailSender.setPort(25);
//
//            return javaMailSender;
//        }

        /**
         * @param to      收件人
         * @param subject 邮箱标题
         * @param text    邮箱内容
         */
        public void sendEmail(String to, String subject, String text) {
            log.info("{} 发送邮件,主题为：{},邮件内容：{} ", to, subject, text);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("495234450@qq.com");
            mailMessage.setSentDate(new Date());
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            emailSender.send(mailMessage);
        }

}
