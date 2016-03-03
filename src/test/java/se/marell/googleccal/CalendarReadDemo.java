/*
 * Created by Daniel Marell 15-07-03 17:27
 */
package se.marell.googleccal;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class CalendarReadDemo {
    private static final String SERVICE_ACCOUNT_ID = "xxxxxxxxxx";

    private static com.google.api.services.calendar.Calendar createCalendar() throws IOException {
        return GoogleCalendarFactory.getCalendarService("google-ccal-demo", SERVICE_ACCOUNT_ID, new File("google-ccal.p12"));
    }

    @Test
    public void listSharedCalendars() throws IOException {
        CalendarList calendarList = createCalendar().calendarList().list().execute();
        List<CalendarListEntry> entries = calendarList.getItems();
        if (entries.size() == 0) {
            System.out.println("No entries found.");
        } else {
            System.out.println("Entries:");
            for (CalendarListEntry entry : entries) {
                Calendar calendar = createCalendar().calendars().get(entry.getId()).execute();
                System.out.printf("summary: %s, description: %s, minAccessRole: %s: %s\n",
                        entry.getSummary(), entry.getDescription(), entry.getAccessRole(), calendar);
            }
        }
    }

    @Test
    public void readSpecificCalendar() throws IOException {
        Events events = createCalendar().events().list("xxxxxxxxxxxxxx@group.calendar.google.com")
                .setTimeMin(DateTime.parseRfc3339("2015-06-01T00:00:00"))
                .setTimeMax(DateTime.parseRfc3339("2015-06-30T23:59:59"))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            double sum = 0;
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                DateTime end = event.getEnd().getDateTime();
                if (end == null) {
                    end = event.getEnd().getDate();
                }
                LocalDateTime start2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(start.getValue()), ZoneId.systemDefault());
                LocalDateTime end2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(end.getValue()), ZoneId.systemDefault());
                Duration duration = Duration.between(start2, end2);
                double hours = duration.getSeconds() / 3600;
                System.out.printf("%s (%s)\t%.1f\n", event.getSummary(), start, hours);
                sum += hours;
            }
            System.out.printf("Sum=%.1f\n", sum);
        }
    }
}
