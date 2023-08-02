package phonebook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class PhoneBookApp {
    private final String DIRECTORY_FILE = "/tmp/phoneBookFiles/directory.txt";
    private final String FIND_FILE = "/tmp/phoneBookFiles/find.txt";

    public void run() {
        String[] findRows = getAllFileRows(FIND_FILE);
        String[] dirRows = getAllFileRows(DIRECTORY_FILE);
        if (findRows != null && dirRows != null) {
            System.out.println("Start searching...");

            long startTime = System.currentTimeMillis();
            int foundEntries = findEntries(findRows, dirRows);
            long endTime = System.currentTimeMillis();

            System.out.printf("Found %s / %s entries. Time taken: %s\n",
                    foundEntries, findRows.length, getTimeTakenStr(startTime, endTime));
        } else {
            System.out.println("Error while processing files!");
        }
    }

    private String getTimeTakenStr(long startTime, long endTime) {
        long totalTimeInMillis = endTime - startTime;
        Instant instant = Instant.ofEpochMilli(totalTimeInMillis);
        LocalTime localTime = LocalTime.ofInstant(instant, ZoneId.systemDefault());
        return String.format("%s min. %s sec. %s ms.",
                localTime.getMinute(),
                localTime.getSecond(),
                localTime.getNano() / 1000000);
    }

    private int findEntries(String[] findRows, String[] dirRows) {
        int foundEntries = 0;
        for (String fRow: findRows) {
            for (String dRow: dirRows) {
                if (dRow.contains(fRow)) {
                    foundEntries++;
                    break;
                }
            }
        }
        return foundEntries;
    }

    private String[] getAllFileRows(String fileDir) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
            return br.lines().toArray(String[]::new);
        } catch (IOException ex) {
            System.out.println("Error while reading file!");
        }
        return null;
    }
}
