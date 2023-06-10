package pl.ambsoft.birthdaybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ambsoft.birthdaybot.entity.Administrator;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.PracownikNotFoundException;
import pl.ambsoft.birthdaybot.repositories.AdministratorRepository;
import pl.ambsoft.birthdaybot.repositories.PracownikRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class PracownikService {

    private final PracownikRepository pracownikRepository;
    private final AdministratorRepository administratorRepository;

    public PracownikService(
            @Autowired PracownikRepository pracownikRepository,
            @Autowired AdministratorRepository administratorRepository) {
        this.pracownikRepository = pracownikRepository;
        this.administratorRepository = administratorRepository;
    }

    public Iterable<Pracownik> getAll() {
        Iterable<Pracownik> pracowniks = pracownikRepository.findAll();
        if ((StreamSupport.stream(pracowniks.spliterator(), false).count()) == 0) {
            throw new NoSuchElementException();
        } else {
            return pracownikRepository.findAll();
        }
    }

    public Optional<Pracownik> getById(Long id) throws NoSuchElementException {
        if (pracownikRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return pracownikRepository.findById(id);
        }
    }

    public void save(Pracownik pracownik) {
        pracownikRepository.save(pracownik);
    }

    public void update(Long id, Pracownik pracownik) {

        Pracownik existingPracownik = pracownikRepository.findById(id).orElse(null);
        existingPracownik.setImie(pracownik.getImie());
        existingPracownik.setNazwisko(pracownik.getNazwisko());
        existingPracownik.setData_urodzenia(pracownik.getData_urodzenia());
        existingPracownik.setEmail(pracownik.getEmail());
        pracownikRepository.save(existingPracownik);
    }

    public void delete(Long id) throws Exception {
        Optional<Pracownik> pracownik = pracownikRepository.findById(id);
        List<Administrator> admins = StreamSupport.stream(administratorRepository.findAll().spliterator(), false)
                .filter(e -> e.getPracownik_id().equals(id))
                .collect(Collectors.toList());
        if ((pracownik.isEmpty())) {
            throw new PracownikNotFoundException("Nie odnaleziono pracownika dla id: " + id);
        } else if (!admins.isEmpty()) {
            throw new Exception();
        } else {
            pracownikRepository.deleteById(id);
        }
    }
}