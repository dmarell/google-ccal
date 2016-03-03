/*
 * Created by Daniel Marell 15-07-08 21:07
 */
package se.marell.googleccal;

import java.time.LocalDateTime;

/**
 * Represents a command in a Google calendar event.
 */
public class CommandCalendarEvent {
    private LocalDateTime dateTime;
    private FunctionCall functionCall;

    public CommandCalendarEvent(LocalDateTime dateTime, FunctionCall functionCall) {
        this.dateTime = dateTime;
        this.functionCall = functionCall;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandCalendarEvent that = (CommandCalendarEvent) o;

        if (!dateTime.equals(that.dateTime)) return false;
        return functionCall.equals(that.functionCall);
    }

    @Override
    public int hashCode() {
        int result = dateTime.hashCode();
        result = 31 * result + functionCall.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CommandCalendarEvent{" +
                "dateTime=" + dateTime +
                ", functionCall=" + functionCall +
                '}';
    }
}
