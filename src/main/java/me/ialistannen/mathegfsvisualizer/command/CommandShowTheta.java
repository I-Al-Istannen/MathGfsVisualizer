package me.ialistannen.mathegfsvisualizer.command;

import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import me.ialistannen.bukkitutilities.command.CommandResult;
import me.ialistannen.bukkitutilities.particle.effect.RecurringParticleEffect;
import me.ialistannen.bukkitutilities.particle.effect.shapes.Orientation;
import me.ialistannen.bukkitutilities.particle.effect.shapes.Sphere;
import me.ialistannen.bukkitutilities.particle.math.SphericalCoordinates;
import me.ialistannen.bukkitutilities.particle.ticker.StandardTicker;
import me.ialistannen.bukkitutilities.particle.ticker.Ticker;
import me.ialistannen.bukkitutilities.particle.ticker.Ticker.Tickable;
import me.ialistannen.mathegfsvisualizer.util.LocationUtil;

/**
 * Shows what the Azimut angle does
 */
class CommandShowTheta extends AbstractCommandNode {

    CommandShowTheta() {
        super("show-theta");
    }

    @Override
    protected CommandResult executePlayer(Player player, String... args) {
        Ticker ticker = StandardTicker.ASYNC_BUKKIT_RUNNABLE.createUnstartedTicker();
        ticker.setDelay(500, TimeUnit.MILLISECONDS);

        new ThetaParticleEffect(
                ticker,
                LocationUtil.getRelativeToTargetBlock(player, 0.5, 2, 0.5),
                Math.PI / 16,
                Particle.DRIP_WATER
        );

        ticker.startTicker();

        return CommandResult.SUCCESSFULLY_INVOKED;
    }

    public static class ThetaParticleEffect extends RecurringParticleEffect {

        private Sphere baseSphere;
        private double theta;
        private Particle particle;

        /**
         * Adds this {@link Tickable} too
         *
         * @param ticker The ticker to use
         * @param center The center location
         * @param granularity The granularity. The granularity may be the distance
         */
        @SuppressWarnings("WeakerAccess")
        public ThetaParticleEffect(Ticker ticker, Location center, double granularity,
                                   Particle particle) {
            this(ticker, center, granularity, particle, 0);
        }

        /**
         * Adds this {@link Tickable} too
         *
         * @param ticker The ticker to use
         * @param center The center location
         * @param granularity The granularity. The granularity may be the distance
         */
        @SuppressWarnings("WeakerAccess")
        public ThetaParticleEffect(Ticker ticker, Location center, double granularity,
                                   Particle particle, double theta) {
            super(ticker, center, granularity);

            this.particle = particle;
            this.theta = theta;

            baseSphere = new Sphere(
                    Orientation.VERTICAL,
                    Math.PI / 16,
                    Math.PI / 16,
                    1,
                    Particle.SUSPENDED_DEPTH
            );
        }
        
        /**
         * Displays a particle effect at the given center
         *
         * @param center The center of the effect
         */
        @Override
        public void display(Location center) {
            baseSphere.display(center);

            // my definitions are swapped
            SphericalCoordinates coordinates = new SphericalCoordinates(1, 0, theta);
            for (double phi = 0; phi < 2 * Math.PI; phi += getGranularity()) {
                coordinates.setTheta(phi);
                center.getWorld().spawnParticle(
                        particle,
                        center.clone().add(coordinates.toBukkitCartesian()),
                        1,
                        0,
                        0,
                        0,
                        0
                );
            }
        }

        private int counter = 0;

        /**
         * Ticks this
         *
         * @param elapsedNanoSeconds The elapsed nano seconds
         */
        @Override
        public void tick(long elapsedNanoSeconds) {
            display(getCenter());
            if (counter++ > 17) {
                getTicker().stopTicker();
            }
            theta += Math.PI / 16;
        }
    }
}
