package com.vickyeka.vickyeskorraaddon.utilities;

import com.projectkorra.projectkorra.Element;
import org.bukkit.Bukkit;

public class ElementChecker {
    public static void checkElementPresence() {
        // Retrieve the array of all registered addon elements
        Element[] elements = Element.getAddonElements();

        // Check if the custom element is in the array
        boolean Tempestfound = false;
        for (Element element : elements) {
            if (element.getName().equals("Tempest")) {
                Tempestfound = true;
                break;
            }
        }

        // Log the result
        if (Tempestfound) {
            Bukkit.getLogger().info("Element Tempest is registered and present.");
        } else {
            Bukkit.getLogger().warning("Element Tempest is NOT found in the registered elements. Could there have been an issue?");
        }
    }

    public static void listElementsPresent(){
        // Retrieve all registered addon elements
        Element[] elements = Element.getAddonElements();

        // Log all element names
        Bukkit.getLogger().info("All Registered Elements:");
        for (Element element : elements) {
            Bukkit.getLogger().info(" - " + element.getName());
        }
    }
}
