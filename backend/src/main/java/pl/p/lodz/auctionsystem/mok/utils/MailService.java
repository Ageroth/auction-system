package pl.p.lodz.auctionsystem.mok.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.p.lodz.auctionsystem.entities.User;

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

    private void sendMessage(String subject, String text, String to) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(email);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        javaMailSender.send(message);
    }

    public void sendVerificationMail(User user) throws MessagingException {
        final String subject = messageService.getMessage("verification.subject");
        final String confirmationUrl = "<a href=" + "\"" + baseUrl + "/registrationConfirm?token=" + user.getActivationCode() + "\">Link</a>";
        final String text = messageService.getMessage("verification.text");
        final String to = user.getEmail();

        sendMessage(subject, text + "\n" + confirmationUrl, to);
    }
}