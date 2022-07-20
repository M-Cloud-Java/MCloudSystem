package net.mcloud.api.eventsystem;

import net.mcloud.api.MCloudApi;

import java.util.*;

public class HandlerList {
    private volatile RegisteredListener[] handlers = null;

    private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerSlots;

    private static final ArrayList<HandlerList> allLists = new ArrayList<>();

    public static void bakeAll() {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                h.bake();
            }
        }
    }

    public static void unregisterAll() {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                synchronized (h) {
                    for (List<RegisteredListener> list : h.handlerSlots.values()) {
                        list.clear();
                    }
                    h.handlers = null;
                }
            }
        }
    }

    public static void unregisterAll(MCloudApi api) {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                h.unregister(api);
            }
        }
    }

    public static void unregisterAll(Listener listener) {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                h.unregister(listener);
            }
        }
    }

    public HandlerList() {
        handlerSlots = new EnumMap<>(EventPriority.class);
        for (EventPriority e : EventPriority.values()) {
            handlerSlots.put(e, new ArrayList<>());
        }
        synchronized (allLists) {
            allLists.add(this);
        }
    }

    public synchronized void register(RegisteredListener listener) {
        if (handlerSlots.get(listener.getEventPriority()).contains(listener))
            throw new IllegalStateException("This listener is already registered to priority " + listener.getEventPriority().toString());
        handlers = null;
        handlerSlots.get(listener.getEventPriority()).add(listener);
    }

    public void registerAll(Collection<RegisteredListener> listeners) {
        for (RegisteredListener listener : listeners) {
            register(listener);
        }
    }

    public synchronized void unregister(RegisteredListener listener) {
        if(handlerSlots.get(listener.getEventPriority()).remove(listener)) {
            handlers = null;
        }
    }

    public synchronized void unregister(MCloudApi api) {
        boolean changed = false;
        for (List<RegisteredListener> list : handlerSlots.values()) {
            for (ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext(); ) {
                if(i.next().getMCloud().equals(api)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if(changed) handlers = null;
    }

    public synchronized void unregister(Listener listener) {
        boolean changed = false;
        for (List<RegisteredListener> list : handlerSlots.values()) {
            for (ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext(); ) {
                if(i.next().getListener().equals(listener)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) handlers = null;
    }

    public synchronized void bake() {
        if(handlers != null) return;
        List<RegisteredListener> entries = new ArrayList<>();
        for (Map.Entry<EventPriority, ArrayList<RegisteredListener>> entry : handlerSlots.entrySet()) {
            entries.addAll(entry.getValue());
        }
        handlers = entries.toArray(new RegisteredListener[0]);
    }

    public RegisteredListener[] getRegisteredListeners() {
        RegisteredListener[] handlers;
        while ((handlers = this.handlers) == null) {
            bake();
        }
        return handlers;
    }

    public static ArrayList<RegisteredListener> getRegisteredListeners(MCloudApi api) {
        ArrayList<RegisteredListener> listeners = new ArrayList<>();
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                synchronized (h) {
                    for (List<RegisteredListener> list : h.handlerSlots.values()) {
                        for (RegisteredListener listener : list) {
                            if(listener.getMCloud().equals(api)) {
                                listeners.add(listener);
                            }
                        }
                    }
                }
            }
        }
        return listeners;
    }

    public static ArrayList<HandlerList> getHandlerLists() {
        synchronized (allLists) {
            return new ArrayList<>(allLists);
        }
    }
}
