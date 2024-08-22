package com.vickyeka.vickyeskorraaddon;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class VickyEsKorraAddon extends JavaPlugin {
    public static VickyEsKorraAddon plugin;


    @Override
    public void onEnable() {
        plugin = this;
        // Initialization code here I think
        getLogger().info("My plugin is geting hooked to korra >.<");
        CoreAbility.registerAddonAbilities("com.vickyeka.vickyeskorraaddon.ability.tempestabilities");
    }

    @Override
    public void onLoad(){
        getLogger().info("Locked In mah boi");
    }

    @Override
    public void onDisable() {
        // Cleanup code here
        getLogger().info("Well ig this is the end T-T");
    }

    public static VickyEsKorraAddon getInstance(){
        return plugin;
    }
}

