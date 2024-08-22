package com.vickyeka.vickyeskorraaddon.ability.tempestabilities;

import com.projectkorra.projectkorra.attribute.Attribute;
import com.vickyeka.vickyeskorraaddon.VickyEsKorraAddon;
import com.vickyeka.vickyeskorraaddon.abilityparents.TempestAbility;
import com.vickyeka.vickyeskorraaddon.listener.TempestBeamListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

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


    public TempestBeam(Player player, Location location) {
        super(player);
        if (this.bPlayer.isOnCooldown("Tempest_Beam")){
            Bukkit.getLogger().info("[VU-PKA] Ability On Cooldown");
        }
        if (!this.bPlayer.isOnCooldown("Tempest_Beam")) {
            Bukkit.getLogger().info("[VU-PKA] Tempest Beam Will Try Running");
            this.knockback = 1;
            this.speed = 2;
            this.damage = 4;
            this.range = 16;
            this.bPlayer.addCooldown(this);

            launchTempestBeam(player, location);

            start();
        }
    }

    public void launchTempestBeam(Player player, Location location) {

        Bukkit.getLogger().info("[VU-PKA] Tempest Beam Arrow Launcher called");
        arrow = player.getWorld().spawn(location, Arrow.class);
        arrow.setInvisible(true);
        arrow.setGravity(false);
        arrow.setKnockbackStrength(knockback);
        Vector direction = location.getDirection().normalize().multiply(speed); // Set the speed
        arrow.setVelocity(direction);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.61f, 0.69f);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_DEEPSLATE_BRICKS_FALL, 0.61f, 0.94f);
        arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.94f, 0.17f);

    }

    @Override
    public void progress() {
        origin = player.getLocation();
        Bukkit.getLogger().info("[VU-PKA] Progress method called");

        if (arrow == null || arrow.isDead()) {
            Bukkit.getLogger().warning("[VU-PKA] Arrow is null or dead, removing ability");
            remove();
            return;
        }

        if (origin.distance(arrow.getLocation()) > range) {
            Bukkit.getLogger().warning("[VU-PKA] Arrow exceeded range, removing ability");
            arrow.remove();
            remove();
            return;
        }

        // Additional logging for collision detection
        for (Entity entity : arrow.getNearbyEntities(0.5, 0.5, 0.5)) {
            Bukkit.getLogger().info("[VU-PKA] Checking for collisions");
            if (entity instanceof Player && entity != player) {
                Bukkit.getLogger().info("[VU-PKA] Collision detected with player, dealing damage and removing arrow");
                entity.getWorld().strikeLightning(entity.getLocation());
                ((Player) entity).damage(damage, player);
                arrow.remove();
                remove();
                return;
            }
        }

        Block hitBlock = arrow.getLocation().getBlock();
        if (hitBlock.getType().isSolid()) {
            Bukkit.getLogger().info("[VU-PKA] Arrow hit solid block, triggering explosion and lightning");
            if (TempestAbility.canTempestGrief()) {
                arrow.getWorld().createExplosion(arrow, 1);
            }
            arrow.getWorld().strikeLightning(hitBlock.getLocation());
            arrow.remove();
            remove();
        }
    }


    public void load(){
        Bukkit.getLogger().info("[VU-PKA] Loading Listener");
        listener = new TempestBeamListener();
        VickyEsKorraAddon.plugin.getServer().getPluginManager().registerEvents(listener, VickyEsKorraAddon.plugin);
    }
    public void stop(){
        Bukkit.getLogger().info("[VU-PKA] Stopping Ability");
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
        return 3;
    }
    @Override
    public String getName() {
        return "Tempest_Beam";
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
