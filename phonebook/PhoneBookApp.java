package phonebook;

import java.io.*;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class PhoneBookApp {
    // File with all contacts and phone numbers
    private final String DIRECTORY_FILE = "/tmp/phoneBookFiles/directory.txt";
    // File with names to find
    private final String FIND_FILE = "/tmp/phoneBookFiles/small_find.txt";

    public void run() {
        String[] findRows = getAllFileRows(FIND_FILE);
        String[] dirRows = getAllFileRows(DIRECTORY_FILE);

        if (findRows != null && dirRows != null) {
            runLinearSearch(findRows, dirRows);
            System.out.println();
            runBubbleSortAndJumpSearch(findRows, dirRows);
        } else {
            System.out.println("Error while processing files!");
        }
    }

    private void runBubbleSortAndJumpSearch(String[] findRows, String[] dirRows) {
        String[] sortedDirRows = dirRows.clone();

        long startTime = System.currentTimeMillis();
        runBubbleSort(sortedDirRows);
        long endTime = System.currentTimeMillis();

        saveAllRowsToFile(sortedDirRows, DIRECTORY_FILE, "_sorted");

        System.out.printf("Sorting time: %s\n", getTimeTakenStr(startTime, endTime));
    }

    private void saveAllRowsToFile(String[] rowsArray, String fileName, String fileSuffix) {
        String newFileName;
        // has extension set
        if (fileName.indexOf(".") > 0) {
            String currentFileName = fileName.substring(0, fileName.lastIndexOf("."));
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            newFileName = currentFileName.concat(fileSuffix).concat(fileExtension);
        } else {
            newFileName = fileName.concat(fileSuffix);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFileName))) {
            for (String row: rowsArray) {
                writer.write(row);
                writer.write("\n");
            }
        } catch (IOException ex) {
            System.out.println("Error while writing to file: " + newFileName);
        }
    }

    private void runBubbleSort(String[] unsortedDirRows) {
        for (int i = 0; i < unsortedDirRows.length; i++) {
            boolean hasChanged = false;
            for (int j = 0; j < unsortedDirRows.length - 1; j++) {
                int startNameIndex1 = unsortedDirRows[j].indexOf(" ") + 1;
                int startNameIndex2 = unsortedDirRows[j + 1].indexOf(" ") + 1;
                String name1 = unsortedDirRows[j].substring(startNameIndex1);
                String name2 = unsortedDirRows[j + 1].substring(startNameIndex2);
                if (name1.compareTo(name2) > 0) {
                    String aux = unsortedDirRows[j];
                    unsortedDirRows[j] = unsortedDirRows[j + 1];
                    unsortedDirRows[j + 1] = aux;
                    hasChanged = true;
                }
            }
            if (!hasChanged) {
                return;
            }
        }
    }

    private void runLinearSearch(String[] findRows, String[] dirRows) {
        System.out.println("Start searching (linear search)...");

        long startTime = System.currentTimeMillis();
        int foundEntries = findEntriesUsingLinearSearch(findRows, dirRows);
        long endTime = System.currentTimeMillis();

        System.out.printf("Found %s / %s entries. Time taken: %s\n",
                foundEntries, findRows.length, getTimeTakenStr(startTime, endTime));
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

    private int findEntriesUsingLinearSearch(String[] findRows, String[] dirRows) {
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
