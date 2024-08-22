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
            return;
        }
        Bukkit.getLogger().info("Event was a right click by listener TempestBeamListener.class");

        Action action = event.getAction();
        Bukkit.getLogger().info("Action: " + action);

        Bukkit.getLogger().info("[VU-PKA] Player Right Click Detected");
        final Player player = event.getPlayer();
        final BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        String currentAbility = bPlayer.getBoundAbilityName();

        if (bPlayer == null) {
            Bukkit.getLogger().info("[VU-PKA] There is no Bending Player");
            return;
        }

        if (currentAbility.equalsIgnoreCase("Tempest_Beam")) {
            Bukkit.getLogger().info("[VU-PKA] Skill is Tempest Beam");
            final TempestBeam tBeam = CoreAbility.getAbility(player, TempestBeam.class);
            if (tBeam != null) {
                Bukkit.getLogger().info("[VU-PKA] Got Parameters");
                tBeam.launchTempestBeam(player, event.getPlayer().getEyeLocation());
            }
        }
    }


}
