package me.ialistannen.mathegfsvisualizer;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import me.ialistannen.bukkitutilities.command.CommandNode;
import me.ialistannen.bukkitutilities.command.CommandTree;
import me.ialistannen.bukkitutilities.command.DefaultCommandExecutor;
import me.ialistannen.bukkitutilities.command.DefaultTabCompleter;
import me.ialistannen.bukkitutilities.language.I18N;
import me.ialistannen.bukkitutilities.language.MessageProvider;
import me.ialistannen.bukkitutilities.modulesystem.ModuleManager;
import me.ialistannen.mathegfsvisualizer.command.CommandMathVisualizer;

public final class MatheGfsVisualizer extends JavaPlugin {

    private static MatheGfsVisualizer instance;

    private MessageProvider messageProvider;
    private CommandTree commandTree;

    public void onEnable() {
        if (!ModuleManager.INSTANCE.registerPlugin(this)) {
            getLogger().severe("Error loading this plugin, see the log from BukkitUtilities!");
            return;
        }
        instance = this;

        I18N.copyDefaultFiles(this, true, "me.ialistannen.mathegfsvisualizer.language");
        messageProvider = new I18N(this, "me.ialistannen.mathegfsvisualizer.language");

        reload();
    }

    private void reload() {
        if (commandTree != null) {
            for (CommandNode node : commandTree.getAllCommands()) {
                // will only unregister if top level command
                commandTree.removeTopLevelChild(node);
            }
        }
        messageProvider.reload();

        commandTree = new CommandTree();
        CommandExecutor commandExecutor = new DefaultCommandExecutor(commandTree, messageProvider);
        TabCompleter tabCompleter = new DefaultTabCompleter(commandTree);

        CommandMathVisualizer commandMathVisualizer = new CommandMathVisualizer();
        commandTree.addTopLevelChildAndRegister(commandMathVisualizer, commandExecutor, tabCompleter, this);
        commandTree.attachHelp(
                commandMathVisualizer,
                getConfig().getString("permissions.command.help"),
                messageProvider
        );
    }

    @Override
    public void onDisable() {
        // prevent the old instance from still being around.
        instance = null;
    }

    /**
     * @return The {@link MessageProvider}
     */
    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    /**
     * Returns the plugins instance
     *
     * @return The plugin instance
     */
    public static MatheGfsVisualizer getInstance() {
        return instance;
    }
}
