/*
    --------------------------
    Project : MCloudSystem
    Package : net.mcloud.runner.commands
    Date 05.08.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.runner.commands;

import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandResponse;

import java.util.ArrayList;

public class CloudClearCommand extends Command {

    @Override
    public CommandResponse execute(String commandName, ArrayList<String> args) {

        System.out.print("\033[H\033[2J");
        logger().info("Die Console wurde geleert!");

        return CommandResponse.SUCCESS;
    }

    @Override
    public String usage() {
        return "clears the console";
    }

    @Override
    public String name() {
        return "clear";
    }

    @Override
    public ArrayList<String> args(String[] args) {
        return new ArrayList<>();
    }
}
