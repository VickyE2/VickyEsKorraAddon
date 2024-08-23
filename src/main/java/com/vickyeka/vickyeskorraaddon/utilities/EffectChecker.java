package com.vickyeka.vickyeskorraaddon.utilities;

import org.bukkit.Bukkit;

public class EffectChecker {

    public static void checkParticleEffectPresence(ParticleTypeEffect.ParticleTypeEffects effectToCheck) {
        // Get all enum values
        ParticleTypeEffect.ParticleTypeEffects[] effects = ParticleTypeEffect.ParticleTypeEffects.values();

        // Check if the specific effect is in the enum
        boolean effectFound = false;
        for (ParticleTypeEffect.ParticleTypeEffects effect : effects) {
            if (effect == effectToCheck) {
                effectFound = true;
                break;
            }
        }

        // Log the result
        if (effectFound) {
            Bukkit.getLogger().info("Particle effect " + effectToCheck.name() + " is registered and present.");
        } else {
            Bukkit.getLogger().warning("Particle effect " + effectToCheck.name() + " is NOT found in the registered effects. Could there have been an issue?");
        }
    }
    public static void listParticleEffectsPresent() {
        // Get all enum values
        ParticleTypeEffect.ParticleTypeEffects[] effects = ParticleTypeEffect.ParticleTypeEffects.values();

        // Log all enum values
        Bukkit.getLogger().info("All Particle Effects Present:");
        for (ParticleTypeEffect.ParticleTypeEffects effect : effects) {
            Bukkit.getLogger().info(" - " + effect.name());
        }
    }
}
