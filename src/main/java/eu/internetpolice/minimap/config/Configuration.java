package eu.internetpolice.minimap.config;

import eu.internetpolice.minimap.XaeroMinimapPaper;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class Configuration {
    protected final XaeroMinimapPaper plugin;
    protected CommentedConfigurationNode config;

    public Configuration(XaeroMinimapPaper plugin) {
        this.plugin = plugin;
        this.config = loadConfiguration();
    }

    protected CommentedConfigurationNode loadConfiguration() {
        Path configPath = plugin.getDataFolder().toPath().resolve("config.yml");
        plugin.saveDefaultConfig();

        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .path(configPath)
            .build();

        try {
            return loader.load();
        } catch (IOException ex) {
            plugin.getSLF4JLogger().error("Failed to load configuration." , ex);
        }

        throw new RuntimeException("Failed to load configuration.");
    }

    public CommentedConfigurationNode getConfig() {
        return config;
    }
}
