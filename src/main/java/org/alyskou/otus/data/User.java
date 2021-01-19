package org.alyskou.otus.data;

import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class User {
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    private Sex sex;

    @Positive
    private int age;

    @NotEmpty
    private String interests;

    @NotNull
    @NotEmpty
    private String city;

    private List<String> friendEmails;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getFriendEmails() {
        return friendEmails;
    }

    public void setFriendEmails(List<String> friendEmails) {
        this.friendEmails = friendEmails;
    }

    @Override
    public String toString() {
        return String.format("User{email='%s'}", email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age
                && email.equals(user.email)
                && password.equals(user.password)
                && firstName.equals(user.firstName)
                && lastName.equals(user.lastName)
                && sex == user.sex
                && interests.equals(user.interests)
                && city.equals(user.city)
                && friendEmails.equals(user.friendEmails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, firstName, lastName, sex, age, interests, city, friendEmails);
    }

    public enum Sex {
        MALE,
        FEMALE
    }

    public static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setSex(Sex.valueOf(resultSet.getString("sex")));
            user.setAge(resultSet.getInt("age"));
            user.setInterests(resultSet.getString("interests"));
            user.setCity(resultSet.getString("city"));
            return user;
        }
    }
}
