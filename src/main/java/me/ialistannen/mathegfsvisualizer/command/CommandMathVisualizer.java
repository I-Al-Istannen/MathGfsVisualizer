package me.ialistannen.mathegfsvisualizer.command;

import java.util.Optional;
import java.util.Queue;

import me.ialistannen.bukkitutilities.command.CommandSenderType;
import me.ialistannen.bukkitutilities.command.argumentmapping.ArgumentMapper;
import me.ialistannen.bukkitutilities.command.argumentmapping.ArgumentMappers;

/**
 * The base command
 */
public class CommandMathVisualizer extends AbstractCommandNode {

    public CommandMathVisualizer() {
        super("main", CommandSenderType.ALL);

        addChild(new CommandShowTheta());
        addChild(new CommandShowPhi());
        addChild(new CommandShowPhiAndTheta());
        addChild(new CommandShowRho());
        addChild(new CommandTest());
    }

    static {
        ArgumentMappers.addMapper(new ArgumentMapper<Integer>() {
            /**
             * Returns the class it translates to
             *
             * @return The class it translates to
             */
            @Override
            public Class<Integer> getTargetClass() {
                return Integer.class;
            }

            /**
             * Maps an object
             *
             * @param strings The Strings to get the information from. Remove what you
             * need, it will be passed to the next mapper afterwards
             *
             * @return The Mapped object
             */
            @Override
            public Optional<Integer> map(Queue<String> strings) {
                if (strings.isEmpty()) {
                    return Optional.empty();
                }
                try {
                    return Optional.ofNullable(Integer.parseInt(strings.poll()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            }
        });
        ArgumentMappers.addMapper(new ArgumentMapper<Double>() {
            /**
             * Returns the class it translates to
             *
             * @return The class it translates to
             */
            @Override
            public Class<Double> getTargetClass() {
                return Double.class;
            }

            /**
             * Maps an object
             *
             * @param strings The Strings to get the information from. Remove what you
             * need, it will be passed to the next mapper afterwards
             *
             * @return The Mapped object
             */
            @Override
            public Optional<Double> map(Queue<String> strings) {
                if (strings.isEmpty()) {
                    return Optional.empty();
                }
                try {
                    return Optional.ofNullable(Double.parseDouble(strings.poll()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            }
        });
    }

}
