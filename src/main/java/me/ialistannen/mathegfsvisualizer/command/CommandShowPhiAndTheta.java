package me.ialistannen.mathegfsvisualizer.command;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ialistannen.bukkitutilities.command.CommandResult;
import me.ialistannen.bukkitutilities.command.argumentmapping.ConvertedParams;
import me.ialistannen.bukkitutilities.particle.math.SphericalCoordinates;
import me.ialistannen.bukkitutilities.particle.ticker.StandardTicker;
import me.ialistannen.bukkitutilities.particle.ticker.Ticker;
import me.ialistannen.mathegfsvisualizer.MatheGfsVisualizer;
import me.ialistannen.mathegfsvisualizer.command.CommandShowPhi.PhiParticleEffect;
import me.ialistannen.mathegfsvisualizer.command.CommandShowTheta.ThetaParticleEffect;
import me.ialistannen.mathegfsvisualizer.util.LocationUtil;

/**
 * Shows PHI AND Theta
 */
class CommandShowPhiAndTheta extends AbstractCommandNode {

    CommandShowPhiAndTheta() {
        super("show.phi.and.theta");
    }

    @ConvertedParams(targetClasses = {Double.class, Double.class})
    public CommandResult executePlayer(Player player, Double theta, Double phi, String[] args, String[] fullArgs) {
        if (theta == null) {
            player.sendMessage(translate("not.a.number", new Object[]{fullArgs[0]}));
            return CommandResult.SUCCESSFULLY_INVOKED;
        }
        if (phi == null) {
            player.sendMessage(translate("not.a.number", new Object[]{fullArgs[1]}));
            return CommandResult.SUCCESSFULLY_INVOKED;
        }

        SphericalCoordinates coordinates = new SphericalCoordinates(
                1,
                Math.toRadians(theta),
                Math.toRadians(phi)
        );


        Location center = LocationUtil.getRelativeToTargetBlock(player, 0.5, 2, 0.5);

        assert center != null;

        double granularity = Math.PI / 16;

        Ticker ticker = StandardTicker.ASYNC_BUKKIT_RUNNABLE.createUnstartedTicker();
        PhiParticleEffect phiParticleEffect = new PhiParticleEffect(
                ticker,
                center,
                granularity,
                false,
                Particle.DRIP_LAVA,
                Math.toRadians(theta)
        );
        ThetaParticleEffect thetaParticleEffect = new ThetaParticleEffect(
                ticker,
                center,
                granularity,
                Particle.DRIP_WATER,
                Math.toRadians(phi)
        );

        new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                if (counter++ > 2) {
                    cancel();
                }
                player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,
                        center.clone().add(coordinates.toBukkitCartesian()),
                        1, 0, 0, 0, 0);

                thetaParticleEffect.display(center.clone());
                phiParticleEffect.display(center.clone());
            }
        }.runTaskTimer(MatheGfsVisualizer.getInstance(), 0, 10);

        return CommandResult.SUCCESSFULLY_INVOKED;
    }
}
