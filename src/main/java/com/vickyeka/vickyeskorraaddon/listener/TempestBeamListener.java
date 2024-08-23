package com.vickyeka.vickyeskorraaddon.listener;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TempestBeamListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            Bukkit.getLogger().info("[VU-PKA] Player Right Clicked Air");

            Player player = event.getPlayer();
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

            if (bPlayer == null) {
                Bukkit.getLogger().info("[VU-PKA] No Bending Player found");
                return;
            }

            String currentAbility = bPlayer.getBoundAbilityName();
            if (currentAbility == null || !currentAbility.equalsIgnoreCase("TempestBeam")) {
                Bukkit.getLogger().info("[VU-PKA] Tempest Beam is not the bound ability");
                return;
            }

            Bukkit.getLogger().info("[VU-PKA] Tempest Beam ability detected and being used");

            TempestBeam tBeam = new TempestBeam(player, player.getEyeLocation());
            if (tBeam != null) {
                Bukkit.getLogger().info("[VU-PKA] Tempest Beam ability created");
                // No need to manually start the ability; it should start on its own
            }
        }
        else{
            Bukkit.getLogger().info("[VU-PKA] Player didn't right click air");
        }
    }
}
