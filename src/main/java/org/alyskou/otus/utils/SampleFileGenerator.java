package org.alyskou.otus.utils;

import java.io.*;
import java.util.*;

public class SampleFileGenerator {
    private static final String FULL_NAME_SAMPLE_FILE = "./src/main/resources/sample/names_50k.txt";
    private static final String FIRST_NAME_SAMPLE_FILE = "./src/main/resources/sample/first_name.txt";
    private static final String LAST_NAME_SAMPLE_FILE = "./src/main/resources/sample/last_name.txt";

    public static void main(String[] args) throws IOException {
        List<String> fullNameList = readFullNameSample();
        System.out.printf("Read list of %d full names\n", fullNameList.size());

        Set<String> firstNameList = new HashSet<>();
        Set<String> lastNameList = new HashSet<>();
        for (String fullName : fullNameList) {
            String[] nameParts = fullName.split(" ");
            firstNameList.add(nameParts[0]);
            lastNameList.add(nameParts[1]);
        }
        System.out.printf("Generate %d first and %d last unique names\n", firstNameList.size(), lastNameList.size());

        writeNamesFile(firstNameList, FIRST_NAME_SAMPLE_FILE);
        writeNamesFile(lastNameList, LAST_NAME_SAMPLE_FILE);
    }

    private static List<String> readFullNameSample() throws IOException {
        List<String> names = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FULL_NAME_SAMPLE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                names.add(line);
            }

        }
        return names;
    }

    private static void writeNamesFile(Collection<String> names, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (String name : names) {
                fileWriter.append(name).append('\n');
            }
        }
    }
}
