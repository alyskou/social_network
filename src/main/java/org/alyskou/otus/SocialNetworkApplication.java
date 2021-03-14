package org.alyskou.otus;

import org.alyskou.otus.data.News;
import org.alyskou.otus.queue.Constants;
import org.alyskou.otus.queue.NewsUpdateReceiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@SpringBootApplication
public class SocialNetworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args);
    }

    @Bean
    RedisMessageListenerContainer getRedisListeningContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(adapter, new PatternTopic(Constants.NEWS_UPDATE_TOPIC));
        return container;
    }

    @Bean
    MessageListenerAdapter getListenerAdapter(NewsUpdateReceiver newsUpdateReceiver) {
        return new MessageListenerAdapter(newsUpdateReceiver, "receiveMessage");
    }

    @Bean
    RedisTemplate<String, List<News>> getNewsFeedRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<News>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
