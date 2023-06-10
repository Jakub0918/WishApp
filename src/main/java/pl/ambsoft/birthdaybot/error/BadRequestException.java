package pl.ambsoft.birthdaybot.error;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }
}
