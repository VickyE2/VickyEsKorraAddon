package com.vickyeka.vickyeskorraaddon.ability.tempestabilities;

import com.projectkorra.projectkorra.firebending.FireBlast;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.avatar.AvatarState;
import com.projectkorra.projectkorra.command.Commands;
import com.projectkorra.projectkorra.firebending.FireBlastCharged;
import com.projectkorra.projectkorra.firebending.util.FireDamageTimer;
import com.projectkorra.projectkorra.region.RegionProtection;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.waterbending.plant.PlantRegrowth;
import com.vickyeka.vickyeskorraaddon.ability.TempestAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.block.Smoker;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TempestBeam extends TempestAbility {
        private static final int MAX_TICKS = 10000;
        @Attribute("PowerFurnace")
        private boolean powerFurnace;
        private boolean showParticles;
        private boolean dissipate;
        private boolean isTempestBeam = false;
        private boolean fireBurstIgnite;
        private int ticks;
        @Attribute("Cooldown")
        private long cooldown;
        private double speedFactor;
        @Attribute("Range")
        private double range;
        @Attribute("Damage")
        private double damage;
        @Attribute("Speed")
        private double speed;
        private double collisionRadius;
        @Attribute("FireTicks")
        private double fireTicks;
        @Attribute("Knockback")
        private double knockback;
        private double flameRadius;
        private Random random;
        private Location location;
        private Location origin;
        private Vector direction;
        private List<Block> safeBlocks;

        public TempestBeam(Location location, Vector direction, Player player, double damage, List<Block> safeBlocks) {
            super(player);
            if (!location.getBlock().isLiquid()) {
                this.setFields();
                this.safeBlocks = safeBlocks;
                this.damage = damage;
                this.location = location.clone();
                this.origin = location.clone();
                this.direction = direction.clone().normalize();
                this.start();
            }
        }

        public TempestBeam(Player player) {
            super(player);
            if (!this.bPlayer.isOnCooldown("TempestBeam")) {
                if (!player.getEyeLocation().getBlock().isLiquid() && !FireBlastCharged.isCharging(player)) {
                    this.setFields();
                    this.isTempestBeam = false;
                    this.safeBlocks = new ArrayList();
                    this.location = player.getEyeLocation();
                    this.origin = player.getEyeLocation();
                    this.direction = player.getEyeLocation().getDirection().normalize();
                    this.location = this.location.add(this.direction.clone());
                    this.start();
                    this.bPlayer.addCooldown("FireBlast", this.cooldown);
                }
            }
        }


    private void setFields() {
            this.isTempestBeam = true;
            this.powerFurnace = true;
            this.showParticles = true;
            this.fireBurstIgnite = true;
            this.dissipate = true;
            this.cooldown = 3;
            this.range = 20;
            this.speed = 2;
            this.collisionRadius = 2;
            this.fireTicks = 22;
            this.knockback = 2;
            this.flameRadius = 2;
            this.damage = 6;
            this.random = new Random();
        }

        private void advanceLocation() {
            if (this.isTempestBeam) {
                this.flameRadius += 0.06;
            }

            if (this.showParticles) {
                this.playTempestParticles(this.location, 6, this.flameRadius, this.flameRadius, this.flameRadius);
            }

            BlockIterator blocks = new BlockIterator(this.getLocation().getWorld(), this.location.toVector(), this.direction, 0.0, (int)Math.ceil(this.direction.clone().multiply(this.speedFactor).length()));

            while(blocks.hasNext() && this.checkLocation(blocks.next())) {
            }

            this.location.add(this.direction.clone().multiply(this.speedFactor));
            if (this.random.nextInt(4) == 0) {
                playTempestSound(this.location);
            }

        }

        public boolean checkLocation(Block block) {
            if (block.isLiquid()) {
                this.remove();
                return false;
            } else if (block.isPassable()) {
                return true;
            } else {
                if (block.getType() == Material.FURNACE && this.powerFurnace) {
                    Furnace furnace = (Furnace)block.getState();
                    furnace.setBurnTime((short)800);
                    furnace.update();
                } else if (block.getType() == Material.SMOKER && this.powerFurnace) {
                    Smoker smoker = (Smoker)block.getState();
                    smoker.setBurnTime((short)800);
                    smoker.update();
                } else if (block.getType() == Material.BLAST_FURNACE && this.powerFurnace) {
                    BlastFurnace blastF = (BlastFurnace)block.getState();
                    blastF.setBurnTime((short)800);
                    blastF.update();
                } else if (block instanceof Campfire) {
                    Campfire campfire = (Campfire)block.getBlockData();
                    if (!campfire.isLit() && (block.getType() != Material.SOUL_CAMPFIRE || this.bPlayer.canUseSubElement(Element.SubElement.BLUE_FIRE))) {
                        campfire.setLit(true);
                    }
                } else if (isIgnitable(this.location.getBlock()) && (!this.isTempestBeam || this.fireBurstIgnite)) {
                    this.ignite(this.location);
                }

                this.remove();
                return false;
            }
        }

        private void affect(Entity entity) {
            if (entity.getUniqueId() != this.player.getUniqueId() && !RegionProtection.isRegionProtected(this, entity.getLocation()) && (!(entity instanceof Player) || !Commands.invincible.contains(((Player)entity).getName()))) {
                if (this.bPlayer.isAvatarState()) {
                    GeneralMethods.setVelocity(this, entity, this.direction.clone().multiply(AvatarState.getValue(this.knockback)));
                } else {
                    GeneralMethods.setVelocity(this, entity, this.direction.clone().multiply(this.knockback));
                }

                if (entity instanceof LivingEntity) {
                    entity.setFireTicks((int)(this.fireTicks * 20.0));
                    DamageHandler.damageEntity(entity, this.damage, this);
                    AirAbility.breakBreathbendingHold(entity);
                    new FireDamageTimer(entity, this.player, this);
                    this.remove();
                }
            }

        }

        private void ignite(Location location) {
            Iterator var2 = GeneralMethods.getBlocksAroundPoint(location, this.collisionRadius).iterator();

            while(true) {
                Block block;
                do {
                    do {
                        do {
                            if (!var2.hasNext()) {
                                return;
                            }

                            block = (Block)var2.next();
                        } while(!isIgnitable(block));
                    } while(this.safeBlocks.contains(block));
                } while(RegionProtection.isRegionProtected(this, block.getLocation()));

                if (canTempestGrief() && (isPlant(block) || isSnow(block))) {
                    block.setType(Material.AIR);
                    new PlantRegrowth(this.player, block);
                }

                this.createTempFire(block.getLocation());
            }
        }

        public void progress() {
            if (this.bPlayer.canBendIgnoreBindsCooldowns(this) && !RegionProtection.isRegionProtected(this, this.location)) {
                this.speedFactor = this.speed * ((double) ProjectKorra.time_step / 1000.0);
                ++this.ticks;
                if (this.ticks > 10000) {
                    this.remove();
                } else if (this.location.distanceSquared(this.origin) > this.range * this.range) {
                    this.remove();
                } else {
                    Entity entity = GeneralMethods.getClosestEntity(this.location, this.collisionRadius);
                    if (entity != null) {
                        this.affect(entity);
                    }

                    this.advanceLocation();
                }
            } else {
                this.remove();
            }
        }

        /** @deprecated */
        @Deprecated
        public static boolean annihilateBlasts(Location location, double radius, Player source) {
            boolean broke = false;
            Iterator var5 = getAbilities(com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam.class).iterator();

            while(var5.hasNext()) {
                com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam blast = (com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam)var5.next();
                Location fireBlastLocation = blast.location;
                if (location.getWorld().equals(fireBlastLocation.getWorld()) && !blast.player.equals(source) && location.distanceSquared(fireBlastLocation) <= radius * radius) {
                    blast.remove();
                    broke = true;
                }
            }

            if (FireBlastCharged.annihilateBlasts(location, radius, source)) {
                broke = true;
            }

            return broke;
        }

        public static ArrayList<com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam> getAroundPoint(Location location, double radius) {
            ArrayList<com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam> list = new ArrayList();
            Iterator var4 = getAbilities(com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam.class).iterator();

            while(var4.hasNext()) {
                com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam fireBlast = (com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam)var4.next();
                Location fireblastlocation = fireBlast.location;
                if (location.getWorld().equals(fireblastlocation.getWorld()) && location.distanceSquared(fireblastlocation) <= radius * radius) {
                    list.add(fireBlast);
                }
            }

            return list;
        }

        public static void removeTempestBeamsAroundPoint(Location location, double radius) {
            Iterator var3 = getAbilities(com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam.class).iterator();

            while(var3.hasNext()) {
                com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam fireBlast = (com.vickyeka.vickyeskorraaddon.ability.tempestabilities.TempestBeam)var3.next();
                Location fireBlastLocation = fireBlast.location;
                if (location.getWorld().equals(fireBlastLocation.getWorld()) && location.distanceSquared(fireBlastLocation) <= radius * radius) {
                    fireBlast.remove();
                }
            }

            FireBlastCharged.removeFireballsAroundPoint(location, radius);
        }

        public String getName() {
            return this.isTempestBeam ? "TempestBeam" : "TempestLaser";
        }

        public Location getLocation() {
            return this.location != null ? this.location : this.origin;
        }

        public long getCooldown() {
            return this.cooldown;
        }

        public boolean isSneakAbility() {
            return true;
        }

        public boolean isHarmlessAbility() {
            return false;
        }

        public double getCollisionRadius() {
            return this.collisionRadius;
        }

        public boolean isPowerFurnace() {
            return this.powerFurnace;
        }

        public void setPowerFurnace(boolean powerFurnace) {
            this.powerFurnace = powerFurnace;
        }

        public boolean isShowParticles() {
            return this.showParticles;
        }

        public void setShowParticles(boolean showParticles) {
            this.showParticles = showParticles;
        }

        public boolean isDissipate() {
            return this.dissipate;
        }

        public void setDissipate(boolean dissipate) {
            this.dissipate = dissipate;
        }

        public int getTicks() {
            return this.ticks;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }

        public double getSpeedFactor() {
            return this.speedFactor;
        }

        public void setSpeedFactor(double speedFactor) {
            this.speedFactor = speedFactor;
        }

        public double getRange() {
            return this.range;
        }

        public void setRange(double range) {
            this.range = range;
        }

        public double getDamage() {
            return this.damage;
        }

        public void setDamage(double damage) {
            this.damage = damage;
        }

        public double getSpeed() {
            return this.speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public void setCollisionRadius(double collisionRadius) {
            this.collisionRadius = collisionRadius;
        }

        public double getFireTicks() {
            return this.fireTicks;
        }

        public void setFireTicks(double fireTicks) {
            this.fireTicks = fireTicks;
        }

        public double getPushFactor() {
            return this.knockback;
        }

        public void setPushFactor(double pushFactor) {
            this.knockback = pushFactor;
        }

        public Random getRandom() {
            return this.random;
        }

        public void setRandom(Random random) {
            this.random = random;
        }

        public Location getOrigin() {
            return this.origin;
        }

        public void setOrigin(Location origin) {
            this.origin = origin;
        }

        public Vector getDirection() {
            return this.direction;
        }

        public void setDirection(Vector direction) {
            this.direction = direction;
        }

        public static int getMaxTicks() {
            return 10000;
        }

        public List<Block> getSafeBlocks() {
            return this.safeBlocks;
        }

        public void setCooldown(long cooldown) {
            this.cooldown = cooldown;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public boolean isFireBurst() {
            return this.isTempestBeam;
        }

        public void setFireBurst(boolean isFireBurst) {
            this.isTempestBeam = isFireBurst;
        }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }
}
