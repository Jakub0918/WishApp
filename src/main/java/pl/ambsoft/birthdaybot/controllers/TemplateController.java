package pl.ambsoft.birthdaybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;
import pl.ambsoft.birthdaybot.service.TemplateProviderService;

import java.util.Optional;

@RestController
@RequestMapping("/template")
public class TemplateController<T> {

    @Autowired
    TemplateProviderService templateService;

    @GetMapping("/{id}/{pracownikID}")
    public ResponseEntity getTemplateHTML(@PathVariable("id") Long templateId,
                                          @PathVariable("pracownikID") Long pracownikID) throws Exception {
        try {
            return ResponseEntity.of(Optional.of(templateService.getTemplateById(templateId, pracownikID)));
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template: " + templateId, e);
        }
    }

    @GetMapping("/{pracownikID}")
    public ResponseEntity getAllTemplates(@PathVariable("pracownikID") Long pracownikID) throws TemplateNotFoundException {
        try {
            return ResponseEntity.of(Optional.of(templateService.getTemplateWithName(pracownikID)));
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template", e);
        }
    }

    @GetMapping("/seasons/{pracownikID}")
    public ResponseEntity getAllSeasonTemplates(@PathVariable("pracownikID") Long pracownikID) throws TemplateNotFoundException {
        try {
            return ResponseEntity.of(Optional.of(templateService.getSeasonTemplateWithName(pracownikID)));
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template", e);
        }
    }
}