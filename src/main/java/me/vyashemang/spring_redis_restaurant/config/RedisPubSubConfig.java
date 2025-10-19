package me.vyashemang.spring_redis_restaurant.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.vyashemang.spring_redis_restaurant.service.RedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import static me.vyashemang.spring_redis_restaurant.constant.RedisPubSubConstants.*;

@Configuration
public class RedisPubSubConfig {
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic(ORDER_EVENTS_CHANNEL));
        container.addMessageListener(listenerAdapter, new ChannelTopic(NOTIFICATION_CHANNEL));
        container.addMessageListener(listenerAdapter, new ChannelTopic(RETRY_ORDER_ASSIGNMENT_CHANNEL));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}
