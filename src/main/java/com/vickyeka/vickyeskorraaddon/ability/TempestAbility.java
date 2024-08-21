package com.vickyeka.vickyeskorraaddon.ability;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import com.vickyeka.vickyeskorraaddon.Elements;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public abstract class TempestAbility extends ElementalAbility implements AddonAbility {

    private static final Map<Block, Player> SOURCE_PLAYERS = new ConcurrentHashMap<>();

    private static final Set<BlockFace> IGNITE_FACES;
    public boolean isIgniteAbility() {
        return true;
    }

    public boolean isExplosiveAbility() {
        return false;
    }

    public static boolean canTempestGrief() {
        return getConfig().getBoolean("Properties.Tempest.TempestGriefing");
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

    public void playTempestParticles(Location loc, int amount, double xOffset, double yOffset, double zOffset) {
        ParticleEffect.FIREWORKS_SPARK.display(loc, amount, xOffset, yOffset, zOffset);
        ParticleEffect.CRIT.display(loc, amount, xOffset, yOffset, zOffset);
        ParticleEffect.END_ROD.display(loc, amount, xOffset, yOffset, zOffset);

    }

    public static void playTempestSound(Location loc) {
        if (com.projectkorra.projectkorra.configuration.ConfigManager.getConfig().getBoolean("Properties.Tempest.PlaySound")) {
            float volume = (float)com.projectkorra.projectkorra.configuration.ConfigManager.getConfig().getDouble("Properties.Tempest.TempestSound.Volume");
            float pitch = (float)com.projectkorra.projectkorra.configuration.ConfigManager.getConfig().getDouble("Properties.Tempest.TempestSound.Pitch");
            Sound sound = Sound.ITEM_TRIDENT_HIT_GROUND;

            try {
                sound = Sound.valueOf(com.projectkorra.projectkorra.configuration.ConfigManager.getConfig().getString("Properties.Tempest.TempestSound.Sound"));
            } catch (IllegalArgumentException var8) {
                ProjectKorra.log.warning("Your current value for 'Properties.Tempest.TempestSound.Sound' is not valid.");
            } finally {
                loc.getWorld().playSound(loc, sound, volume, pitch);
            }
        }

    }

    public void createTempFire(Location loc) {
        this.createTempFire(loc, getConfig().getLong("Properties.Tempest.RevertTicks") + (long)((new Random()).nextDouble() * (double)getConfig().getLong("Properties.Fire.RevertTicks")));
    }

    public void createTempFire(Location loc, long time) {
        if (isIgnitable(loc.getBlock())) {
            new TempBlock(loc.getBlock(), createFireState(loc.getBlock(), this.getTempestType() == Material.SOUL_FIRE), time);
            SOURCE_PLAYERS.put(loc.getBlock(), this.getPlayer());
        }

    }

    public static BlockData createFireState(Block position, boolean blue) {
        Fire fire = (Fire)Material.FIRE.createBlockData();
        if (isIgnitable(position) && position.getRelative(BlockFace.DOWN).getType().isSolid()) {
            return (BlockData)(blue ? Material.SOUL_FIRE.createBlockData() : fire);
        } else {
            Iterator var3 = IGNITE_FACES.iterator();

            while(var3.hasNext()) {
                BlockFace face = (BlockFace)var3.next();
                fire.setFace(face, false);
                if (isIgnitable(position.getRelative(face))) {
                    fire.setFace(face, true);
                }
            }

            return fire;
        }
    }

    public Material getTempestType() {
        return this.getBendingPlayer().canUseSubElement(Elements.BLUE_TEMPEST) ? Material.SOUL_FIRE : Material.FIRE;
    }

    @Override
    public void load(){

    }

    @Override
    public void stop(){

    }

}
