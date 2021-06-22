package pl.lodz.p.it.auctionsystem.mok.utils;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGridAPI;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.MailException;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

import java.io.IOException;

/**
 * Klasa służaca do obsługi wysyłania emaili.
 */
@Component
@RequiredArgsConstructor
public class MailService {

    private final SendGridAPI sendGridAPI;

    private final MessageService messageService;

    @Value("${base.frontend.url}")
    private String baseFrontendUrl;

    @Value("${email}")
    private String email;

    @Value("${name}")
    private String name;

    /**
     * Wysyła wiadomość o wskazanych parametrach.
     *
     * @param subject temat wiadomości
     * @param text    ciało wiadomości
     * @param to      odbiorca wiadomości
     */
    private void sendMessage(String subject, String text, String to) throws ApplicationException {
        Email from = new Email(email, name);
        Content content = new Content("text/html", text);
        Mail mail = new Mail(from, subject, new Email(to), content);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sendGridAPI.api(request);

        } catch (IOException ex) {
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
        final String url = "<a href=" + baseFrontendUrl + "/activation/" + user.getActivationCode() + ">link</>";
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
        final String url = "<a href=" + baseFrontendUrl + "/password_reset/" + user.getPasswordResetCode() + ">link</>";
        final String text = messageService.getMessage("email.text.passwordReset");
        final String to = user.getEmail();

        sendMessage(subject, text + "\n" + url, to);
    }
}