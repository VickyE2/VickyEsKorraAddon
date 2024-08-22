package com.vickyeka.vickyeskorraaddon.ability.tempestabilities;

import com.projectkorra.projectkorra.attribute.Attribute;
import com.vickyeka.vickyeskorraaddon.VickyEsKorraAddon;
import com.vickyeka.vickyeskorraaddon.ability.TempestAbility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class TempestBeam extends TempestAbility {
    private static final int MAX_TICKS = 10000;
    private boolean isTempestBeam = false;
    private boolean fireBurstIgnite;
    private int ticks;
    @Attribute("Cooldown")
    private long cooldown;
    @Attribute("Range")
    private double range;
    @Attribute("Damage")
    private double damage;
    @Attribute("Speed")
    private double speed;
    @Attribute("Knockback")
    private int knockback;
    private double flameRadius;
    private Random random;
    private Location location;
    private Location origin;
    private Vector direction;
    private List<Block> safeBlocks;
    private Arrow arrow;

    public TempestBeam(Player player) {
        super(player);
        if (!this.bPlayer.isOnCooldown("TempestBeam")) {
            launchTempestBeam(player);
            this.start();
            this.knockback = 1;
            this.speed = 2;
            this.damage = 4;
            this.cooldown = 3;
            this.range = 16;
            this.bPlayer.addCooldown(this, cooldown);
        }
    }

    public void launchTempestBeam(Player player) {
        Location location = player.getEyeLocation();
        arrow = player.getWorld().spawn(location, Arrow.class);
        arrow.setInvisible(true);
        arrow.setCustomName("TempestBeam");
        arrow.setCustomNameVisible(false);
        arrow.setGravity(false);
        arrow.setKnockbackStrength(knockback);
        Vector direction = location.getDirection().normalize().multiply(speed); // Set the speed
        arrow.setVelocity(direction);
    }

    @Override
    public void progress() {
        if (arrow == null || arrow.isDead()) {
            remove();
            return;
        }

        if (origin.distance(arrow.getLocation()) > range) {
            arrow.remove();
            remove();
            return;
        }

        // Play particle effect around the arrow
        new ParticleTask(arrow, 0, 0.2, 1, Particle.FIREWORKS_SPARK, Particle.CRIT_MAGIC, Particle.SOUL_FIRE_FLAME).runTask(VickyEsKorraAddon.plugin);

        // Play sound effect
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_EVOKER_PREPARE_ATTACK, 0.5f, 1f);

        // Check for collisions
        for (Entity entity : arrow.getNearbyEntities(0.5, 0.5, 0.5)) {
            if (entity instanceof Player && entity != player) {
                entity.getWorld().strikeLightning(entity.getLocation()); // Lightning damage
                ((Player) entity).damage(damage, player); // Apply damage
                arrow.remove(); // Remove the arrow
                remove(); // End the ability
                return;
            }
        }

        Block hitBlock = arrow.getLocation().getBlock();
        if (hitBlock.getType().isSolid()) {
            arrow.getWorld().strikeLightning(hitBlock.getLocation()); // Lightning damage on block hit
            arrow.remove();
            remove();
        }
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 5;
    }

    @Override
    public String getName() {
        return "TempestBeam";
    }

    @Override
    public Location getLocation() {
        return arrow != null ? arrow.getLocation() : null;
    }

    @Override
    public String getAuthor() {
        return "YourName";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
