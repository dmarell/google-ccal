/*
 * Created by Daniel Marell 15-07-08 11:13
 */
package se.marell.googleccal;

import com.google.api.services.calendar.Calendar;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Helper class providing a way of reading calendar events from a Google calendar.
 * Clients call getNewEvents() at least once an hour. Only new events are returned,
 * meaning that an event is not returned twice. The function returns new events, where the oldest can be up
 * to one hour old and the newest up to one day in the future.
 * The returned events could be used to trigger operations.
 */
public class CommandCalendarProvider {
    private Set<CommandCalendarEvent> returnedEvents = new HashSet<>();

    private com.google.api.services.calendar.Calendar calendarService;

    public CommandCalendarProvider(Calendar calendarService) {
        this.calendarService = calendarService;
    }

    public List<CommandCalendarEvent> getNewEvents(String calendarId) throws IOException {
        removeOldReturnedEvents();

        LocalDateTime from = timeDateOfOldestNewEventToConsider();
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        Set<CommandCalendarEvent> visibleEvents = new HashSet<>(new CommandCalendarReader(calendarService).getEvents(calendarId, from, to));
        LocalDateTime now = LocalDateTime.now();

        List<CommandCalendarEvent> result = new ArrayList<>();
        Iterator<CommandCalendarEvent> iter = visibleEvents.iterator();
        while (iter.hasNext()) {
            CommandCalendarEvent e = iter.next();
            if (!returnedEvents.contains(e) &&
                    e.getDateTime().isAfter(timeDateOfOldestNewEventToConsider()) &&
                    !e.getDateTime().isAfter(now)) {
                result.add(e);
                iter.remove();
            }
        }
        returnedEvents.addAll(result);
        return result;
    }

    private void removeOldReturnedEvents() {
        LocalDateTime oldest = timeDateOfOldestNewEventToConsider();
        Iterator<CommandCalendarEvent> iter = returnedEvents.iterator();
        while (iter.hasNext()) {
            CommandCalendarEvent e = iter.next();
            if (e.getDateTime().isBefore(oldest)) {
                iter.remove();
            }
        }
    }

    private LocalDateTime timeDateOfOldestNewEventToConsider() {
        return LocalDateTime.now().minusMinutes(60);
    }
}
