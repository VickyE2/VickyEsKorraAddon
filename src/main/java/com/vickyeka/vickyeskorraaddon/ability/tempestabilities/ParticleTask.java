package com.vickyeka.vickyeskorraaddon.ability.tempestabilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleTask extends BukkitRunnable {

    private final Arrow arrow;
    private double angle;
    private double radius; // Radius of the helix
    private double heightStep;

    private final Particle particleHead;
    private final Particle particleMiddle;
    private final Particle particleTail;// Height increment per tick

    public ParticleTask(Arrow arrow, double angle, double radius, double heightStep, Particle particleHead, Particle particleMiddle, Particle particleTail) {
        this.arrow = arrow;
        this.angle = angle;
        this.radius = radius;
        this.heightStep = heightStep;
        this.particleHead = particleHead;
        this.particleMiddle = particleMiddle;
        this.particleTail = particleTail;
    }

    @Override
    public void run() {
        if (arrow.isDead()) {
            cancel();
            return;
        }

        Location loc = arrow.getLocation();
        double height = loc.getY() + Math.sin(angle) * heightStep; // Helix height calculation

        // Generate particles in a helix pattern
        for (double i = 0; i < 2 * Math.PI; i += 0.1) {
            double x = loc.getX() + radius * Math.cos(i);
            double z = loc.getZ() + radius * Math.sin(i);
            Location particleLoc = new Location(loc.getWorld(), x, height, z);

            // Different particle types based on position
            if (Math.abs(i) < 0.1) {
                loc.getWorld().spawnParticle(particleHead, particleLoc, 1); // Fire at the tip
            } else if (Math.abs(i - Math.PI) < 0.1) {
                loc.getWorld().spawnParticle(particleMiddle, particleLoc, 1); // Sparks in the middle
            } else {
                loc.getWorld().spawnParticle(particleTail, particleLoc, 1); // Blue fire at the tail
            }
        }

        angle += 0.1; // Adjust speed of spiral rotation
    }
}
