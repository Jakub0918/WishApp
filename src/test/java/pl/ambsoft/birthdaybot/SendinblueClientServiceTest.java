package pl.ambsoft.birthdaybot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.ambsoft.birthdaybot.provider.TransactionalEmailsApiProvider;
import pl.ambsoft.birthdaybot.service.SendinblueClientService;
import sendinblue.ApiException;
import sibModel.GetSmtpTemplateOverview;
import sibModel.GetSmtpTemplates;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendinblueClientServiceTest {

    @InjectMocks
    SendinblueClientService sendinblueClientService;
    @Mock
    TransactionalEmailsApiProvider apiInstance;

    @Test
    public void getSeasonTemplateWithName_withLowerCase_shouldReturnTemplateList() throws ApiException {
        //given
        GetSmtpTemplateOverview template = new GetSmtpTemplateOverview();
        template.setId(1L);
        template.setSubject("spring");
        template.setIsActive(true);
        GetSmtpTemplates templates = new GetSmtpTemplates();
        templates.addTemplatesItem(template);
        when(apiInstance.getSmtpTemplates(true, 1000L, 0L, null))
                .thenReturn(templates);

        //when
        var actual = sendinblueClientService.getHTMLSeasonTemplateID();

        //then
        assertFalse(actual.isEmpty());
    }
}
