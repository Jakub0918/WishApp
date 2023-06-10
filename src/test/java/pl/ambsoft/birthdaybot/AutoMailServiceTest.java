package pl.ambsoft.birthdaybot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.BadMailRequestException;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;
import pl.ambsoft.birthdaybot.service.AutoMailService;
import pl.ambsoft.birthdaybot.service.MailService;
import pl.ambsoft.birthdaybot.service.PracownikService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoMailServiceTest {

    @InjectMocks
    AutoMailService autoMailService;
    @Mock
    PracownikService pracownikService;
    @Mock
    MailService mailService;


    @Test
    public void testSendReminder_shouldSendMail() throws BadMailRequestException {
        //given
        LocalDate date = LocalDate.of(1990, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()).plusDays(7L);
        List<Pracownik> pracownicy = List.of(
                new Pracownik(1L, "Jan", "Kowalski", date, null));
        String subject = "Jan" + " " + "Kowalski" + " ma za 7 dni urodziny";
        String text = "Jan" + " " + "Kowalski" + " obchodzi urodziny w dniu " +
                date.getDayOfMonth() + "." + date.getMonthValue();
        when(pracownikService.getAll()).thenReturn(pracownicy);

        //when
        autoMailService.sendReminder();

        //then
        verify(pracownikService, times(1)).getAll();
        verify(mailService, times(1)).sendMailWithText(null, subject, text);
    }

    public List<Pracownik> getPracownik() {
        LocalDate date = LocalDate.of(1990, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        return List.of(
                new Pracownik(1L, "Jan", "Kowalski", date, "test@o2.pl"));
    }

    @Test
    public void testSendRandomZyczenia_shouldSendMail() throws TemplateNotFoundException {
        //given

        when(pracownikService.getAll()).thenReturn(getPracownik());

        //when
        autoMailService.sendRandomZyczenia();

        //then
        verify(pracownikService, times(1)).getAll();
        verify(mailService, times(1)).sendRandomMail("test@o2.pl", null, 1L);
    }

    @Test
    public void testSendSeasonZyczenia_shouldSendMail() throws TemplateNotFoundException {
        //given
        when(pracownikService.getAll()).thenReturn(getPracownik());

        //when
        autoMailService.sendRandomSeasonZyczenia();

        //then
        verify(pracownikService, times(1)).getAll();
        verify(mailService, times(1)).sendRandomSeasonMail("test@o2.pl", null, 1L);
    }
}
