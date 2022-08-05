package net.mcloud.api.commandsystem.reader;


import net.mcloud.api.commandsystem.CommandMap;
import net.mcloud.api.commandsystem.completer.ConsoleCompleter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public record ConsoleCommandReader(CommandMap commandMap) {
    private LineReader prepareLineReader() {

        ConsoleCompleter completer = new ConsoleCompleter(this);

        Terminal terminal;

        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .streams(System.in, System.out)
                    .encoding(StandardCharsets.UTF_8)
                    .dumb(true)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return LineReaderBuilder.builder()
                .completer(completer)
                .terminal(terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_REMOVE_SLASH, false)
                .option(LineReader.Option.INSERT_TAB, false)
                .build();

    }

    public void startConsoleInput() {

        var lineReader = prepareLineReader();

        ConsoleThread consoleThread = new ConsoleThread(lineReader, commandMap);
        consoleThread.start();
    }
}
