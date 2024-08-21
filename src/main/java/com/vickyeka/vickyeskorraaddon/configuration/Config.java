package com.vickyeka.vickyeskorraaddon.configuration;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.projectkorra.projectkorra.ProjectKorra;
import java.io.File;

import com.vickyeka.vickyeskorraaddon.VickyEsPKA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    private final VickyEsPKA plugin;
    private final File file;
    private final FileConfiguration config;

    public Config(File file) {
        this.plugin = VickyEsPKA.plugin;
        this.file = new File(this.plugin.getDataFolder() + File.separator + file);
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.reload();
    }

    public void create() {
        if (!this.file.getParentFile().exists()) {
            try {
                this.file.getParentFile().mkdir();
                this.plugin.getLogger().info("Generating new directory for " + this.file.getName() + "!");
            } catch (Exception var3) {
                this.plugin.getLogger().info("Failed to generate directory!");
                var3.printStackTrace();
            }
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                this.plugin.getLogger().info("Generating new " + this.file.getName() + "!");
            } catch (Exception var2) {
                this.plugin.getLogger().info("Failed to generate " + this.file.getName() + "!");
                var2.printStackTrace();
            }
        }

    }

    public FileConfiguration get() {
        return this.config;
    }

    public void reload() {
        this.create();

        try {
            this.config.load(this.file);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void save() {
        try {
            this.config.options().copyDefaults(true);
            this.config.save(this.file);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}

