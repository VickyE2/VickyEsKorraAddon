package com.vickyeka.vickyeskorraaddon.configuration;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.vickyeka.vickyeskorraaddon.configuration.Config;
import com.projectkorra.projectkorra.configuration.ConfigType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.projectkorra.projectkorra.configuration.ConfigManager.configCheck;

public class ConfigManager extends com.projectkorra.projectkorra.configuration.ConfigManager {
    public static Config presetConfig;
    public static Config defaultConfig;
    public static Config languageConfig;

    public ConfigManager() {
        presetConfig = new Config(new File("presets.yml"));
        defaultConfig = new Config(new File("config.yml"));
        languageConfig = new Config(new File("language.yml"));
        configCheck(ConfigType.DEFAULT);
        configCheck(ConfigType.LANGUAGE);
        configCheck(ConfigType.PRESETS);
    }
    public static void configCheck(ConfigType type) {
        FileConfiguration config;
        if (type == ConfigType.PRESETS) {
            config = presetConfig.get();
            ArrayList<String> abilities = new ArrayList();
            abilities.add("TempestBeam");
            config.addDefault("Example", abilities);
            presetConfig.save();
        } else if (type == ConfigType.LANGUAGE) {
            config = languageConfig.get();
            config.addDefault("Properties.Tempest.TempestGriefing", false);
            config.addDefault("Properties.Tempest.PlaySound", true);
            config.addDefault("Properties.Tempest.TempestSound.Volume", 2.0);
            config.addDefault("Properties.Tempest.TempestSound.Pitch", 1.0);
            config.addDefault("Properties.Tempest.TempestSound.Sound", true);
            config.addDefault("Properties.Tempest.PlaySound", true);
            config.addDefault("Properties.Tempest.RevertTicks", 100);
        }

    }
}
