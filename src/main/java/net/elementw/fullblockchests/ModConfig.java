package net.elementw.fullblockchests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ModConfig {
    public static ModConfig INSTANCE = new ModConfig();
    public static final String MOD_NAME = "Full Block Chests";
    public static final String MOD_ID = "fullblockchests";
    public static final Logger LOGGER = LoggerFactory.getLogger("FullBlockChests");

    public boolean disableBlockedCheck;
    public boolean reskinModdedChests;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private ModConfig() {
        Properties properties = new Properties();
        properties.setProperty("disableBlockedCheck", "true");
        properties.setProperty("reskinModdedChests", "true");
        File file = new File("config", "fullblockchests.properties");
        if (file.exists()) {
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                LOGGER.error("Unable to read config!", e);
            }
        } else {
            file.getParentFile().mkdirs();
            try {
                FileWriter writer = new FileWriter(file);
                properties.store(writer, "Full Block Chests config");
            } catch (IOException e) {
                LOGGER.error("Unable to write to config!", e);
            }
        }
        disableBlockedCheck = properties.getProperty("disableBlockedCheck", "false").equals("true");
        reskinModdedChests = properties.getProperty("reskinModdedChests", "false").equals("true");
    }
}
