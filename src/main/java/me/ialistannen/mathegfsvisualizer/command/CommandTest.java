package me.ialistannen.mathegfsvisualizer.command;

import org.bukkit.entity.Player;

import me.ialistannen.bukkitutilities.command.CommandResult;


/**
 * A test command
 */
public class CommandTest extends AbstractCommandNode {

    public CommandTest() {
        super("test");
    }

    @Override
    protected CommandResult executePlayer(Player player, String... args) {
        player.sendMessage("Test!");
        return CommandResult.SUCCESSFULLY_INVOKED;
    }

}
