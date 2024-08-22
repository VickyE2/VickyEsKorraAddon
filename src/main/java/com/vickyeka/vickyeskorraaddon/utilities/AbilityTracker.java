package com.vickyeka.vickyeskorraaddon.utilities;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.Bukkit;

public class AbilityTracker {
    public static void listAbilitiesPresentFor(Element element){
        // Retrieve all registered addon elements
        String[] abilityNames = CoreAbility.getAbilitiesByElement(element).stream().map(CoreAbility::getName).toArray(String[]::new);


        // Log all element names
        Bukkit.getLogger().info("All Registered Addon Abilities:");
        for (String abilityName : abilityNames) {
            Bukkit.getLogger().info(" - " + abilityName);
        }
    }
}
