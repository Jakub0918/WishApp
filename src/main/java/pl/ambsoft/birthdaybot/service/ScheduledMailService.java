package pl.ambsoft.birthdaybot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ambsoft.birthdaybot.error.BadRequestException;

@Service
@EnableAsync
@EnableScheduling
public class ScheduledMailService {

    private final AutoMailService autoMailService;
    private final String sendMode;

    public ScheduledMailService(
            @Autowired AutoMailService autoSendMailService,
            @Value("${SEND_MODE: random}") String sendMode) {
        this.autoMailService = autoSendMailService;
        this.sendMode = sendMode;
    }

    @Async
    @Scheduled(cron = "0 0 ${HOUR} * * *")
    protected void autoSending() throws BadRequestException {
        autoMailService.sendReminder();
        if (sendMode.equals("random") || sendMode.equals("seasons")) {
            switch (sendMode) {
                case "random" -> autoMailService.sendRandomZyczenia();
                case "seasons" -> autoMailService.sendRandomSeasonZyczenia();
            }
        } else throw new BadRequestException("Błędna wartość zmiennej środowiskowej SEND_MODE");


    }
}
