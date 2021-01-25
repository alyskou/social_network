package org.alyskou.otus.data.generator;

import org.alyskou.otus.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserGenerator {
    private static final int MAX_RANDOM_EMAIL_POSTFIX = 100_000;

    private final StringSampleProvider stringSampleProvider;
    private final Random random;

    @Autowired
    UserGenerator(StringSampleProvider stringSampleProvider) {
        this.stringSampleProvider = stringSampleProvider;
        this.random = new Random(System.currentTimeMillis());
    }

    public User generateRandomUser() {
        User user = new User();

        String firstName = stringSampleProvider.getSomeName();
        user.setFirstName(firstName);

        String lastName = stringSampleProvider.getSomeName();
        user.setLastName(lastName);

        String city = stringSampleProvider.getSomeCity();
        user.setCity(city);

        String email = generateEmail(firstName, lastName, city);
        user.setEmail(email);

        String password = generatePassword(email);
        user.setPassword(password);

        User.Sex sex = generateSex();
        user.setSex(sex);

        int age = generateAge();
        user.setAge(age);

        String interests = generateInterests();
        user.setInterests(interests);

        return user;
    }

    private String generateEmail(String firstName, String lastName, String city) {
        city = city.replaceAll(" ", "_");
        return String.format("%s.%s.%s%d@email.com",
                firstName, lastName, city, random.nextInt(MAX_RANDOM_EMAIL_POSTFIX));
    }

    private String generatePassword(String email) {
        return String.format("%s!", email);
    }

    private User.Sex generateSex() {
        return random.nextInt(2) == 0 ? User.Sex.MALE : User.Sex.FEMALE;
    }

    private int generateAge() {
        return random.nextInt(100);
    }

    private String generateInterests() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, nulla porta hendrerit ex nec commodo";
    }
}
