/*
    --------------------------
    Project : MCloud
    Package : net.mcloud.api.command.impl
    Date 23.06.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.runner.command.impl;

import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;
import net.mcloud.api.utils.logger.ConsoleColor;
import net.mcloud.runner.MCloudRunner;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCommand extends Command {

    @Override
    public CommandResponse execute(String command_name, ArrayList<String> args) {

        logger().info("all commands listed", ConsoleColor.WHITE);

        MCloudRunner.getInstance().getMCloudApi().getCloudCommandMap().getMap().forEach((s, command) -> {
            logger().info(s + " - " + command.usage(), ConsoleColor.PURPLE);
        });

        return CommandResponse.SUCCESS;
    }

    @Override
    public String usage() {
        return "Zeigt dir eine Liste mit allen Befehlen an.";
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public Map<String, List<AttributedString>> widgetOpt() {
        return new HashMap<>();
    }

    @Override
    public List<AttributedString> desc() {
        return List.of(new AttributedString("Get help about the commands"));
    }
}
