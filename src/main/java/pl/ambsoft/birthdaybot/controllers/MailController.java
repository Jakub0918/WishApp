package pl.ambsoft.birthdaybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.birthdaybot.error.BadRequestException;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;
import pl.ambsoft.birthdaybot.service.MailService;

@RestController
@RequestMapping("/sendMail")
public class MailController {
    @Autowired
    private MailService mailService;


    @GetMapping("/{to}/{templateId}/{pracownikID}")
    public ResponseEntity sendMail(@PathVariable("to") String to,
                                   @PathVariable("templateId") Long templateId,
                                   @PathVariable("pracownikID") Long pracownikID)
            throws Exception {
        try {
            String subject = "Testowa wiadomość";
            mailService.sendMail(to, subject, templateId, pracownikID);
            return new ResponseEntity(null, HttpStatus.resolve(200));
        } catch (TemplateNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Bad request");
        }
    }
}
