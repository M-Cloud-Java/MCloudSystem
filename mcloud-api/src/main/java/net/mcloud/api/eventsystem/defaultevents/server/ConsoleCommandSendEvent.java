package net.mcloud.api.eventsystem.defaultevents.server;

import net.mcloud.api.eventsystem.Cancellable;
import net.mcloud.api.eventsystem.Event;
import net.mcloud.api.eventsystem.HandlerList;

import java.util.ArrayList;

public class ConsoleCommandSendEvent extends Event implements Cancellable {
    private boolean cancel = false;
    private static final HandlerList handlers = new HandlerList();
    private String cmd_name;
    private ArrayList<String> cmd_args;

    public ConsoleCommandSendEvent(String cmd_Name, ArrayList<String> args) {
        this.cmd_name = cmd_Name;
        this.cmd_args = args;
    }

    public ArrayList<String> getCmd_args() {
        return cmd_args;
    }

    public String getCmd_name() {
        return cmd_name;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean value) {
        cancel = value;
    }
}
