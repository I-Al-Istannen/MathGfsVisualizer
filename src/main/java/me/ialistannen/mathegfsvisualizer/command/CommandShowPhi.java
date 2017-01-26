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
 * Shows Phi
 */
class CommandShowPhi extends AbstractCommandNode {

    CommandShowPhi() {
        super("show-phi");
    }

    @Override
    protected CommandResult executePlayer(Player player, String... args) {
        Ticker ticker = StandardTicker.ASYNC_BUKKIT_RUNNABLE.createUnstartedTicker();
        ticker.setDelay(500, TimeUnit.MILLISECONDS);

        // FIXME: 08.01.2017 PHI SHOULD ONLY BE HALF A CIRCLE ==> Theta 0 <= x <= PI

        new PhiParticleEffect(
                ticker,
                LocationUtil.getRelativeToTargetBlock(player, 0.5, 2, 0.5),
                Math.PI / 16,
                true,
                Particle.DRIP_WATER
        );

        ticker.startTicker();

        return CommandResult.SUCCESSFULLY_INVOKED;
    }

    public static class PhiParticleEffect extends RecurringParticleEffect {

        private Sphere baseSphere;
        private double phi;
        private Particle particle;

        /**
         * Adds this {@link Tickable} too
         *
         * @param ticker The ticker to use
         * @param center The center location
         * @param granularity The granularity. The granularity may be the distance
         */
        @SuppressWarnings("WeakerAccess")
        public PhiParticleEffect(Ticker ticker, Location center, double granularity, boolean showSphere,
                                 Particle particle) {
            this(ticker, center, granularity, showSphere, particle, 0);
        }

        /**
         * Adds this {@link Tickable} too
         *
         * @param ticker The ticker to use
         * @param center The center location
         * @param granularity The granularity. The granularity may be the distance
         */
        @SuppressWarnings("WeakerAccess")
        public PhiParticleEffect(Ticker ticker, Location center, double granularity, boolean showSphere,
                                 Particle particle, double phi) {
            super(ticker, center, granularity);

            this.phi = phi;
            this.particle = particle;

            if (showSphere) {
                baseSphere = new Sphere(
                        Orientation.VERTICAL,
                        Math.PI / 16,
                        Math.PI / 16,
                        1,
                        Particle.SUSPENDED_DEPTH
                );
            }
        }

        /**
         * Displays a particle effect at the given center
         *
         * @param center The center of the effect
         */
        @Override
        public void display(Location center) {
            if (baseSphere != null) {
                baseSphere.display(center);
            }

            // my definition is swapped...
            SphericalCoordinates coordinates = new SphericalCoordinates(1, phi, 0);
            for (double theta = 0; theta < Math.PI; theta += getGranularity()) {
                coordinates.setPhi(theta);
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
            if (counter++ > 32) {
                getTicker().stopTicker();
            }
            phi += Math.PI / 16;
        }
    }
}
