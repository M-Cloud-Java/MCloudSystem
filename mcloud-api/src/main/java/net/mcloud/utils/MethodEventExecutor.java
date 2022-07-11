package net.mcloud.utils;

import net.mcloud.eventsystem.Event;
import net.mcloud.eventsystem.EventExecutor;
import net.mcloud.eventsystem.Listener;
import net.mcloud.utils.exceptions.EventException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEventExecutor implements EventExecutor {

    private final Method method;

    public MethodEventExecutor(Method method) {
        this.method = method;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            Class<Event>[] params = (Class<Event>[]) method.getParameterTypes();
            for (Class<Event> param : params) {
                if (param.isAssignableFrom(event.getClass())) {
                    method.invoke(listener, event);
                    break;
                }
            }
        } catch (InvocationTargetException ex) {
            throw new EventException(ex.getCause());
        } catch (ClassCastException ex) {

        } catch (Throwable t) {
            throw new EventException(t);
        }
    }

    public Method getMethod() {
        return method;
    }
}
