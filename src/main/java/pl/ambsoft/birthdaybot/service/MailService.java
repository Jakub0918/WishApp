package pl.ambsoft.birthdaybot.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.ambsoft.birthdaybot.error.BadMailRequestException;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;


@Service
public class MailService {

    private final TemplateProviderService templateService;
    private final JavaMailSender javaMailSender;
    private final String from;

    public MailService(
            @Autowired TemplateProviderService templateService,
            @Autowired JavaMailSender javaMailSender,
            @Value("${SPRING_MAIL_USERNAME}") final String from) {
        this.templateService = templateService;
        this.javaMailSender = javaMailSender;
        this.from = from;
    }


    public void sendMail(String to, String subject, Long templateId, Long pracownikId) throws TemplateNotFoundException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            String template = templateService.getTemplateById(templateId, pracownikId);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template: " + templateId, e);
        }
    }

    public void sendRandomMail(String to, String subject, Long pracownikId) throws TemplateNotFoundException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            String template = templateService.getTemplateWithName(pracownikId);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template", e);
        }
    }

    public void sendRandomSeasonMail(String to, String subject, Long pracownikId) throws TemplateNotFoundException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            String template = templateService.getSeasonTemplateWithName(pracownikId);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template", e);
        }
    }

    public void sendMailWithText(String to, String subject, String text) throws BadMailRequestException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, false);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BadMailRequestException("Błąd podczas wysyłania wiadomości");
        }
    }
}
