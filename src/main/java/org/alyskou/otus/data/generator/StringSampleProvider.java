package org.alyskou.otus.data.generator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StringSampleProvider {
    private static final String NAME_SAMPLE_FILE = "./src/main/resources/sample/names.txt";
    private static final String CITIES_SAMPLE_FILE = "./src/main/resources/sample/cities.txt";

    private final String[] names;
    private final String[] cities;
    private final Random random;

    StringSampleProvider() {
        names = readSampleFile(NAME_SAMPLE_FILE).toArray(new String[0]);
        cities = readSampleFile(CITIES_SAMPLE_FILE).toArray(new String[0]);
        random = new Random(System.currentTimeMillis());
    }

    public String getSomeName() {
        return names[random.nextInt(names.length)];
    }

    public String getSomeCity() {
        return cities[random.nextInt(cities.length)];
    }

    private static ArrayList<String> readSampleFile(String filename)  {
        ArrayList<String> names = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                names.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can't load sample file [%s]", filename), e);
        }
        return names;
    }
}
