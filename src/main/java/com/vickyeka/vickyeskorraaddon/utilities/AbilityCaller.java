package com.vickyeka.vickyeskorraaddon.utilities;

import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.Bukkit;

public class AbilityCaller {

    public static void Call(String calledabilityname) {

        // Retrieve the ability using the name
        CoreAbility ability = CoreAbility.getAbility(calledabilityname);
    // Check if the ability exists and log its details
        if (ability != null) {
            // Retrieve details of the ability
            String name = ability.getName();
            String element = ability.getElement().toString();

            // Log the details
            Bukkit.getLogger().fine("Ability Details:");
            Bukkit.getLogger().fine("Name: " + name);
            Bukkit.getLogger().fine("Element: " + element);
        } else {
            Bukkit.getLogger().severe("Ability with name '" + calledabilityname + "' not found.");
        }
    }
}
