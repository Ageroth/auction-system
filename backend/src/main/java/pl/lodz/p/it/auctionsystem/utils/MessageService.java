package pl.lodz.p.it.auctionsystem.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Klasa obsługująca internacjonalizację wiadomości.
 */
@Component
public class MessageService {

    @Resource
    private MessageSource messageSource;

    /**
     * Internacjonalizuje wiadomość o podanym kodzie.
     *
     * @param code kod wiadomości
     * @return zinternacjonalizowana wiadomość
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}