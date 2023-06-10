package pl.ambsoft.birthdaybot.error;

import sendinblue.ApiException;

public class BadMailRequestException extends ApiException {

    public BadMailRequestException(String message) {
        super(message);
    }
}
