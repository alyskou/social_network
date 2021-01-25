package org.alyskou.otus.service;

import org.alyskou.otus.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public void saveNewUsersBatch(ArrayList<User> users) {
        long startTimestamp = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(
                "insert into users values(?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, users.get(i).getEmail());
                        ps.setString(2, passwordEncoder.encode(users.get(i).getPassword()));
                        ps.setString(3, users.get(i).getFirstName());
                        ps.setString(4, users.get(i).getLastName());
                        ps.setString(5, users.get(i).getSex().toString());
                        ps.setInt(6, users.get(i).getAge());
                        ps.setString(7, users.get(i).getInterests());
                        ps.setString(8, users.get(i).getCity());
                    }

                    @Override
                    public int getBatchSize() {
                        return users.size();
                    }
                }
        );

        long endTimestamp = System.currentTimeMillis();
        System.out.printf("SQL batch update for %d users finished in %dms\n",
                users.size(), endTimestamp -  startTimestamp);
    }

    @Nullable
    public User getUser(String email) {
        User user;
        try {
            user =  jdbcTemplate.queryForObject(
                    "select * from users where email = ?",
                    new User.UserRowMapper(),
                    email);

        } catch (EmptyResultDataAccessException e){
            return null;
        }

        if (user != null) {
            List<String> friendEmails = getUserFriends(email);
            user.setFriendEmails(friendEmails) ;
        }
        return user;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from users limit 1000", new User.UserRowMapper());
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
