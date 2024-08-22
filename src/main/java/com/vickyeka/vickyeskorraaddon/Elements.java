package com.vickyeka.vickyeskorraaddon;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.Ability;
import net.md_5.bungee.api.ChatColor;

public class Elements extends Element{

    public static Element TEMPEST;
    public static com.projectkorra.projectkorra.Element.SubElement BLUE_TEMPEST;

    static {
        TEMPEST = new Element("Tempest", null, VickyEsKorraAddon.plugin) {
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

        BLUE_TEMPEST = new Element.SubElement("Blue Tempest", Elements.TEMPEST, Element.ElementType.BENDING) {
            final String prefix = "[Blue Tempest]";
            final ChatColor color = ChatColor.DARK_BLUE;

            @Override
            public ChatColor getColor() {
                return color == null ? ChatColor.DARK_BLUE : color;
            }

            @Override
            public String getPrefix() {
                return prefix;
            }

        };
    }

    public Elements(String name) {
        super(name);
    }
}
