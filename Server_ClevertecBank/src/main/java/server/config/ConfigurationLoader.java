package server.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigurationLoader {
    private final String configFilePath = "Server_ClevertecBank/src/main/resources/application.yml";

    public Map<String, Object> loadConfig() throws IOException {
        try (InputStream input = new FileInputStream(configFilePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(input);
        }
    }
}
