package com.vickyeka.vickyeskorraaddon;

// This is the main Tempest bending file..... There will be changes...... I think?

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.Ability;
import org.bukkit.entity.Player;

public class Tempest extends Element {

    public static final SubElement TEMPEST;
    private static final SubElement[] SUB_ELEMENTS;

    public Tempest() {
        super("MyNewElement");
    }

    static {
        TEMPEST = new SubElement("Tempest", FIRE);
        SUB_ELEMENTS = new SubElement[]{TEMPEST};
    }

}
