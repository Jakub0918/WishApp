package pl.ambsoft.birthdaybot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ambsoft.birthdaybot.entity.Administrator;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.OnlyOneAdministratorException;
import pl.ambsoft.birthdaybot.repositories.AdministratorRepository;
import pl.ambsoft.birthdaybot.repositories.PracownikRepository;
import pl.ambsoft.birthdaybot.service.AdministratorService;

import java.nio.file.FileAlreadyExistsException;
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
public class AdministratorServiceTest {


    private final Pracownik pracownik = new Pracownik(1L, "Jan", "Kowalski", null, "test@o2.pl");
    private final Administrator administrator = new Administrator(1L, 1L, true, true, true, true, true, true, true, true, true);

    @Mock
    PracownikRepository pracownikRepository;
    @InjectMocks
    AdministratorService administratorService;
    @Mock
    AdministratorRepository administratorRepository;

    @Test
    public void testGetAll_shouldReturnPracownikList() {
        //given
        when(administratorRepository.findAll()).thenReturn(exampleAdministratorzy());
        when(pracownikRepository.findById(1L)).thenReturn(Optional.of(pracownik));

        //when
        var actual = administratorService.getAll();

        //then
        assertTrue(actual.iterator().hasNext());
        var actualList = actual.stream().toList();
        assertEquals(1, actualList.size());
        assertTrue(actualList.contains(Optional.of(pracownik)));
    }

    @Test
    public void testGetAllAdmin_shouldReturnAdminList() {
        //given
        when(administratorRepository.findAll()).thenReturn(exampleAdministratorzy());

        //when
        var actual = administratorService.getAllAdmin();

        //then
        assertTrue(actual.iterator().hasNext());
        var actualList = StreamSupport.stream(actual.spliterator(), false).toList();
        assertEquals(1, actualList.size());
    }

    @Test
    public void testGetById_shouldReturnPracownik() {
        //given
        when(administratorRepository.findById(1L)).thenReturn(Optional.of(administrator));
        when(pracownikRepository.findById(1L)).thenReturn(Optional.of(pracownik));

        //when
        var actual = administratorService.getById(1L);

        //then
        assertEquals(Optional.of(pracownik), actual);
    }

    @Test
    public void testUpdate_shouldReturnUpdatedAdministrator() {
        //given
        when(administratorRepository.findById(1L)).thenReturn(Optional.of(administrator));
        when(administratorRepository.save(administrator)).thenReturn(administrator);
        final Administrator updatedAdministrator = new Administrator(1L, 1L, false, true, true, true, true, true, true, true, true);


        //when
        administratorService.update(1L, updatedAdministrator);
        var actual = administratorRepository.findById(1L);

        //then
        assertEquals(Optional.of(updatedAdministrator), actual);
    }

    @Test
    public void testGetAll_shouldReturnException() {
        //given
        when(administratorRepository.findAll()).thenThrow(NoSuchElementException.class);

        //then
        assertThrows(NoSuchElementException.class, () -> administratorService.getAll());
    }

    @Test
    public void testGetAllAdmin_shouldReturnException() {
        //given
        when(administratorRepository.findAll()).thenThrow(NoSuchElementException.class);

        //then
        assertThrows(NoSuchElementException.class, () -> administratorService.getAllAdmin());
    }

    @Test
    public void testGetById_shouldReturnException() {
        //given
        when(administratorRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        //then
        assertThrows(NoSuchElementException.class, () -> administratorService.getById(1L));
    }

    @Test
    public void testSave_shouldReturnException() {
        //given
        when(administratorRepository.findAll()).thenReturn(exampleAdministratorzy());

        //then
        assertThrows(FileAlreadyExistsException.class, () -> administratorService.save(administrator));
    }

    @Test
    public void testDelete_shouldReturnOnlyOneAdministratorException() {
        //given
        when(administratorRepository.findAll()).thenReturn(exampleAdministratorzy());
        when(administratorRepository.findById(anyLong())).thenReturn(Optional.of(administrator));

        //then
        assertThrows(OnlyOneAdministratorException.class, () -> administratorService.delete(1L));
    }

    private Iterable<Administrator> exampleAdministratorzy() {
        return List.of(
                new Administrator(1L, 1L, true, true, true, true, true, true, true, true, true));
    }
}