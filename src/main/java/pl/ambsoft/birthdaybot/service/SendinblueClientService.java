package pl.ambsoft.birthdaybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.ambsoft.birthdaybot.enums.Season;
import pl.ambsoft.birthdaybot.error.NotActiveTemplateException;
import pl.ambsoft.birthdaybot.error.TemplateNotFoundException;
import pl.ambsoft.birthdaybot.provider.TransactionalEmailsApiProvider;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.GetSmtpTemplateOverview;

import java.time.LocalDate;
import java.util.List;

@Repository
public class SendinblueClientService {
    private final TransactionalEmailsApiProvider apiInstance;
    public String apiKey;

    public SendinblueClientService(@Value("${SENDINBLUE_API_KEY}") final String apiKey,
                                   @Autowired TransactionalEmailsApiProvider apiInstance) {
        this.apiKey = apiKey;
        this.apiInstance = apiInstance;
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey1 = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey1.setApiKey(apiKey);
    }

    public String getHTMLTemplateByID(Long templateId) throws TemplateNotFoundException {
        try {
            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
            GetSmtpTemplateOverview result = apiInstance.getSmtpTemplate(templateId);
            if (result.isIsActive()) {
                return result.getHtmlContent();
            } else throw new NotActiveTemplateException("Template: " + templateId + "is not active.");
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd przy pobieraniu template: " + templateId, e);
        }
    }

    public List<Long> getHTMLTemplateID() throws TemplateNotFoundException {
        List<Long> templateIDList;
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        try {
            templateIDList = apiInstance
                    .getSmtpTemplates(true, 1000L, 0L, null).getTemplates()
                    .stream()
                    .filter(el -> el.isIsActive())
                    .filter(el -> !el.getSubject().equals(Season.SPRING.toString()))
                    .filter(el -> !el.getSubject().equals(Season.SUMMER.toString()))
                    .filter(el -> !el.getSubject().equals(Season.AUTUMN.toString()))
                    .filter(el -> !el.getSubject().equals(Season.WINTER.toString()))
                    .map(GetSmtpTemplateOverview::getId)
                    .toList();
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd podczas pobierania template", e);
        }
        return templateIDList;
    }

    public List<Long> getHTMLSeasonTemplateID() throws TemplateNotFoundException {
        List<Long> templateIDList;
        try {
            templateIDList = apiInstance
                    .getSmtpTemplates(true, 1000L, 0L, null).getTemplates()
                    .stream()
                    .filter(el -> el.isIsActive())
                    .filter(el -> el.getSubject().equalsIgnoreCase(getCurrentSeason().toString()))
                    .map(GetSmtpTemplateOverview::getId)
                    .toList();
        } catch (Exception e) {
            throw new TemplateNotFoundException("Błąd podczas pobierania template", e);
        }
        return templateIDList;
    }

    private Boolean isInRange(LocalDate date, LocalDate after, LocalDate before) {
        return date.isAfter(after) && date.isBefore(before);
    }

    private Season getCurrentSeason() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        if (isInRange(now, LocalDate.of(year, 3, 20), LocalDate.of(year, 6, 21))) {
            return Season.SPRING;
        } else if (isInRange(now, LocalDate.of(year, 6, 21), LocalDate.of(year, 9, 22))) {
            return Season.SUMMER;
        } else if (isInRange(now, LocalDate.of(year, 9, 22), LocalDate.of(year, 12, 21))) {
            return Season.AUTUMN;
        } else return Season.WINTER;
    }
}

