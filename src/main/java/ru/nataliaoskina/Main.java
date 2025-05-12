package ru.nataliaoskina;

import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.webhook.TelegramBotsWebhookApplication;
import ru.nataliaoskina.di.AppComponent;
import ru.nataliaoskina.di.DaggerAppComponent;

@Slf4j
public class Main {
    public static void main(String[] args) {
        AppComponent appComponent = DaggerAppComponent.create();

        try (Liquibase liquibase = appComponent.liquibase()) {
            liquibase.update("");
        } catch (LiquibaseException e) {
            throw new RuntimeException("Liquibase failed", e);
        }

        TelegramBotsWebhookApplication botApp = appComponent.getTelegramBotsWebhookApplication();
        try (botApp) {
            Thread.currentThread().join();
        } catch (TelegramApiException e) {
            log.error("Error registering bot", e);
            System.exit(1);
        } catch (RuntimeException e) {
            log.error("An unexpected error occurred: ", e);
            System.exit(1);
        } catch (InterruptedException e) {
            log.error("Main thread was interrupted", e);
            System.exit(1);
        }
    }
}
