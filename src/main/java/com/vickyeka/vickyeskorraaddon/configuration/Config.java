package com.vickyeka.vickyeskorraaddon.configuration;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.projectkorra.projectkorra.configuration.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;

public class Config extends com.projectkorra.projectkorra.configuration.ConfigManager {

    public static void configCheck(ConfigType type) {
        FileConfiguration config;
        if (type == ConfigType.LANGUAGE) {
            config = languageConfig.get();
            config.addDefault("VickyEsKA.Tempest.CanTempestGrief", true);
            config.addDefault("VickyEsKA.Tempest.ChatColor", "GOLD");
            presetConfig.save();
        }
    }
}

