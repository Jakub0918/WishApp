package pl.ambsoft.birthdaybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ambsoft.birthdaybot.entity.Administrator;
import pl.ambsoft.birthdaybot.error.AdministratorNotFoundException;
import pl.ambsoft.birthdaybot.error.BadRequestException;
import pl.ambsoft.birthdaybot.error.OnlyOneAdministratorException;
import pl.ambsoft.birthdaybot.service.AdministratorService;

import java.util.Optional;


@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    @Autowired
    AdministratorService administratorService;

    @GetMapping("")
    public ResponseEntity getAll() throws Exception {
        try {
            return ResponseEntity.of(Optional.of(administratorService.getAll()));
        } catch (Exception e) {
            throw new AdministratorNotFoundException("Brak administratorów");
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllAdmin() throws Exception {
        try {
            return ResponseEntity.of(Optional.of(administratorService.getAllAdmin()));
        } catch (Exception e) {
            throw new AdministratorNotFoundException("Brak administratorów");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) throws Exception {
        try {
            return ResponseEntity.of(Optional.of(administratorService.getById(id)));
        } catch (Exception e) {
            throw new AdministratorNotFoundException("Nie odnaleziono administratora dla id: " + id);
        }
    }

    @PostMapping("")
    public ResponseEntity save(@RequestBody Administrator administrator) throws Exception {
        try {
            administratorService.save(administrator);
            return new ResponseEntity(administrator, HttpStatus.resolve(200));
        } catch (Exception e) {
            throw new BadRequestException("Błąd w trakcie dodawania administratora");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody Administrator administrator) throws Exception {
        try {
            administratorService.update(id, administrator);
            return new ResponseEntity(administrator, HttpStatus.resolve(200));
        } catch (Exception e) {
            throw new BadRequestException("Błąd w trakcie aktualizacji administratora lub brak id");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity patchUpdate(@PathVariable("id") Long id, @RequestBody Administrator updatedAdministrator) throws Exception {
        try {
            administratorService.update(id, updatedAdministrator);
            return new ResponseEntity(updatedAdministrator, HttpStatus.resolve(200));
        } catch (Exception e) {
            throw new BadRequestException("Błąd w trakcie aktualizacji administratora lub brak id");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception {
        try {
            administratorService.delete(id);
            return new ResponseEntity(null, HttpStatus.resolve(202));
        } catch (OnlyOneAdministratorException e) {
            throw new OnlyOneAdministratorException("Nie możesz usunąć jedynego administratora");
        } catch (Exception e) {
            throw new AdministratorNotFoundException("Nie odnaleziono administratora dla id: " + id);
        }
    }
}