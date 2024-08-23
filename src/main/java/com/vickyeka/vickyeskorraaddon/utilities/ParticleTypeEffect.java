package com.vickyeka.vickyeskorraaddon.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import static com.vickyeka.vickyeskorraaddon.configuration.ConfigManager.readConfig;

public class ParticleTypeEffect {

    public enum ParticleTypeEffects {
        LINE,
        HELIX,
        WAVY_LINE,
        BURST_SPIRAL,
        CONVERGING_LINES,
        RIPPLES,
        FALLING_LEAVES,
        EXPLODING_STARS,
        PULSE_WAVES,
        OSCILLATING_RINGS
    }

    public enum SpacingMode {
        LINEAR,
        EXPONENTIAL
    }

    public static Location[] LINE(Location origin, double radius, int particleCount) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double x = origin.getX() + radius;
            double z = origin.getZ() + radius;
            double y = origin.getY();
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }
    public static Location[] HELIX(Location origin, double radius, double heightStep, double angle, int particleCount) {
        Location[] positions = new Location[particleCount];
        double step = 2 * Math.PI / particleCount; // Adjust step based on the number of points

        for (int i = 0; i < particleCount; i++) {
            // Introduce a phase shift/randomization to spread out points
            double theta = angle + i * step + Math.random() * step;

            double x = origin.getX() + radius * Math.cos(theta);
            double z = origin.getZ() + radius * Math.sin(theta);
            double y = origin.getY() + i * heightStep / particleCount;

            positions[i] = new Location(origin.getWorld(), x, y, z);
        }

        return positions;
    }


    public static Location[] WAVY_LINE(Location origin, double radius, double heightStep, double angle, int particleCount) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double x = origin.getX() + (radius * Math.cos(theta) * Math.sin(angle));
            double z = origin.getZ() + (radius * Math.sin(theta) * Math.cos(angle));
            double y = origin.getY() + (theta * heightStep);
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }

    public static Location[] BURST_SPIRAL(Location origin, double radius, double heightStep, double angle, int particleCount) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double spiralRadius = radius * (i / (double) particleCount); // Increasing radius for burst effect
            double x = origin.getX() + spiralRadius * Math.cos(theta);
            double z = origin.getZ() + spiralRadius * Math.sin(theta);
            double y = origin.getY() + theta * heightStep;
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }

    public static Location[] CONVERGING_LINES(Location origin, double radius, double heightStep, double angle, int particleCount) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double t = (i / (double) particleCount); // A value between 0 and 1
            double x = origin.getX() + (radius - radius * t) * Math.cos(theta);
            double z = origin.getZ() + (radius - radius * t) * Math.sin(theta);
            double y = origin.getY() + theta * heightStep;
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }

    public static Location[] RIPPLES(Location origin, double radius, double angleStep, double angle, int particleCount, SpacingMode spacingMode, int numCircles) {
        if (numCircles <= 0) {
            if (readConfig().getBoolean("debug.isEnabled") == true){
                Bukkit.getLogger().severe("Number of circles must be greater than 0.");
            }
            return new Location[0]; // Return an empty array
        }

        Location[] positions = new Location[particleCount];
        int minPointsPerCircle = 6;  // Minimum number of points per circle

        // Calculate number of particles per circle
        int pointsPerCircle = Math.max(minPointsPerCircle, particleCount / numCircles);
        int remainingParticles = particleCount;

        // Generate the ripple effect
        for (int circleIndex = 0; circleIndex < numCircles; circleIndex++) {
            // Calculate the number of particles to place in the current circle
            int currentCirclePoints = Math.min(pointsPerCircle, remainingParticles);

            // Update remaining particles
            remainingParticles -= currentCirclePoints;

            // Check if remainingParticles went negative and correct it
            if (remainingParticles < 0) {
                if (readConfig().getBoolean("debug.isEnabled") == true){
                    Bukkit.getLogger().severe("Remaining particles went negative. Consider making the number of Available Particles divisible by the number of circles. Check the logic.");
                }
                remainingParticles = 0; // Correct the issue
            }

            // Calculate the size of the circle based on the angle parameter
            double sizeFactor = Math.max(0.4 * radius, Math.min(1.2 * radius, angle / 360.0)); // Normalize angle to 0-1 range
            double circleRadius;
            if (spacingMode == SpacingMode.LINEAR) {
                circleRadius = radius * (circleIndex + 1) / numCircles;
            } else { // EXPONENTIAL
                circleRadius = radius * (1.0 + Math.pow(circleIndex, 1.5) / Math.pow(numCircles, 1.5));
            }
            circleRadius *= sizeFactor; // Adjust circle radius based on size factor

            // Calculate angular step
            double thetaStep = 2 * Math.PI / currentCirclePoints;

            for (int i = 0; i < currentCirclePoints; i++) {
                double theta = i * thetaStep;  // azimuthal angle
                double phi = angleStep * i;    // polar angle increases with each ripple

                // Parametric equations for the ellipsoid-based ripple effect
                double x = circleRadius * Math.cos(theta) * Math.sin(phi);
                double y = circleRadius * Math.sin(theta) * Math.sin(phi);
                double z = circleRadius * Math.cos(phi);

                // Ensure index is within bounds
                int index = circleIndex * pointsPerCircle + i;
                if (index < positions.length) {
                    positions[index] = new Location(origin.getWorld(), origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                } else {
                    Bukkit.getLogger().warning("Index out of bounds: " + index);
                }
            }

            if (readConfig().getBoolean("debug.isEnabled") == true){
                // Debugging information
                Bukkit.getLogger().info("Circle Index: " + circleIndex + ", Points per Circle: " + pointsPerCircle + ", Current Circle Points: " + currentCirclePoints + ", Remaining Particles: " + remainingParticles);
            }
        }

        return positions;
    }



    public static Location[] FALLING_LEAVES(Location origin, double radius, double heightStep, double angle, int particleCount) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double x = origin.getX() + radius * Math.cos(theta);
            double z = origin.getZ() + radius * Math.sin(theta);
            double y = origin.getY() - (i / (double) particleCount) * heightStep; // Falling effect
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }

    public static Location[] EXPLODING_STARS(Location origin, double radius, double heightStep, double angle, int particleCount) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double explosionRadius = radius * Math.random(); // Randomized explosion radius
            double x = origin.getX() + explosionRadius * Math.cos(theta);
            double z = origin.getZ() + explosionRadius * Math.sin(theta);
            double y = origin.getY() + heightStep; // Constant height for explosion effect
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }

    public static Location[] PULSE_WAVES(Location origin, double radius, double heightStep, double angle, int particleCount, double pFreq) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double waveRadius = radius * Math.sin(angle + i * pFreq); // Pulse effect
            double x = origin.getX() + waveRadius * Math.cos(theta);
            double z = origin.getZ() + waveRadius * Math.sin(theta);
            double y = origin.getY() + heightStep;
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }

    public static Location[] OSCILLATING_RINGS(Location origin, double radius, double heightStep, double angle, int particleCount, double rFreq) {
        Location[] positions = new Location[particleCount];
        for (int i = 0; i < particleCount; i++) {
            double theta = angle + (i / (double) particleCount) * 2 * Math.PI;
            double oscillationRadius = radius * Math.sin(angle + i * rFreq); // Oscillating effect
            double x = origin.getX() + oscillationRadius * Math.cos(theta);
            double z = origin.getZ() + oscillationRadius * Math.sin(theta);
            double y = origin.getY() + heightStep;
            positions[i] = new Location(origin.getWorld(), x, y, z);
        }
        return positions;
    }
}
