/*
    --------------------------
    Project : MCloudSystem
    Package : net.mcloud.api.commandsystem.completer
    Date 24.07.2022
    @author ShortException
    --------------------------
*/


package net.mcloud.api.commandsystem.completer;

import net.mcloud.api.commandsystem.Command;
import net.mcloud.api.commandsystem.reader.ConsoleCommandReader;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCompleter implements Completer {

    private final ConsoleCommandReader handler;

    public ConsoleCompleter(ConsoleCommandReader handler) {
        this.handler = handler;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {

        if (line.line().isBlank()) {
            handler.commandMap().getMap().forEach((s, command) -> {
                candidates.add(new Candidate(s));
            });
            return;
        }

        String sentLine = line.line();

        ArrayList<String> suggestions = getSuggestions(sentLine);
        if (suggestions.isEmpty())
            return;

        candidates.addAll(suggestions.stream().map(Candidate::new).toList());

    }

    private ArrayList<String> getSuggestions(String sentLine) {
        ArrayList<String> list = new ArrayList<>();

        String[] splitCommand = sentLine.split(" ");

        if (splitCommand[0] == null || !handler.commandMap().getMap().containsKey(splitCommand[0])) {
            return list;
        }

        Command command = handler.commandMap().getMap().get(splitCommand[0]);

        String[] args = sentLine.replace(splitCommand[0], "").replaceFirst(" ", "").split(" ");

        list.addAll(command.args(args));

        return list;
    }
}
