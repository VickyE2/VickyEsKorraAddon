package com.vickyeka.vickyeskorraaddon;

import com.projectkorra.projectkorra.ability.CoreAbility;
import com.vickyeka.vickyeskorraaddon.configuration.ConfigManager;
import com.vickyeka.vickyeskorraaddon.listener.TempestBeamListener;
import com.vickyeka.vickyeskorraaddon.utilities.AbilityCaller;
import com.vickyeka.vickyeskorraaddon.utilities.AbilityTracker;
import com.vickyeka.vickyeskorraaddon.utilities.ElementChecker;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class VickyEsKorraAddon extends JavaPlugin {
    public static VickyEsKorraAddon plugin;

    public static VickyEsKorraAddon getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        new ConfigManager();
        Elements.initialize();
        try {
            CoreAbility.registerPluginAbilities(this,"com.vickyeka.vickyeskorraaddon.ability.tempestabilities");
            getLogger().info(ChatColor.GREEN + "Abilities registered successfully.");

        } catch (Exception e) {
            getLogger().severe(ChatColor.DARK_RED + "Failed to register abilities: " + e.getMessage());
        }
        getLogger().info(ChatColor.YELLOW + "[VU-PKA] " + ChatColor.RESET + "My plugin is getting hooked to Korra >.<");

        this.getDescription().getPrefix();
        getServer().getPluginManager().registerEvents(new TempestBeamListener(), plugin);

        // Just to load the prefix if needed.
        getLogger().setFilter(record -> {
            record.setMessage(ChatColor.GREEN + "[VU-PKA] " + ChatColor.RESET + record.getMessage());
            return true;
        });

        AbilityCaller.Call("Tempest_Beam");

        // Check if Addon Elements and Abilities is loaded
        ElementChecker.checkElementPresence();
        ElementChecker.listElementsPresent();
        AbilityTracker.listAbilitiesPresentFor(Elements.TEMPEST);
    }

    @Override
    public void onLoad(){
        getLogger().warning(ChatColor.GREEN + "[VU-PKA] " + ChatColor.GREEN +"Locked In mah boi");
    }

    @Override
    public void onDisable() {
        // Cleanup code here
        getLogger().info(ChatColor.DARK_RED + "[VU-PKA] " + ChatColor.RESET +"Well ig this is the end T-T");
        getLogger().info(ChatColor.DARK_RED + "[VU-PKA] " + ChatColor.RESET +"Disabled VU_PKA");
    }

    public static VickyEsKorraAddon getInstance(){
        return plugin;
    }
}

