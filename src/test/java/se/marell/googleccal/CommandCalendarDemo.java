/*
 * Created by Daniel Marell 03/03/16.
 */
package se.marell.googleccal;

import java.io.File;
import java.util.List;

public class CommandCalendarDemo {
    public static void main(String[] args) throws Exception {
        String applicationName = "google-ccal-demo";
        String serviceAccountId = "<create an ID/email according to README.md>";
        String calendarId = "<create a calendar and copy it's ID here>";

        CommandCalendarProvider commandCalender = new CommandCalendarProvider(
                GoogleCalendarFactory.getCalendarService(
                        applicationName, serviceAccountId, new File("google-ccal.p12")));

        // Add a command event in the calendar near the current date/time while this loop is running.
        // It should be printed once.
        while (true) {
            List<CommandCalendarEvent> events = commandCalender.getNewEvents(calendarId);
            System.out.println("Found events: " + events.size());
            for (CommandCalendarEvent e : events) {
                System.out.println("    " + e);
            }
            Thread.sleep(10000);
        }
    }
}
