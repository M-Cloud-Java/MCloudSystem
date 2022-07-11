package net.mcloud.eventsystem;

import net.mcloud.utils.exceptions.EventException;

public interface EventExecutor {

    void execute(Listener listener, Event event) throws EventException;
}
