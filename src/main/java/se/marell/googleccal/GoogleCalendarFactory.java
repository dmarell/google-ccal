/*
 * Created by Daniel Marell 15-07-08 20:56
 */
package se.marell.googleccal;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

public class GoogleCalendarFactory {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static Calendar getCalendarService(String applicationName, String serviceAccountId, File privateKeyP12File) throws IOException {
        HttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("newTrustedTransport failed", e);
        }
        return new Calendar.Builder(httpTransport, JSON_FACTORY, authorize(httpTransport, serviceAccountId, privateKeyP12File))
                .setApplicationName(applicationName)
                .build();
    }

    private static Credential authorize(HttpTransport httpTransport, String serviceAccountId, File privateKeyP12File) throws IOException {
        try {
            return new GoogleCredential.Builder().setTransport(httpTransport)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId(serviceAccountId)
                    .setServiceAccountScopes(Collections.singletonList(CalendarScopes.CALENDAR_READONLY))
                    .setServiceAccountPrivateKeyFromP12File(privateKeyP12File)
                    .build();
        } catch (GeneralSecurityException e) {
            throw new IOException("GeneralSecurityException", e);
        }
    }

    public static LocalDateTime convert(DateTime dateTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getValue()), ZoneId.systemDefault());
    }

    public static DateTime convert(LocalDateTime dateTime) {
        return new DateTime(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}