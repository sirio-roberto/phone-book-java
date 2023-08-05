package phonebook;

import java.io.*;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class PhoneBookApp {
    // File with all contacts and phone numbers
    private final String DIRECTORY_FILE = "/tmp/phoneBookFiles/directory.txt";
    // File with names to find
    private final String FIND_FILE = "/tmp/phoneBookFiles/find.txt";

    public void run() {
        String[] findRows = getAllFileRows(FIND_FILE);
        String[] dirRows = getAllFileRows(DIRECTORY_FILE);

        if (findRows != null && dirRows != null) {
            long searchStartTime = System.currentTimeMillis();
            runLinearSearch(findRows, dirRows);
            long searchEndTime = System.currentTimeMillis();

            System.out.println();
            runBubbleSortAndJumpSearch(findRows, dirRows, searchEndTime - searchStartTime);
        } else {
            System.out.println("Error while processing files!");
        }
    }

    private void runBubbleSortAndJumpSearch(String[] findRows, String[] dirRows, long linearSearchDuration) {
        System.out.println("Start searching (bubble sort + jump search)...");

        String[] sortedDirRows = dirRows.clone();

        long sortStartTime = System.currentTimeMillis();
        boolean hasSorted = runBubbleSort(sortedDirRows, linearSearchDuration);
        // saveAllRowsToFile(sortedDirRows, DIRECTORY_FILE, "_sorted");
        long sortEndTimeSort = System.currentTimeMillis();

        int foundEntries;
        long searchStartTime = System.currentTimeMillis();
        if (hasSorted) {
            foundEntries = findEntriesUsingJumpSearch(findRows, sortedDirRows);
        } else {
            foundEntries = findEntriesUsingLinearSearch(findRows, dirRows);
        }
        long searchEndTime = System.currentTimeMillis();

        System.out.printf("Found %s / %s entries. Time taken: %s\n",
                foundEntries, findRows.length, getTimeTakenStr(sortStartTime, searchEndTime));

        System.out.printf("Sorting time: %s%s\n", getTimeTakenStr(sortStartTime, sortEndTimeSort),
                hasSorted ? "" : " - STOPPED, moved to linear search");

        System.out.printf("Searching time: %s\n", getTimeTakenStr(searchStartTime, searchEndTime));
    }

    // TODO: it seems this method is too slow, so we need to review it
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

    private boolean runBubbleSort(String[] unsortedDirRows) {
        return runBubbleSort(unsortedDirRows, -1L);
    }

    private boolean runBubbleSort(String[] unsortedDirRows, long linearSearchDuration) {
        long maxAllowedDuration = linearSearchDuration * 10;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < unsortedDirRows.length; i++) {
            boolean hasChanged = false;
            for (int j = 0; j < unsortedDirRows.length - 1; j++) {
                if (maxAllowedDuration > 0) {
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime > maxAllowedDuration) {
                        return false;
                    }
                }
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
                return true;
            }
        }
        return true;
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

    private int findEntriesUsingJumpSearch(String[] findRows, String[] sortedDirRows) {
        int foundEntries = 0;
        int jumpLen = (int) Math.floor(Math.sqrt(sortedDirRows.length));
        for (String fRow: findRows) {
            for (int i = 0; i < sortedDirRows.length; i += jumpLen) {
                if (i >= sortedDirRows.length) {
                    i = sortedDirRows.length - 1;
                }

                int startNameIndex = sortedDirRows[i].indexOf(" ") + 1;
                String name = sortedDirRows[i].substring(startNameIndex);
                if (name.compareTo(fRow) == 0) {
                    foundEntries++;
                    break;
                } else if (name.compareTo(fRow) > 0) {
                    int blockStartIndex = i - jumpLen;
                    if (blockStartIndex < 0) {
                        break;
                    }
                    while (i > blockStartIndex) {
                        i--;
                        startNameIndex = sortedDirRows[i].indexOf(" ") + 1;
                        name = sortedDirRows[i].substring(startNameIndex);
                        if (name.compareTo(fRow) == 0) {
                            foundEntries++;
                            break;
                        }
                    }
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
