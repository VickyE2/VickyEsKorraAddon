package com.vickyeka.vickyeskorraaddon;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import net.md_5.bungee.api.ChatColor;

public class Elements {

    public static Element TEMPEST;

    public Elements() {
        TEMPEST = new Element("Tempest", Element.ElementType.BENDING, ProjectKorra.plugin) {
            final String prefix = "[Tempest]";
            final ChatColor color = ChatColor.GOLD;

            @Override
            public ChatColor getColor() {
                return color == null ? ChatColor.GOLD : color;
            }
            @Override
            public String getPrefix() {
                return prefix;
            }
        };
    }

    public Elements getElementName(){
        return Elements.this;
    }
}
