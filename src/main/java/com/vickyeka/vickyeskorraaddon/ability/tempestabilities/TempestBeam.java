package com.vickyeka.vickyeskorraaddon.ability.tempestabilities;

import com.projectkorra.projectkorra.attribute.Attribute;
import com.vickyeka.vickyeskorraaddon.VickyEsKorraAddon;
import com.vickyeka.vickyeskorraaddon.abilityparents.TempestAbility;
import com.vickyeka.vickyeskorraaddon.listener.TempestBeamListener;
import com.vickyeka.vickyeskorraaddon.utilities.ParticleTask;
import com.vickyeka.vickyeskorraaddon.utilities.ParticleTypeEffect;
import com.vickyeka.vickyeskorraaddon.utilities.SystemTimeGetter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Optional;

import static com.vickyeka.vickyeskorraaddon.configuration.ConfigManager.readConfig;

public class TempestBeam extends TempestAbility {
    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.KNOCKBACK)
    private int knockback;
    private Location origin;
    private Arrow arrow;
    private Listener listener;
    private long startTime;


    public TempestBeam(Player player, Location location) {
        super(player);
        if (readConfig().getBoolean("debug.isEnabled") == true){
                Bukkit.getLogger().info("[VU-PKA] Ability On Cooldown");
            }
        if (!this.bPlayer.isOnCooldown("TempestBeam")) {
            if (readConfig().getBoolean("debug.isEnabled") == true){
                Bukkit.getLogger().info("[VU-PKA] Tempest Beam Will Try Running");
            }
            this.knockback = 1;
            this.speed = 1.05;
            this.damage = 4;
            this.range = 16;
            this.bPlayer.addCooldown(this);
            this.startTime = SystemTimeGetter.getTime();

            launchTempestBeam(player, location);

            start();
        }
    }

    public void launchTempestBeam(Player player, Location location) {
        if (readConfig().getBoolean("debug.isEnabled") == true){
            Bukkit.getLogger().info("[VU-PKA] I the 'Tempest Beam Arrow Launcher' has been called upon ._ .");
        }
        arrow = player.getWorld().spawn(location, Arrow.class);
        arrow.setInvisible(true);
        arrow.setGravity(false);
        arrow.setKnockbackStrength(knockback);
        if (readConfig().getBoolean("debug.isEnabled") == true){
            Bukkit.getLogger().info("[VU-PKA] Currently Arrow Set to invisible is " + arrow.isInvisible());
        }
        Vector direction = location.getDirection().normalize().multiply(speed); // Set the speed
        arrow.setVelocity(direction);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.61f, 0.39f);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_DEEPSLATE_BRICKS_FALL, 0.61f, 0.54f);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.94f, 0.37f);
    }

    @Override
    public void progress() {
        origin = player.getLocation();
        if (readConfig().getBoolean("debug.isEnabled") == true){
            Bukkit.getLogger().info("[VU-PKA] Start Time is " + startTime);
            Bukkit.getLogger().info("[VU-PKA] Progress method called");
        }

        if (arrow == null || arrow.isDead()) {
            if (readConfig().getBoolean("debug.isEnabled") == true){
                Bukkit.getLogger().warning("[VU-PKA] Arrow is null or dead, removing ability");
            }
            remove();
            return;
        }

        if (origin.distance(arrow.getLocation()) > range) {
            if (readConfig().getBoolean("debug.isEnabled") == true){
                Bukkit.getLogger().warning("[VU-PKA] Arrow exceeded range, removing ability");
            }
            arrow.remove();
            remove();
            return;
        }

         new ParticleTask(startTime, arrow, 0.7, 0.5, 1.5,
                 Color.fromRGB(18, 120, 181),
                 Color.fromRGB(243, 153, 14),
                 Color.fromRGB(243, 37, 14),
                 20, 30,
                 0, 0, 0,
                 0, 0, 0,
                 0, 0,
                 2, 0,
                 0.63f, 0.3f,
                 Particle.REDSTONE, Particle.DUST_COLOR_TRANSITION,
                 ParticleTypeEffect.ParticleTypeEffects.HELIX, ParticleTypeEffect.ParticleTypeEffects.RIPPLES,
                 0, 0, 0, ParticleTypeEffect.SpacingMode.EXPONENTIAL, 3,
                 player.getYaw(), player.getPitch())
                 .runTask(VickyEsKorraAddon.plugin);

        for (Entity entity : arrow.getNearbyEntities(0.5, 0.5, 0.5)) {
            if (entity != player) {
                double entityDamage = (entity instanceof Player) ? damage : damage * 1.2; // 1.2x damage for mobs
                if (readConfig().getBoolean("debug.isEnabled") == true){
                    Bukkit.getLogger().info("[VU-PKA] Collision detected with entity, dealing damage and removing arrow");
                }
               if (entity instanceof Player) {
                    ((Player) entity).damage(entityDamage, player);
                } else if (entity instanceof LivingEntity) { // For mobs
                    ((LivingEntity) entity).damage(entityDamage, player);
                }

                // Additional effects for entities (optional)
                if (entity instanceof Player) {
                    entity.getWorld().strikeLightning(entity.getLocation()); // Lightning effect for players
                }

                arrow.remove();
                remove();
                return;
            }

            Location arrowLocation = arrow.getLocation();
            for (int i = 0; i < range; i++) {
                Block block = arrowLocation.getBlock();
                if (block.getType().isSolid()) {
                    if (readConfig().getBoolean("debug.isEnabled") == true){
                        Bukkit.getLogger().info("[VU-PKA] Arrow hit solid block, triggering explosion and lightning");
                    }
                    if (TempestAbility.canTempestGrief()) {
                        arrow.getWorld().createExplosion(arrow, 1);
                    }
                    arrow.getWorld().strikeLightning(block.getLocation());
                    arrow.remove();
                    remove();
                    return;
                }
                arrowLocation.add(arrow.getVelocity().normalize()); // Move to the next position
            }
        }
    }


    public void load(){
        if (readConfig().getBoolean("debug.isEnabled") == true){
            Bukkit.getLogger().info("[VU-PKA] Loading Listener");
        }
        listener = new TempestBeamListener();
        VickyEsKorraAddon.plugin.getServer().getPluginManager().registerEvents(listener, VickyEsKorraAddon.plugin);
    }
    public void stop(){
        if (readConfig().getBoolean("debug.isEnabled") == true){
            Bukkit.getLogger().info("[VU-PKA] Stopping Ability");
        }
        HandlerList.unregisterAll(listener);
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
        return 40;
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
        return "VickyE2";
    }
    @Override
    public String getVersion() {
        return "0.0.1";
    }
}
