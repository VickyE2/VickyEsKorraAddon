package com.vickyeka.vickyeskorraaddon.ability.tempestabilities;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.vickyeka.vickyeskorraaddon.ability.TempestAbility;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TempestBeamListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (bPlayer == null) return;

        if (bPlayer.canBend(CoreAbility.getAbility(TempestBeam.class))){
            new TempestBeam(player);
        }
    }


}
