/*
    --------------------------
    Project : MCloudSystem
    Package : net.mcloud.api.commandsystem.reader
    Date 24.07.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.api.commandsystem.reader;

import net.mcloud.api.MCloudAPI;
import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.CommandMap;
import net.mcloud.api.commandsystem.CommandResponse;
import net.mcloud.api.eventsystem.defaultevents.server.ConsoleCommandSendEvent;
import net.mcloud.api.utils.logger.ConsoleColor;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;

import java.util.ArrayList;
import java.util.Locale;

public class ConsoleThread extends Thread {

    private final LineReader reader;
    private final CommandMap map;

    public ConsoleThread(LineReader reader, CommandMap map) {
        this.reader = reader;
        this.map = map;
    }

    @Override
    public synchronized void start() {
        String line;
        try {

            while (MCloudAPI.getApi().isEnabled()) {
                line = reader.readLine(ConsoleColor.RESET.getColor() + "Cloud > ").toLowerCase(Locale.ROOT);

                String[] command_line = line.split(" ");
                String command_name = command_line[0].replaceAll(" ", "");
                ArrayList<String> args = new ArrayList<>();
                for (String s : command_line) {
                    if (!s.equals(command_name)) {
                        args.add(s);
                    }
                }
                if (!line.isEmpty() || !line.isBlank()) {
                    ConsoleCommandSendEvent event = new ConsoleCommandSendEvent(command_name, args);
                    if (!event.isCancelled()) {
                        MCloudAPI.getApi().getCloudManager().callEvent(event);
                        if (this.map == null) {
                            MCloudAPI.getApi().getLogger().error("CommandMap is null");
                            args.clear();
                        } else {
                            Command command = this.map.getMap().get(command_name);
                            if (command == null) {
                                MCloudAPI.getApi().getLogger().error("Dieser Command wurde nicht gefunden!");
                                args.clear();
                            } else {
                                CommandResponse execute = command.execute(command_name, args);
                                MCloudAPI.getApi().getLogger().info("Command " + execute.name());
                                args.clear();
                            }
                        }
                    }
                }
            }

        } catch (UserInterruptException | EndOfFileException e) {
            e.printStackTrace();
        }
    }
}
