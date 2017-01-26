package me.ialistannen.mathegfsvisualizer.command;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ialistannen.bukkitutilities.command.CommandResult;
import me.ialistannen.bukkitutilities.command.argumentmapping.ConvertedParams;
import me.ialistannen.bukkitutilities.particle.math.SphericalCoordinates;
import me.ialistannen.mathegfsvisualizer.MatheGfsVisualizer;
import me.ialistannen.mathegfsvisualizer.util.LocationUtil;

/**
 * Shows Rho (who needs this?)
 */
class CommandShowRho extends AbstractCommandNode {

    CommandShowRho() {
        super("show.rho");
    }

    @ConvertedParams(targetClasses = {Double.class})
    public CommandResult executePlayer(Player player, Double rho, String[] args, String[] fullArgs) {
        if (rho == null) {
            player.sendMessage(translate("not.a.number", new Object[]{fullArgs[0]}));
            return CommandResult.SUCCESSFULLY_INVOKED;
        }
        SphericalCoordinates coordinates = new SphericalCoordinates(0, 0, 0);

        Location center = LocationUtil.getRelativeToTargetBlock(player, 0.5, 2, 0.5);

        final double startRho = rho;
        
        new BukkitRunnable() {
            int counter = 0;
            double rho = startRho;

            @Override
            public void run() {
                if (counter++ > 20) {
                    cancel();
                }

                coordinates.setRho(rho);

                rho += 0.25;

                for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 16) {
                    coordinates.setTheta(theta);
                    for (double phi = 0; phi < 2 * Math.PI; phi += Math.PI / 16) {
                        coordinates.setPhi(phi);

                        Location location = center.clone().add(coordinates.toBukkitCartesian());
                        location.getWorld().spawnParticle(
                                Particle.CRIT,
                                location,
                                1,
                                0,
                                0,
                                0,
                                0
                        );
                    }
                }
            }
        }.runTaskTimerAsynchronously(MatheGfsVisualizer.getInstance(), 0, 5);
        return CommandResult.SUCCESSFULLY_INVOKED;
    }
}
