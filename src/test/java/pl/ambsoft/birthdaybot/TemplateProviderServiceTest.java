package pl.ambsoft.birthdaybot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;
import pl.ambsoft.birthdaybot.service.PracownikService;
import pl.ambsoft.birthdaybot.service.SendinblueClientService;
import pl.ambsoft.birthdaybot.service.TemplateProviderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TemplateProviderServiceTest {
    private final Pracownik pracownik =
            new Pracownik(1L, "Jan", "Kowalski", LocalDate.of(1990, 1, 1), "test@o2.pl");
    @InjectMocks
    TemplateProviderService templateProviderService;
    @Mock
    PracownikService pracownikService;
    @Mock
    SendinblueClientService sendinblueClientService;


    @Test
    public void getTemplateById_shouldReturnTemplate() throws TemplateNotFoundException {
        //given
        when(pracownikService.getById(anyLong())).thenReturn(Optional.of(pracownik));
        when(sendinblueClientService.getHTMLTemplateByID(anyLong())).thenReturn("Test template with (IMIE) and (NAZWISKO)");


        //when
        String actual = templateProviderService.getTemplateById(1L, pracownik.getId());

        //then
        assertEquals("Test template with Jan and Kowalski", actual);
    }

    @Test
    public void getTemplateWithName_shouldReturnTemplate() throws TemplateNotFoundException {
        //given
        when(pracownikService.getById(anyLong())).thenReturn(Optional.of(pracownik));
        when(sendinblueClientService.getHTMLTemplateID()).thenReturn(List.of(1L));
        when(sendinblueClientService.getHTMLTemplateByID(1L)).thenReturn("Test template with (IMIE) and (NAZWISKO)");


        //when
        String actual = templateProviderService.getTemplateWithName(1L);

        //then
        assertEquals("Test template with Jan and Kowalski", actual);
    }

    @Test
    public void getSeasonTemplatesWithName_shouldReturnTemplate() throws TemplateNotFoundException {
        //given
        when(pracownikService.getById(anyLong())).thenReturn(Optional.of(pracownik));
        when(sendinblueClientService.getHTMLSeasonTemplateID()).thenReturn(List.of(1L));
        when(sendinblueClientService.getHTMLTemplateByID(1L)).thenReturn("Test template with (IMIE) and (NAZWISKO)");


        //when
        String actual = templateProviderService.getSeasonTemplateWithName(1L);

        //then
        assertEquals("Test template with Jan and Kowalski", actual);
    }


    @Test
    public void getTemplateById_shouldThrowException() throws TemplateNotFoundException {
        //given
        when(sendinblueClientService.getHTMLTemplateByID(anyLong())).thenThrow(TemplateNotFoundException.class);

        //then
        assertThrows(TemplateNotFoundException.class, () -> templateProviderService.getTemplateById(1L, 1L));
    }

    @Test
    public void getTemplateWithName_shouldThrowException() throws TemplateNotFoundException {
        //when
        when(sendinblueClientService.getHTMLTemplateID()).thenThrow(TemplateNotFoundException.class);


        //then
        assertThrows(TemplateNotFoundException.class, () -> templateProviderService.getTemplateWithName(1L));
    }

    @Test
    public void getSeasonTemplateWithName_shouldThrowException() throws TemplateNotFoundException {
        //when
        when(sendinblueClientService.getHTMLSeasonTemplateID()).thenThrow(TemplateNotFoundException.class);


        //then
        assertThrows(TemplateNotFoundException.class, () -> templateProviderService.getSeasonTemplateWithName(1L));
    }
}
