package org.alyskou.otus.service;

import org.alyskou.otus.data.News;
import org.alyskou.otus.queue.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Clock;
import java.util.List;

@Service
public class NewsService {
    private static final String NEWS_FEED_PREFIX = "news_feed_";

    private final JdbcTemplate masterJdbc;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, List<News>> newsFeedRedisTemplate;
    private final Clock clock;

    @Autowired
    NewsService(
            @Qualifier("jdbcMaster") JdbcTemplate masterJdbc,
            UserService userService,
            StringRedisTemplate stringRedisTemplate,
            RedisTemplate<String, List<News>> newsFeedRedisTemplate) {
        this.masterJdbc = masterJdbc;
        this.stringRedisTemplate = stringRedisTemplate;
        this.newsFeedRedisTemplate = newsFeedRedisTemplate;
        this.userService = userService;
        this.clock = Clock.systemUTC();
    }

    public void saveNews(String authorEmail, String text) {
        masterJdbc.update(
                "insert into news value (?, ?, ?)",
                authorEmail,
                Timestamp.from(clock.instant()),
                text);

        stringRedisTemplate.convertAndSend(Constants.NEWS_UPDATE_TOPIC, authorEmail);
    }

    public void rebuildNews(String authorEmail) {
        userService.getUserFollowers(authorEmail).forEach(email -> getNewsFeed(email, true));
    }

    public List<News> getNewsFeed(String email, boolean invalidateCache) {
        String cacheKey = newsFeedKey(email);
        if (!invalidateCache) {
            List<News> fromCache = newsFeedRedisTemplate.opsForValue().get(cacheKey);
            if (fromCache != null) return fromCache;
        }

        List<News> fromDb = masterJdbc.query(
                "select " +
                    "n.author_email, n.timestamp, n.text " +
                "from news n " +
                "inner join friendships f on n.author_email = f.to_email " +
                "where f.from_email = ? " +
                "order by n.timestamp desc;",
                new News.NewsRowMapper(),
                email);

        newsFeedRedisTemplate.opsForValue().set(cacheKey, fromDb);

        return fromDb;
    }

    public List<News> getOwnNews(String email) {
        return masterJdbc.query(
                "select * from news where author_email = ? order by timestamp desc",
                new News.NewsRowMapper(),
                email);
    }

    private static String newsFeedKey(String email) {
        return NEWS_FEED_PREFIX + email;
    }
}
