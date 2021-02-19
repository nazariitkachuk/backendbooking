package com.hotelapp.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;


@Service
@Validated
public class MailService {


    @NotNull
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public  String sendTemplatedEmail(String sendTo, String text,String subject){

        Context context = new Context();

        context.setVariable("maintext",text);
        context.setVariable("title","sample");


      String body=  templateEngine.process("reservation",context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,true);
            helper.setTo(sendTo);
            helper.setFrom(this.from);
            helper.setSubject(subject);
            helper.setText(body,true);
            
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        return "s";
    }

    @Async
    public String sendMail(String sendTo, String text, String from,String subject)  {


            SimpleMailMessage mailMessage= new SimpleMailMessage();

            mailMessage.setTo(sendTo);
            mailMessage.setText(text);
            mailMessage.setFrom(from);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
            System.out.println("DONE");


        return "Sent email";
    }


}
