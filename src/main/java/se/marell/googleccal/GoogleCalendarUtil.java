/*
 * Created by Daniel Marell 03/03/16.
 */
package se.marell.googleccal;

import com.google.api.client.util.DateTime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class GoogleCalendarUtil {
    private GoogleCalendarUtil() {
    }

    public static LocalDateTime convert(DateTime dateTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getValue()), ZoneId.systemDefault());
    }

    public static DateTime convert(LocalDateTime dateTime) {
        return new DateTime(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
