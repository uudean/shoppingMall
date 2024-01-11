package com.soloproject.shoppingmall.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

   @Bean
    public JavaMailSender javaMailSender(){
       JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
       mailSender.setHost(host);
       mailSender.setPort(port);
       mailSender.setUsername(username);
       mailSender.setPassword(password);
       mailSender.setDefaultEncoding("UTF-8");

       Properties mailProperties = new Properties();
       mailProperties.put("mail.smtp.auth",auth);
       mailProperties.put("mail.smtp.starttls.enable",starttlsEnable);
       mailProperties.put("mail.smtp.starttls.required",starttlsRequired);
       mailProperties.put("mail.smtp.timeout",timeout);

       mailSender.setJavaMailProperties(mailProperties);

       return mailSender;
   }


}
