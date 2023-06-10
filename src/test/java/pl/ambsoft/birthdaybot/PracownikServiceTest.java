package pl.ambsoft.birthdaybot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.repositories.PracownikRepository;
import pl.ambsoft.birthdaybot.service.PracownikService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PracownikServiceTest {

    private final Pracownik pracownik = new Pracownik(1L, "Jan", "Kowalski", null, "test@o2.pl");
    @InjectMocks
    PracownikService pracownikService;
    @Mock
    PracownikRepository pracownikRepository;

    @Test
    public void testGetAll_shouldReturnList() {
        //given
        when(pracownikRepository.findAll()).thenReturn(examplePracownicy());

        //when
        var actual = pracownikService.getAll();

        //then
        assertTrue(actual.iterator().hasNext());
        var actualList = StreamSupport.stream(actual.spliterator(), false).toList();
        assertEquals(1, actualList.size());
    }

    @Test
    public void testGetById_shouldReturnPracownik() {
        //given
        when(pracownikRepository.findById(1L)).thenReturn(Optional.of(pracownik));

        //when
        var actual = pracownikService.getById(1L);

        //then
        assertEquals(Optional.of(pracownik), actual);
    }

    @Test
    public void testUpdate_shouldReturnUpdatedPracownik() {
        //given
        when(pracownikRepository.save(pracownik)).thenReturn(pracownik);
        when(pracownikRepository.findById(1L)).thenReturn(Optional.of(pracownik));
        final Pracownik updatedPracownik = new Pracownik(1L, "Piotr", "Kowalski", null, "test@o2.pl");


        //when
        pracownikService.update(1L, updatedPracownik);
        var actual = pracownikService.getById(1L);

        //then
        assertEquals(Optional.of(updatedPracownik), actual);
    }

    @Test
    public void testGetAll_shouldReturnException() {
        //given
        when(pracownikRepository.findAll()).thenThrow(NoSuchElementException.class);

        //then
        assertThrows(NoSuchElementException.class, () -> pracownikService.getAll());
    }

    @Test
    public void testGetById_shouldReturnException() {
        //given
        when(pracownikRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        //then
        assertThrows(NoSuchElementException.class, () -> pracownikService.getById(1L));
    }

    private Iterable<Pracownik> examplePracownicy() {
        return List.of(
                new Pracownik(1L, "Jan", "Kowalski", null, "test@o2.pl"));
    }

}