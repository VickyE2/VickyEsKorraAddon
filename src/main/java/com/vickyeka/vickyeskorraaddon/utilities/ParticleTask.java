package com.vickyeka.vickyeskorraaddon.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static com.vickyeka.vickyeskorraaddon.configuration.ConfigManager.readConfig;
import static com.vickyeka.vickyeskorraaddon.utilities.EffectChecker.listParticleEffectsPresent;

public class ParticleTask extends BukkitRunnable {

    private final Arrow arrow;
    private final double radiusH;
    private final double radiusM;
    private final double heightStep;
    private final long startTime;
    private final Color headColor;
    private final Color transitionColorStart;
    private final Color transitionColorEnd;
    private final int headCount;
    private final int middleCount;
    private final double spreadXH;
    private final double spreadYH;
    private final double spreadZH;
    private final double spreadXM;
    private final double spreadYM;
    private final double spreadZM;
    private final float speedH;
    private final float speedM;
    private final long lagBehind;
    private final double backwardVelocity;
    private final double rFreq;
    private final double pFreq;
    private final double angleStep;
    private final float sizeH;
    private final float sizeM;
    private final ParticleTypeEffect.SpacingMode spacingMode;
    private final int circleNumber;
    private final Particle particleH;
    private final Particle particleM;
    private final ParticleTypeEffect.ParticleTypeEffects effectTypeH;
    private final ParticleTypeEffect.ParticleTypeEffects effectTypeM;
    private final float yaw;
    private final float pitch;

    public ParticleTask(long startTime, Arrow arrow, double radiusH, double radiusM, double heightStep, Color headColor, Color transitionColorStart, Color transitionColorEnd,
                        int headCount, int middleCount,
                        double spreadXH, double spreadYH, double spreadZH,
                        double spreadXM, double spreadYM, double spreadZM,
                        float speedH, float speedM,
                        long lagBehind, double backwardVelocity,
                        float sizeH, float sizeM,
                        Particle particleH, Particle particleM,
                        ParticleTypeEffect.ParticleTypeEffects effectTypeH, ParticleTypeEffect.ParticleTypeEffects effectTypeM,
                        double rFreq, double pFreq, double angleStep, ParticleTypeEffect.SpacingMode spacingMode, int circleNumber,
                        float yaw, float pitch) {

        this.arrow = arrow;
        this.radiusH = radiusH;
        this.radiusM = radiusM;
        this.heightStep = heightStep;
        this.headColor = headColor;
        this.transitionColorStart = transitionColorStart;
        this.transitionColorEnd = transitionColorEnd;
        this.headCount = headCount;
        this.middleCount = middleCount;
        this.spreadXH = spreadXH;
        this.spreadYH = spreadYH;
        this.spreadZH = spreadZH;
        this.spreadXM = spreadXM;
        this.spreadYM = spreadYM;
        this.spreadZM = spreadZM;
        this.speedH = speedH;
        this.speedM = speedM;
        this.sizeH = sizeH;
        this.sizeM = sizeM;
        this.spacingMode = spacingMode;
        this.circleNumber = circleNumber;
        this.particleH = particleH;
        this.particleM = particleM;
        this.lagBehind = lagBehind;
        this.rFreq = (rFreq == 0) ? 0.5 : rFreq; // Default to 1.0 if rFreq is 0
        this.pFreq = (pFreq == 0) ? 0.2 : pFreq; // Default to 1.0 if pFreq is 0
        this.angleStep = (angleStep == 0) ? 1.0 : angleStep; // Default to 1.0 if angleStep is 0
        this.backwardVelocity = backwardVelocity;
        this.startTime = startTime;
        this.effectTypeH = effectTypeH;
        this.effectTypeM = effectTypeM;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void run() {
        if (arrow.isDead()) {
            cancel();
            return;
        }

        Location loc = arrow.getLocation();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime; // Calculate the elapsed time
        double angle = (elapsedTime / 10.0);
        if (readConfig().getBoolean("debug.isEnabled") == true){
            Bukkit.getLogger().info("Current Angle is " + angle);
        }

        // Generate particle positions based on effect type
        Location[] headPositions;
        Location[] middlePositions;

        switch (effectTypeH) {
            case LINE:
                headPositions = ParticleTypeEffect.LINE(loc.clone(), radiusH, headCount);
                break;
            case HELIX:
                headPositions = ParticleTypeEffect.HELIX(loc.clone(), radiusH, heightStep, angle, headCount);
                break;
            case WAVY_LINE:
                headPositions = ParticleTypeEffect.WAVY_LINE(loc.clone(), radiusH, heightStep, angle, headCount);
                break;
            case BURST_SPIRAL:
                headPositions = ParticleTypeEffect.BURST_SPIRAL(loc.clone(), radiusH, heightStep, angle, headCount);
                break;
            case CONVERGING_LINES:
                headPositions = ParticleTypeEffect.CONVERGING_LINES(loc.clone(), radiusH, heightStep, angle, headCount);
                break;
            case RIPPLES:
                headPositions = ParticleTypeEffect.RIPPLES(loc.clone(), radiusH, angleStep, angle, headCount, spacingMode, circleNumber);
                break;
            case FALLING_LEAVES:
                headPositions = ParticleTypeEffect.FALLING_LEAVES(loc.clone(), radiusH, heightStep, angle, headCount);
                break;
            case EXPLODING_STARS:
                headPositions = ParticleTypeEffect.EXPLODING_STARS(loc.clone(), radiusH, heightStep, angle, headCount);
                break;
            case PULSE_WAVES:
                headPositions = ParticleTypeEffect.PULSE_WAVES(loc.clone(), radiusH, heightStep, angle, headCount, pFreq);
                break;
            case OSCILLATING_RINGS:
                headPositions = ParticleTypeEffect.OSCILLATING_RINGS(loc.clone(), radiusH, heightStep, angle, headCount, rFreq);
                break;
            default:
                if (readConfig().getBoolean("debug.isEnabled") == true){
                    Bukkit.getLogger().severe("[VU-PKA] You tried to use " + effectTypeH + " which currently doesn't exist");
                    listParticleEffectsPresent();
                }
                throw new IllegalStateException("Unexpected effect type: " + effectTypeH);

        }
        switch (effectTypeM) {
            case LINE:
                middlePositions = ParticleTypeEffect.LINE(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, middleCount);
                break;
            case HELIX:
                middlePositions = ParticleTypeEffect.HELIX(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount);
                break;
            case WAVY_LINE:
                middlePositions = ParticleTypeEffect.WAVY_LINE(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount);
                break;
            case BURST_SPIRAL:
                middlePositions = ParticleTypeEffect.BURST_SPIRAL(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount);
                break;
            case CONVERGING_LINES:
                middlePositions = ParticleTypeEffect.CONVERGING_LINES(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount);
                break;
            case RIPPLES:
                middlePositions = ParticleTypeEffect.RIPPLES(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, angleStep, angle, middleCount, spacingMode, circleNumber);
                break;
            case FALLING_LEAVES:
                middlePositions = ParticleTypeEffect.FALLING_LEAVES(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount);
                break;
            case EXPLODING_STARS:
                middlePositions = ParticleTypeEffect.EXPLODING_STARS(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount);
                break;
            case PULSE_WAVES:
                middlePositions = ParticleTypeEffect.PULSE_WAVES(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount, pFreq);
                break;
            case OSCILLATING_RINGS:
                middlePositions = ParticleTypeEffect.OSCILLATING_RINGS(loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind)), radiusM, heightStep, angle, middleCount, rFreq);
                break;
            default:
                if (readConfig().getBoolean("debug.isEnabled") == true){
                    Bukkit.getLogger().severe("[VU-PKA] You tried to use " + effectTypeM + " which currently doesn't exist");
                    listParticleEffectsPresent();
                }
                throw new IllegalStateException("Unexpected effect type: " + effectTypeM);
        }

        headPositions = applyOrientation(loc, headPositions, yaw, pitch);
        middlePositions = applyOrientation(loc, middlePositions, yaw, pitch);

        // Adjust locations for particles for head
        Location redstoneLocation = loc.clone();
        Location dustLocation = loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind));

        if (this.particleH == Particle.REDSTONE) {
            for (Location pos : headPositions) {
                redstoneLocation.getWorld().spawnParticle(particleH, pos, 1, spreadXH, spreadYH, spreadZH, speedH, new Particle.DustOptions(headColor, sizeH));
            }
        } else if (particleH == Particle.DUST_COLOR_TRANSITION) {
            // Generate dust transition particles for head
            for (Location pos : headPositions) {
                redstoneLocation.getWorld().spawnParticle(particleH, pos, 1, spreadXH, spreadYH, spreadZH, speedH, new Particle.DustTransition(transitionColorStart, transitionColorEnd, sizeH));
            }
        } else {
            // Default particle type handling for head
            for (Location pos : headPositions) {
                loc.getWorld().spawnParticle(particleH, pos, 1, spreadXH, spreadYH, spreadZH, speedH, sizeH);
            }
        }

        if (particleM == Particle.DUST_COLOR_TRANSITION) {
            // Lag behind by adjusting the location of dust particles
            dustLocation = dustLocation.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind));
            for (Location pos : middlePositions) {
                // Generate dust transition particles for middle
                dustLocation.getWorld().spawnParticle(particleM, pos, 1, spreadXM, spreadYM, spreadZM, speedM, new Particle.DustTransition(transitionColorStart, transitionColorEnd, sizeM));
            }
        } else if (particleM == Particle.REDSTONE) {
            //redstone particle handling for middle
            for (Location pos : middlePositions) {
                // Generate dust transition particles for middle
                dustLocation.getWorld().spawnParticle(particleM, pos, 1, spreadXM, spreadYM, spreadZM, speedM, new Particle.DustOptions(headColor, sizeM));
            }
        } else {
            //default particle handling for middle
            for (Location pos : middlePositions) {
                Location BParticleLocation = loc.clone().subtract(arrow.getVelocity().normalize().multiply(lagBehind));
                // Generate dust transition particles for middle
                BParticleLocation.getWorld().spawnParticle(particleM, pos, 1, spreadXM, spreadYM, spreadZM, speedM, sizeM);
            }
        }
    }

    // Helper method to apply orientation
    private Location[] applyOrientation(Location origin, Location[] positions, float yaw, float pitch) {
        double[][] rotationMatrix = getRotationMatrix(yaw, pitch);
        Location[] adjustedPositions = new Location[positions.length];

        for (int i = 0; i < positions.length; i++) {
            Location pos = positions[i].clone().subtract(origin);
            Vector vector = new Vector(pos.getX(), pos.getY(), pos.getZ());
            Vector rotated = rotateVector(vector, rotationMatrix);
            adjustedPositions[i] = origin.clone().add(rotated);
        }

        return adjustedPositions;
    }

    private double[][] getRotationMatrix(float yaw, float pitch) {
        double yawRad = Math.toRadians((double) yaw);
        double pitchRad = Math.toRadians((double) pitch);

        double cosYaw = Math.cos(yawRad);
        double sinYaw = Math.sin(yawRad);
        double cosPitch = Math.cos(pitchRad);
        double sinPitch = Math.sin(pitchRad);

        return new double[][] {
                {cosYaw, 0, -sinYaw},
                {sinPitch * sinYaw, cosPitch, cosYaw * sinPitch},
                {cosPitch * sinYaw, -sinPitch, cosPitch * cosYaw}
        };
    }

    private Vector rotateVector(Vector vector, double[][] matrix) {
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();

        double newX = x * matrix[0][0] + y * matrix[0][1] + z * matrix[0][2];
        double newY = x * matrix[1][0] + y * matrix[1][1] + z * matrix[1][2];
        double newZ = x * matrix[2][0] + y * matrix[2][1] + z * matrix[2][2];

        return new Vector(newX, newY, newZ);
    }


}
