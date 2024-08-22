package com.vickyeka.vickyeskorraaddon.ability;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import com.vickyeka.vickyeskorraaddon.Elements;

import com.vickyeka.vickyeskorraaddon.VickyEsKorraAddon;
import com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeamListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public abstract class TempestAbility extends ElementalAbility implements AddonAbility {


    private Listener listener;
    private static final Map<Block, Player> SOURCE_PLAYERS = new ConcurrentHashMap<>();

    private static final Set<BlockFace> IGNITE_FACES;
    public boolean isIgniteAbility() {
        return true;
    }

    public boolean isExplosiveAbility() {
        return false;
    }

    public static boolean canTempestGrief() {
        return getConfig().getBoolean("VickyEsKA.Tempest.CanTempestGrief");
}

    public TempestAbility(Player player){
        super(player);
    }

    public static boolean isIgnitable(Material material) {
        return material.isFlammable() || material.isBurnable();
    }

    public static boolean isIgnitable(Block block) {
        Block support = block.getRelative(BlockFace.DOWN);
        Location loc = support.getLocation();
        boolean supported = support.getBoundingBox().overlaps(loc.add(0.0, 0.8, 0.0).toVector(), loc.add(1.0, 1.0, 1.0).toVector());
        return !isWater(block) && !block.isLiquid() && GeneralMethods.isTransparent(block) && (supported && support.getType().isSolid() || IGNITE_FACES.stream().map((face) -> {
            return block.getRelative(face).getType();
        }).anyMatch(TempestAbility::isIgnitable));
    }

    public Element getElement() {
        return Elements.TEMPEST;
    }

    static {
        IGNITE_FACES = new HashSet(Arrays.asList(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP));
    }

    public static void stopBending() {
        SOURCE_PLAYERS.clear();
    }

    public static Map<Block, Player> getSourcePlayers() {
        return SOURCE_PLAYERS;
    }


    @Override
    public void load(){

        listener = new TempestBeamListener();
        VickyEsKorraAddon.plugin.getServer().getPluginManager().registerEvents(listener, VickyEsKorraAddon.plugin);
    }

    @Override
    public void stop(){

        HandlerList.unregisterAll(listener);
    }

}
