package com.vickyeka.vickyeskorraaddon.configuration;

import com.projectkorra.projectkorra.configuration.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {
    private static Config vupkaConfig;

    private static final ConfigType vupka_DEFAULT = new ConfigType("vupka_Default");

    public ConfigManager() {
        vupkaConfig = new Config(new File("VU-PKA_Config.yml"));
        configCheck(vupka_DEFAULT);
    }

    public static void configCheck(ConfigType type) {
        FileConfiguration config = vupkaConfig.get();

        config.addDefault("VickyEsKA.Tempest.CanTempestGrief", true);
        config.addDefault("VickyEsKA.Tempest.ChatColor", "GOLD");
        config.addDefault("debug.isEnabled", false);
        config.options().copyDefaults(true);
        vupkaConfig.save();
    }

    public static FileConfiguration readConfig() {
        return vupkaConfig.get();
    }

    public static void saveConfig() {
        vupkaConfig.save();
    }
}
