package ru.nataliaoskina.di;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ReflectionMappers;
import org.jdbi.v3.core.mapper.reflect.SnakeCaseColumnNameMatcher;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.nataliaoskina.configuration.properties.AppProperties;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Module
@Slf4j
public class DatabaseModule {

    @Provides
    @Singleton
    public Jdbi create(HikariDataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.getConfig(ReflectionMappers.class).setColumnNameMatchers(List.of(new SnakeCaseColumnNameMatcher()));
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }

    @Provides
    @Singleton
    public HikariConfig provideHikariConfig(AppProperties appProperties) {
        AppProperties.Datasource datasourceProperties = appProperties.datasource();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(datasourceProperties.driver());
        hikariConfig.setJdbcUrl(datasourceProperties.url());
        hikariConfig.setUsername(datasourceProperties.username());
        hikariConfig.setPassword(datasourceProperties.password());

        AppProperties.Datasource.Pool pool = datasourceProperties.pool();
        hikariConfig.setMaximumPoolSize(pool.maximumPoolSize());
        hikariConfig.setMinimumIdle(pool.minimumIdle());
        hikariConfig.setIdleTimeout(pool.idleTimeoutMs());
        hikariConfig.setMaxLifetime(pool.maxLifetimeMs());
        return hikariConfig;
    }

    @Provides
    @Singleton
    public HikariDataSource provideHikariDataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public Connection provideConnection(AppProperties config) {
        AppProperties.Datasource datasource = config.datasource();
        try {
            return DriverManager.getConnection(
                    datasource.url(),
                    datasource.username(),
                    datasource.password()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Liquibase migration failed because failed db connection" ,e);
        }
    }

    @Provides
    @Singleton
    public Liquibase provideLiquibase(Connection connection, AppProperties properties) {
        if (properties.liquibase().enabled()) {
            log.info("Start liquibase init provider");
            try {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));
                ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();

                return new Liquibase(properties.liquibase().changeLog(), resourceAccessor, database);
            } catch (Exception e) {
                throw new RuntimeException("Liquibase migration failed", e);
            }
        }
        return null;
    }
}
