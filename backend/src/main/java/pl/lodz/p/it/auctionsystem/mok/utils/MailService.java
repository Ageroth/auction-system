package pl.lodz.p.it.auctionsystem.mok.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.MailException;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Klasa służaca do obsługi wysyłania emaili.
 */
@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private final MessageService messageService;

    @Value("${base.frontend.url}")
    private String baseFrontendUrl;

    @Value("${email}")
    private String email;

    /**
     * Wysyła wiadomość o wskazanych parametrach.
     *
     * @param subject temat wiadomości
     * @param text    ciało wiadomości
     * @param to      odbiorca wiadomości
     */
    private void sendMessage(String subject, String text, String to) throws ApplicationException {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            String mailMessage = messageService.getMessage("exception.mailException");

            throw new MailException(mailMessage);
        }
    }

    /**
     * Wysyła email dotyczący aktywacji konta.
     *
     * @param user obiekt przechowujący dane
     */
    public void sendAccountActivationMail(User user) throws ApplicationException {
        final String subject = messageService.getMessage("email.subject.accountActivation");
        final String url = baseFrontendUrl + "/activation/" + user.getActivationCode();
        final String text = messageService.getMessage("email.text.accountActivation");
        final String to = user.getEmail();

        sendMessage(subject, text + "\n" + url, to);
    }

    /**
     * Wysyła email dotyczący resetu hasła.
     *
     * @param user obiekt przechowujący dane
     */
    public void sendPasswordResetEmail(User user) throws ApplicationException {
        final String subject = messageService.getMessage("email.subject.passwordReset");
        final String url = baseFrontendUrl + "/password_reset/" + user.getPasswordResetCode();
        final String text = messageService.getMessage("email.text.passwordReset");
        final String to = user.getEmail();

        sendMessage(subject, text + "\n" + url, to);
    }
}