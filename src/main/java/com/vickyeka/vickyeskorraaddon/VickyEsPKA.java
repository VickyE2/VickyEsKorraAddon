package com.vickyeka.vickyeskorraaddon;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import org.bukkit.plugin.java.JavaPlugin;

public class VickyEsPKA extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialization code here I think
        getLogger().info("My plugin is geting hooked to korra >.<");
        new Elements();
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
}

