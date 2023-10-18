package org.example.prefs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Configurations {
    public static final Configurations Configs = new Configurations();
    private static final String DEFAULT_JSON_CONFIG_FILEPATH = "./src/main/resources/configurations.json";
    private Map<String, Object> PREFS;

    public Configurations() {
        this(DEFAULT_JSON_CONFIG_FILEPATH);
    }

    public Configurations(String filepath) {
        try {
            String join = String.join("\n", Files.readAllLines(Path.of(filepath)));
            PREFS = new Gson().fromJson(join, TypeToken.getParameterized(Map.class, String.class, Object.class).getType());
            PREFS.put(ConfigsNames.TEST_DB_LOCATION_URL.getName(), getConfigAsString(ConfigsNames.TEST_DB_URL).split(":")[2] + ".mv.db");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getConfigAsString(ConfigsNames key) {
        return PREFS.get(key.getName()).toString();
    }

}
