package net.mcloud.eventsystem;

public interface Cancellable {

    boolean isCancelled();
    void setCancelled(boolean value);
}
