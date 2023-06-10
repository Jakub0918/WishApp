package pl.ambsoft.birthdaybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ambsoft.birthdaybot.entity.Administrator;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.AdministratorNotFoundException;
import pl.ambsoft.birthdaybot.error.OnlyOneAdministratorException;
import pl.ambsoft.birthdaybot.repositories.AdministratorRepository;
import pl.ambsoft.birthdaybot.repositories.PracownikRepository;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final PracownikRepository pracownikRepository;

    public AdministratorService(
            @Autowired AdministratorRepository administratorRepository,
            @Autowired PracownikRepository pracownikRepository) {
        this.administratorRepository = administratorRepository;
        this.pracownikRepository = pracownikRepository;
    }

    public ArrayList<Optional<Pracownik>> getAll() {
        ArrayList<Optional<Pracownik>> pracownikList = new ArrayList<>();
        for (Administrator a : administratorRepository.findAll()) {
            pracownikList.add(pracownikRepository.findById(a.getPracownik_id()));
        }
        if (pracownikList.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return pracownikList;
        }

    }

    public Iterable<Administrator> getAllAdmin() {
        Iterable<Administrator> administrators = administratorRepository.findAll();
        if (StreamSupport.stream(administrators.spliterator(), false).findAny().isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return administratorRepository.findAll();
        }
    }

    public Optional<Pracownik> getById(Long id) {
        Optional<Administrator> administrator = administratorRepository.findById(id);
        Optional<Pracownik> pracownik = pracownikRepository.findById(administrator.get().getPracownik_id());

        if (pracownik.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return pracownik;
        }
    }

    public void save(Administrator administrator) throws Exception {
        Iterable<Administrator> iter = administratorRepository.findAll();
        List<Administrator> list = StreamSupport.stream(iter.spliterator(), false).
                filter(e -> e.getPracownik_id().equals(administrator.getPracownik_id()))
                .toList();
        if (list.isEmpty()) {
            administratorRepository.save(administrator);
        } else {
            throw new FileAlreadyExistsException("Taki administrator już istnieje");
        }
    }

    public void update(Long id, Administrator administrator) {
        Administrator existingAdministrator = administratorRepository.findById(id).orElse(null);
        existingAdministrator.setAdd_pracownik(administrator.isAdd_pracownik());
        existingAdministrator.setEdit_pracownik(administrator.isEdit_pracownik());
        existingAdministrator.setDelete_pracownik(administrator.isDelete_pracownik());
        existingAdministrator.setAdd_zyczenie(administrator.isAdd_zyczenie());
        existingAdministrator.setEdit_zyczenie(administrator.isEdit_zyczenie());
        existingAdministrator.setDelete_zyczenie(administrator.isDelete_zyczenie());
        existingAdministrator.setAdd_przypomnienie(administrator.isAdd_przypomnienie());
        existingAdministrator.setEdit_przypomnienie(administrator.isEdit_przypomnienie());
        existingAdministrator.setDelete_przypomnienie(administrator.isDelete_przypomnienie());

        administratorRepository.save(existingAdministrator);
    }

    public void delete(Long id) throws Exception {
        Iterable<Administrator> a = administratorRepository.findAll();
        Optional<Administrator> admin = administratorRepository.findById(id);
        if ((admin.isEmpty())) {
            throw new AdministratorNotFoundException("Nie odnaleziono administratora dla id: " + id);
        } else if ((StreamSupport.stream(a.spliterator(), false).count()) == 1) {
            throw new OnlyOneAdministratorException("Nie możesz usunąć jedynego administratora");
        } else {
            administratorRepository.deleteById(id);
        }
    }
}