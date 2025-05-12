package ru.nataliaoskina.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;
import lombok.extern.slf4j.Slf4j;
import ru.nataliaoskina.configuration.properties.AppProperties;

import javax.inject.Singleton;

@Module
@Slf4j
public class AppModule {

    @Provides
    @Singleton
    public AppProperties provideAppProperties(ObjectMapper mapper) {
        Config rawConfig = ConfigFactory.load().resolve();
        return mapper.convertValue(rawConfig.root().unwrapped(), AppProperties.class);
    }

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

}
