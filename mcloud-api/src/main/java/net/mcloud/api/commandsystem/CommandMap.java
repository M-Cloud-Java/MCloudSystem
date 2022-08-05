package net.mcloud.api.commandsystem;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {

    private final Map<String, Command> COMMAND_MAP;

    public CommandMap() {
        COMMAND_MAP = new HashMap<>();
    }

    public void register(Command command) {
        COMMAND_MAP.put(command.name(), command);
    }

    public void unregister(String command) {
        COMMAND_MAP.remove(command);
    }

    public Map<String, Command> getMap() {
        return COMMAND_MAP;
    }
}
