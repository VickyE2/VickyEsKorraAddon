package com.vickyeka.vickyeskorraaddon;

import com.projectkorra.projectkorra.Element;
import com.vickyeka.vickyeskorraaddon.utilities.Hexcolor;
import net.md_5.bungee.api.ChatColor;

public class Elements extends Element{

    public static Element TEMPEST;

    public static void initialize() {
        // Initialize TEMPEST only once
        if (TEMPEST == null) {
            TEMPEST = new Element("Tempest", ElementType.BENDING, VickyEsKorraAddon.plugin) {
                final String colorString = "#0000FF,#4D4DFF,#8C8CFF,#B3B3FF,#FFFFFF,#FFB3B3,#FF8C8C,#FF6666,#FF4D4D,#FF0000";
                final String prefix = Hexcolor.getHexGradientPrefix("[TEMPEST]", colorString);
                @Override
                public ChatColor getColor() {
                    return color == null ? ChatColor.DARK_AQUA : color;
                }
                @Override
                public String getPrefix() {
                    return prefix;
                }
            };
            VickyEsKorraAddon.plugin.getLogger().info("Tempest element initialized with prefix: " + TEMPEST.getPrefix());
        }
    }

    // Private constructor to prevent instantiation

    public Elements(String name) {
        super(name);
    }
}
