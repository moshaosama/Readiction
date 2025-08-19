package dev.bakr.readiction_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.username}")
    private String emailUsername;
    @Value("${spring.mail.password}")
    private String emailPassword;

    // About how you are going to go ahead and send a mail from spring to the reader's inbox.
    @Bean
    public JavaMailSender javaMailSender() {
        /* Create a new instance of the JavaMailSenderImpl class, which is the default implementation of Spring’s
        JavaMailSender interface. */
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set the Gmail's SMTP (Simple Mail Transfer Protocol) server as the host of mailSender for sending emails
        mailSender.setHost("smtp.gmail.com");
        /* Configure the SMTP port (587 is the default for STARTTLS with Gmail). STARTTLS is a command used to upgrade
        an unencrypted connection to a secure (TLS/SSL) connection, within the same port. */
        mailSender.setPort(587);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        /* configuring JavaMail-specific low-level properties on the email sender (your JavaMailSenderImpl) to tell it
        exactly how to behave when sending an email */
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
