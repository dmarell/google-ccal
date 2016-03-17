## Google Command Calendar

Java interface for reading Google calendar. It was made for entering commands in my calendar and
control my car heater or lights in my house. 

## Release notes
* Version 1.0.0 - 2016-03-17
  * First release

### Maven usage

```
    <repositories>
      <repository>
        <id>marell</id>
        <url>http://marell.se/artifactory/libs-release</url>
      </repository>
    </repositories>
...
    <dependency>
        <groupId>se.marell.googleccal</groupId>
        <artifactId>google-ccal</artifactId>
        <version>1.0.0</version>
    </dependency>
```

### Setup
 
1. Login to https://console.developers.google.com/home/dashboard
2. Create a new project
3. Enable Google Calendar API
4. Select "Create Credentials", "Create service account key",
   "App engine default service account", Key type: "P12".
   Download the P12 file.
5. Rename the P12-file to google-ccal.p12 and locate it in the root directory of the application
6. Copy the service account email and use it as service account id
7. Create a new calendar and get it's ID from it's settings page
8. Share this calendar with the service account email/id

### Test
Test it by adding a calendar event with any title text (does not matter) and in the "Description" field a 
function call like this one:

foo(12, bar)

Use the CommandCalendarProvider and call getNewEvents()

It will return a CommandCalendarEvent with a functionCall with name "foo" and parameters "12" and "foo".

### Code example

```
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
```

See CalendarReadDemo.java for more examples how to read Calendars.
