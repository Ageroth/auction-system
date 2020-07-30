package pl.lodz.p.it.auctionsystem.mok.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.auctionsystem.entities.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {
    
    private final JavaMailSender javaMailSender;
    private final MessageService messageService;
    @Value("${base-url}")
    private String baseUrl;
    @Value("${email}")
    private String email;
    
    @Autowired
    public MailService(JavaMailSender javaMailSender, MessageService messageService) {
        this.javaMailSender = javaMailSender;
        this.messageService = messageService;
    }
    
    private void sendMessage(String subject, String text, String to) {
        MimeMessage message = javaMailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public void sendAccountVerificationMail(User user) {
        final String subject = messageService.getMessage("verification.subject");
        final String url = "<a href=" + "\"" + baseUrl + "/verify_account?token=" + user.getActivationCode() +
                "\">Link</a>";
        final String text = messageService.getMessage("verification.text");
        final String to = user.getEmail();
        
        sendMessage(subject, text + "\n" + url, to);
    }
    
    public void sendResetPasswordMail(User user) {
        final String subject = messageService.getMessage("passwordReset.subject");
        final String url = "<a href=" + "\"" + baseUrl + "/reset-password?token=" + user.getResetPasswordCode() +
                "\">Link</a>";
        final String text = messageService.getMessage("passwordReset.text");
        final String to = user.getEmail();
        
        sendMessage(subject, text + "\n" + url, to);
    }
}