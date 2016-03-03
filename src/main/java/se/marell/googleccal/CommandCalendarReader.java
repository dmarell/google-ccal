/*
 * Created by Daniel Marell 15-07-04 12:17
 */
package se.marell.googleccal;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for reading calendar command events.
 */
public class CommandCalendarReader {
    private Calendar calendarService;

    public CommandCalendarReader(Calendar calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Get calendar command events from a given calendar between two points in time.
     *
     * @param calendarId Calendar I§D
     * @param from       From date and time
     * @param to         To date and time
     * @return List of calendar command events
     * @throws IOException
     */
    public List<CommandCalendarEvent> getEvents(String calendarId, LocalDateTime from, LocalDateTime to) throws IOException {
        Events events = calendarService.events().list(calendarId)
                .setTimeMin(GoogleCalendarUtil.convert(from))
                .setTimeMax(GoogleCalendarUtil.convert(to))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<CommandCalendarEvent> result = new ArrayList<>();
        for (Event event : events.getItems()) {
            LocalDateTime localStartDateTime = GoogleCalendarUtil.convert(getDateTime(event.getStart()));
            if (event.getDescription() != null) {
                for (String line : splitLines(event.getDescription())) {
                    FunctionCall call = FunctionCall.parse(line);
                    if (call != null) {
                        result.add(new CommandCalendarEvent(localStartDateTime, call));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get calendar command events from a given calendar between two dates.
     *
     * @param calendarId Calendar I§D
     * @param fromDate   From date
     * @param toDate     To date
     * @return List of calendar command events
     * @throws IOException
     */
    public List<CommandCalendarEvent> getEvents(String calendarId, LocalDate fromDate, LocalDate toDate) throws IOException {
        return getEvents(calendarId, fromDate.atTime(0, 0), toDate.atTime(0, 0));
    }

    private List<String> splitLines(String string) throws IOException {
        BufferedReader rdr = new BufferedReader(new StringReader(string));
        List<String> lines = new ArrayList<>();
        for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
            lines.add(line);
        }
        rdr.close();
        return lines;
    }

    private DateTime getDateTime(EventDateTime eventDateTime) {
        DateTime result = eventDateTime.getDateTime();
        if (result == null) {
            result = eventDateTime.getDate();
        }
        return result;
    }
}
