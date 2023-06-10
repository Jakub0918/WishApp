package pl.ambsoft.birthdaybot.error;

import sendinblue.ApiException;

public class TemplateNotFoundException extends ApiException {

    public TemplateNotFoundException(String message, Exception e) {
        super(message);
        e.printStackTrace();
    }
}
