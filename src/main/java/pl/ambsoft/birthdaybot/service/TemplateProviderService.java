package pl.ambsoft.birthdaybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;

import java.util.List;
import java.util.Random;


@Service
public class TemplateProviderService {

    private final SendinblueClientService sendinblueClientService;
    private final PracownikService pracownikService;

    public TemplateProviderService(@Autowired SendinblueClientService sendinblueClientService,
                                   @Autowired PracownikService pracownikService) {
        this.sendinblueClientService = sendinblueClientService;
        this.pracownikService = pracownikService;
    }

    public String getTemplateById(Long templateId, Long pracownikID) throws TemplateNotFoundException {
        try {
            String template = sendinblueClientService.getHTMLTemplateByID(templateId);
            template = replaceTemplateVariables(template, pracownikID);
            return template;
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template: " + templateId, e);
        }
    }

    public String getTemplateWithName(Long pracownikID) throws TemplateNotFoundException {
        return getTemplateById(getRadnomLong(sendinblueClientService.getHTMLTemplateID()), pracownikID);
    }

    public String getSeasonTemplateWithName(Long pracownikID) throws TemplateNotFoundException {
        return getTemplateById(getRadnomLong(sendinblueClientService.getHTMLSeasonTemplateID()), pracownikID);
    }

    private String replaceTemplateVariables(String template, Long pracownikId) {
        String imie = pracownikService.getById(pracownikId).get().getImie();
        String nazwisko = pracownikService.getById(pracownikId).get().getNazwisko();
        template = template.replace("(IMIE)", imie);
        template = template.replace("(NAZWISKO)", nazwisko);
        return template;
    }

    private Long getRadnomLong(List<Long> longList) {
        Random random = new Random();
        return longList.get(random.nextInt(longList.size()));
    }
}

