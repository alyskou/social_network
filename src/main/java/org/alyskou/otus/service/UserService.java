package org.alyskou.otus.service;

import org.alyskou.otus.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(User user) {
        User existingUser = getUser(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException(String.format("User with email [%s] already exists", user.getEmail()));
        }

        jdbcTemplate.update(
                "insert into users values(?,?,?,?,?,?,?,?)",
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                user.getFirstName(),
                user.getLastName(),
                user.getSex().toString(),
                user.getAge(),
                user.getInterests(),
                user.getCity());
    }

    @Nullable
    public User getUser(String email) {
        User user = null;
        try {
            user =  jdbcTemplate.queryForObject(
                    "select * from users where email = ?",
                    new User.UserRowMapper(),
                    email);

        } catch (EmptyResultDataAccessException e){
            return null;
        }

        List<String> friendEmails = getUserFriends(email);
        user.setFriendEmails(friendEmails) ;
        return user;

    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from users", new User.UserRowMapper());
    }

    public void addFriend(String fromEmail, String toEmail) {
        jdbcTemplate.update("insert into friendships values(?, ?)", fromEmail, toEmail);
    }

    public List<String> getUserFriends(String email) {
         return jdbcTemplate.queryForList(
                "select to_email from friendships where from_email = ?",
                String.class,
                email);
    }
}
