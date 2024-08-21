package com.vickyeka.vickyeskorraaddon;

import com.vickyeka.vickyeskorraaddon.configuration.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

public class CoreAbility {
    public static FileConfiguration getConfig() {
        return ConfigManager.getConfig();
    }
}
