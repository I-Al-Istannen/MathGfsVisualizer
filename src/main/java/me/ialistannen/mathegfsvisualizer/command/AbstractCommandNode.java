package me.ialistannen.mathegfsvisualizer.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import me.ialistannen.bukkitutilities.command.CommandNode;
import me.ialistannen.bukkitutilities.command.CommandSenderType;
import me.ialistannen.bukkitutilities.command.TranslatedCommandNode;
import me.ialistannen.mathegfsvisualizer.MatheGfsVisualizer;

/**
 * HELP ME BOILERPLATE
 */
public class AbstractCommandNode extends TranslatedCommandNode {

    protected AbstractCommandNode(String baseKey, CommandSenderType... commandSenderTypes) {
        super(new Permission(MatheGfsVisualizer.getInstance().getConfig().getString("permissions.command." + baseKey)),
                "command." + baseKey,
                MatheGfsVisualizer.getInstance().getMessageProvider(),
                commandSenderTypes);
    }
    
    public AbstractCommandNode(String baseKey) {
        this(baseKey, CommandSenderType.PLAYER);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> wholeChat, int relativeIndex) {
        return getChildren().stream()
                .map(CommandNode::getKeyword)
                .collect(Collectors.toList());
    }
}
