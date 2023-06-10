package pl.ambsoft.birthdaybot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.BadMailRequestException;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AutoMailService {
    private final String mail;
    private final String subject;
    private final PracownikService pracownikService;
    private final MailService mailService;


    public AutoMailService(
            @Value("${SPRING_MAIL_USERNAME}") String mail,
            @Value("${SUBJECT_ZYCZENIE: Wszystkiego najlepszego}") String subject,
            @Autowired PracownikService pracownikService,
            @Autowired MailService mailService) {
        this.mail = mail;
        this.subject = subject;
        this.pracownikService = pracownikService;
        this.mailService = mailService;
    }

    private List<Pracownik> birthdayIn7Days() {
        var pracownicy = pracownikService.getAll();
        var pracownicy1 = (StreamSupport.stream(pracownicy.spliterator(), false).toList());
        return pracownicy1.stream()
                .filter(pracownik -> {
                    var birthdayDate = pracownik.getData_urodzenia();
                    var date = LocalDate.of
                            (birthdayDate.getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()).plusDays(7L);
                    return date.isEqual(birthdayDate);
                })
                .collect(Collectors.toList());
    }

    private List<Pracownik> birthdayToday() {
        var pracownicy = pracownikService.getAll();
        var pracownicy1 = (StreamSupport.stream(pracownicy.spliterator(), false).toList());
        return pracownicy1.stream()
                .filter(pracownik -> {
                    var birthdayDate = pracownik.getData_urodzenia();
                    var date = LocalDate.of
                            (birthdayDate.getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
                    return date.isEqual(birthdayDate);
                })
                .collect(Collectors.toList());
    }

    public void sendReminder() {
        birthdayIn7Days().forEach(pracownik -> {
            String subject = pracownik.getImie() + " " + pracownik.getNazwisko() + " ma za 7 dni urodziny";
            String text = pracownik.getImie() + " " + pracownik.getNazwisko() + " obchodzi urodziny w dniu " +
                    pracownik.getData_urodzenia().getDayOfMonth() + "." + pracownik.getData_urodzenia().getMonthValue();
            try {
                mailService.sendMailWithText(mail, subject, text);
            } catch (BadMailRequestException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendRandomZyczenia() {
        birthdayToday().forEach(pracownik -> {
            try {
                mailService.sendRandomMail(pracownik.getEmail(), subject, pracownik.getId());
            } catch (TemplateNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendRandomSeasonZyczenia() {
        birthdayToday().forEach(pracownik -> {
            try {
                mailService.sendRandomSeasonMail(pracownik.getEmail(), subject, pracownik.getId());
            } catch (TemplateNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}



