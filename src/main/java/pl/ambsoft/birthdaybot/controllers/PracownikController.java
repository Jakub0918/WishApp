package pl.ambsoft.birthdaybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ambsoft.birthdaybot.entity.Pracownik;
import pl.ambsoft.birthdaybot.error.BadRequestException;
import pl.ambsoft.birthdaybot.error.PracownikAdminException;
import pl.ambsoft.birthdaybot.error.PracownikNotFoundException;
import pl.ambsoft.birthdaybot.service.PracownikService;

import java.util.Optional;


@RestController
@RequestMapping("/pracownik")
public class PracownikController {


    @Autowired
    PracownikService pracownikService;

    @GetMapping("")
    public ResponseEntity getAll() throws Exception {
        try {
            return ResponseEntity.of(Optional.of(pracownikService.getAll()));
        } catch (Exception e) {
            throw new PracownikNotFoundException("Brak pracowników");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) throws Exception {
        try {
            return ResponseEntity.of(Optional.of(pracownikService.getById(id)));
        } catch (Exception e) {
            throw new PracownikNotFoundException("Nie odnaleziono pracownika dla id: " + id);

        }
    }

    @PostMapping("")
    public ResponseEntity save(@RequestBody Pracownik pracownik) throws Exception {
        try {
            pracownikService.save(pracownik);
            return new ResponseEntity(pracownik, HttpStatus.resolve(200));
        } catch (Exception e) {
            throw new BadRequestException("Bład w trakcie dodawania pracownika");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody Pracownik pracownik) throws Exception {
        try {
            pracownikService.update(id, pracownik);
            return new ResponseEntity(pracownik, HttpStatus.resolve(200));
        } catch (Exception e) {
            throw new BadRequestException("Bład w trakcie aktualizacji pracownika lub brak id");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception {
        try {
            pracownikService.delete(id);
            return new ResponseEntity(null, HttpStatus.resolve(202));
        } catch (PracownikNotFoundException e) {
            throw new PracownikNotFoundException("Nie odnaleziono pracownika dla id: " + id);
        } catch (Exception e) {
            throw new PracownikAdminException("Ten pracownik jest administratorem, najpierw usuń jego uprawnienia");
        }
    }
}