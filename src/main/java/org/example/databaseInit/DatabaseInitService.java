package org.example.databaseInit;

import org.example.prefs.ConfigsNames;
import org.example.prefs.Configurations;
import org.flywaydb.core.Flyway;



public class DatabaseInitService {

    public static void initDatabase(String dbURL) {
        Flyway flyway = Flyway.configure().dataSource(
                dbURL,
                Configurations.Configs.getConfigAsString(ConfigsNames.USER_NAME_CONFIG_NAME),
                Configurations.Configs.getConfigAsString(ConfigsNames.PASSWORD_CONFIG_NAME))
                .load();

        flyway.migrate();
    }

    public static void main(String[] args) {
        initDatabase(Configurations.Configs.getConfigAsString(ConfigsNames.DB_URL));
    }
}
