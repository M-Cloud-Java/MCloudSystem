package net.mcloud.eventsystem;

import net.mcloud.MCloudApi;
import net.mcloud.utils.exceptions.EventException;

public class RegisteredListener {

    private final Listener listener;

    private final EventPriority eventPriority;

    private final MCloudApi api;

    private final EventExecutor eventExecutor;

    private  final boolean ignoreCancelled;

    public RegisteredListener(Listener listener, EventExecutor eventExecutor, EventPriority eventPriority, MCloudApi api, boolean ignoreCancelled) {
        this.listener = listener;
        this.eventPriority = eventPriority;
        this.eventExecutor = eventExecutor;
        this.ignoreCancelled = ignoreCancelled;
        this.api = api;
    }

    public void callEvent(Event event) throws EventException {
        if(event instanceof Cancellable) {
            if( event.isCancelled() && isIgnoreCancelled()) {
                return;
            }
        }
        eventExecutor.execute(listener, event);
    }

    public Listener getListener() {
        return listener;
    }

    public MCloudApi getMCloud() {
        return api;
    }

    public EventPriority getEventPriority() {
        return eventPriority;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }
}
