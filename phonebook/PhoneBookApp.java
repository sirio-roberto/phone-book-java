package phonebook;

import java.io.*;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PhoneBookApp {

    public void run() {
        // File with names to find
        String FIND_FILE = "/tmp/phoneBookFiles/find.txt";
        // File with all contacts and phone numbers
        String DIRECTORY_FILE = "/tmp/phoneBookFiles/medium_directory.txt";

        String[] findRows = getAllFileRows(FIND_FILE);
        String[] dirRows = getAllFileRows(DIRECTORY_FILE);

        if (findRows != null && dirRows != null) {
            runLinearSearch(findRows, dirRows);

            System.out.println();
            runHashCreationAndSearch(findRows, dirRows);

            System.out.println();
            runQuickSortAndBinarySearch(findRows, dirRows);

            System.out.println();
            runBubbleSortAndJumpSearch(findRows, dirRows);
        } else {
            System.out.println("Error while processing files!");
        }
    }

    private void runHashCreationAndSearch(String[] findRows, String[] dirRows) {
        System.out.println("Start searching (hash table)...");

        Map<String, String> phoneBook = new HashMap<>();
        AtomicInteger foundEntries = new AtomicInteger(0);
        long hashCreationDuration = getMethodDuration(() -> convertBookArrayToMap(dirRows, phoneBook));
        long hashSearchDuration = getMethodDuration(() -> runHashSearch(findRows, phoneBook, foundEntries));

        System.out.printf("Found %s / %s entries. Time taken: %s\n",
                foundEntries, findRows.length, getTimeTakenStr(hashCreationDuration + hashSearchDuration));
        System.out.printf("Creating time: %s\n", getTimeTakenStr(hashCreationDuration));
        System.out.printf("Searching time: %s\n", getTimeTakenStr(hashSearchDuration));
    }

    private void runHashSearch(String[] findRows, Map<String, String> phoneBook, AtomicInteger foundEntries) {
        for (String fRow: findRows) {
            if (phoneBook.containsKey(fRow)) {
                foundEntries.incrementAndGet();
            }
        }
    }

    private long getMethodDuration(Runnable methodToExecute) {
        long startTime = System.currentTimeMillis();
        methodToExecute.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private void convertBookArrayToMap(String[] dirRows, Map<String, String> phoneBook) {
        for (String dRow : dirRows) {
            String name = getNameFromPhoneBookRow(dRow);
            String phone = dRow.substring(0, dRow.indexOf(" "));
            phoneBook.put(name, phone);
        }
    }

    private void runQuickSortAndBinarySearch(String[] findRows, String[] dirRows) {
        System.out.println("Start searching (quick sort + binary search)...");

        String[] sortedDirRows = dirRows.clone();

        long sortDuration = getMethodDuration(() -> runQuickSort(sortedDirRows));

        AtomicInteger foundEntries = new AtomicInteger(0);
        long searchDuration = getMethodDuration(() -> findEntriesUsingBinarySearch(findRows, sortedDirRows, foundEntries));

        System.out.printf("Found %s / %s entries. Time taken: %s\n",
                foundEntries, findRows.length, getTimeTakenStr(sortDuration + searchDuration));
        System.out.printf("Sorting time: %s\n", getTimeTakenStr(sortDuration));
        System.out.printf("Searching time: %s\n", getTimeTakenStr(searchDuration));
    }

    private void runBubbleSortAndJumpSearch(String[] findRows, String[] dirRows) {
        System.out.println("Start searching (bubble sort + jump search)...");

        String[] sortedDirRows = dirRows.clone();

        long sortDuration = getMethodDuration(() -> runBubbleSort(sortedDirRows));

        AtomicInteger foundEntries = new AtomicInteger(0);
        long searchDuration = getMethodDuration(() -> findEntriesUsingJumpSearch(findRows, sortedDirRows, foundEntries));

        System.out.printf("Found %s / %s entries. Time taken: %s\n",
                foundEntries, findRows.length, getTimeTakenStr(sortDuration + searchDuration));
        System.out.printf("Sorting time: %s\n", getTimeTakenStr(sortDuration));
        System.out.printf("Searching time: %s\n", getTimeTakenStr(searchDuration));
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

    private void runQuickSort(String[] unsortedDirRows) {
        runQuickSort(unsortedDirRows, 0, unsortedDirRows.length - 1);
    }

    private void runQuickSort(String[] unsortedDirRows, int lowIndex, int highIndex) {
        if (lowIndex >= highIndex) {
            return;
        }
        String pivotName = getNameFromPhoneBookRow(unsortedDirRows[highIndex]);
        int leftPointer = lowIndex;
        int rightPointer = highIndex;

        while (leftPointer < rightPointer) {
            String leftName = getNameFromPhoneBookRow(unsortedDirRows[leftPointer]);
            while (leftName.compareTo(pivotName) <= 0 && leftPointer < rightPointer) {
                leftPointer++;
                leftName = getNameFromPhoneBookRow(unsortedDirRows[leftPointer]);
            }
            String rightName = getNameFromPhoneBookRow(unsortedDirRows[rightPointer]);
            while (rightName.compareTo(pivotName) >= 0 && leftPointer < rightPointer) {
                rightPointer--;
                rightName = getNameFromPhoneBookRow(unsortedDirRows[rightPointer]);
            }
            swap(unsortedDirRows, leftPointer, rightPointer);
        }
        swap(unsortedDirRows, leftPointer, highIndex);
        // quickSort on left side
        runQuickSort(unsortedDirRows, lowIndex, leftPointer - 1);
        // quickSort on right side
        runQuickSort(unsortedDirRows, leftPointer + 1, highIndex);
    }

    private void swap(String[] stringArray, int index1, int index2) {
        String aux = stringArray[index1];
        stringArray[index1] = stringArray[index2];
        stringArray[index2] = aux;
    }

    private static String getNameFromPhoneBookRow(String dirRows) {
        int startNameIndex1 = dirRows.indexOf(" ") + 1;
        return dirRows.substring(startNameIndex1);
    }

    private void runLinearSearch(String[] findRows, String[] dirRows) {
        System.out.println("Start searching (linear search)...");

        AtomicInteger foundEntries = new AtomicInteger(0);
        long searchDuration = getMethodDuration(() -> findEntriesUsingLinearSearch(findRows, dirRows, foundEntries));

        System.out.printf("Found %s / %s entries. Time taken: %s\n",
                foundEntries, findRows.length, getTimeTakenStr(searchDuration));
    }

    private String getTimeTakenStr(long duration) {
        Instant instant = Instant.ofEpochMilli(duration);
        LocalTime localTime = LocalTime.ofInstant(instant, ZoneId.systemDefault());
        return String.format("%s min. %s sec. %s ms.",
                localTime.getMinute(),
                localTime.getSecond(),
                localTime.getNano() / 1000000);
    }

    private void findEntriesUsingLinearSearch(String[] findRows, String[] dirRows, AtomicInteger foundEntries) {
        for (String fRow: findRows) {
            for (String dRow: dirRows) {
                if (dRow.contains(fRow)) {
                    foundEntries.incrementAndGet();
                    break;
                }
            }
        }
    }

    private void findEntriesUsingJumpSearch(String[] findRows, String[] sortedDirRows, AtomicInteger foundEntries) {
        int jumpLen = (int) Math.floor(Math.sqrt(sortedDirRows.length));
        for (String fRow: findRows) {
            for (int i = 0; i < sortedDirRows.length; i += jumpLen) {
                if (i >= sortedDirRows.length) {
                    i = sortedDirRows.length - 1;
                }

                String name = getNameFromPhoneBookRow(sortedDirRows[i]);
                int startNameIndex;
                if (name.compareTo(fRow) == 0) {
                    foundEntries.incrementAndGet();
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
                            foundEntries.incrementAndGet();
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void findEntriesUsingBinarySearch(String[] findRows, String[] sortedDirRows, AtomicInteger foundEntries) {
        for (String fRow: findRows) {
            if (findWithBinarySearch(fRow, sortedDirRows, 0, sortedDirRows.length -1, -1)) {
                foundEntries.incrementAndGet();
            }
        }
    }

    private boolean findWithBinarySearch(String fRow, String[] sortedDirRows, int lowIndex, int highIndex, int previousIndex) {
        int currentIndex = (lowIndex + highIndex) / 2;
        if (currentIndex == previousIndex) {
            return false;
        }
        String indexName = getNameFromPhoneBookRow(sortedDirRows[currentIndex]);
        if (indexName.compareTo(fRow) == 0) {
            return true;
        }
        if (indexName.compareTo(fRow) > 0) {
            return findWithBinarySearch(fRow, sortedDirRows, lowIndex, currentIndex, currentIndex);
        }
        return findWithBinarySearch(fRow, sortedDirRows, currentIndex, highIndex, currentIndex);
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
