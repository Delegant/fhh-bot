package ru.nataliaoskina.fhhbot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import ru.nataliaoskina.listener.TelegramBotListener;

@SpringBootTest
class FhhBotApplicationTests {

    @Autowired
    ApplicationContext applicationContext;
    @MockBean
    TelegramBotListener telegramBotListener;

    @Test
    void contextLoads() {
        Assertions.assertThat(applicationContext).isNotNull();
    }
}
