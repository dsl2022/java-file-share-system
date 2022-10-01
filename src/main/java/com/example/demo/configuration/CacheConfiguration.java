package com.example.demo.configuration;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.time.Duration;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

//import com.macrame.toptal.demo.configuration.cache.CacheKeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
//@EnableGemfireRepositories(basePackages = "com.imagego.galaxy.authentication.server.user.repository.gemfire")
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory.class)
public class CacheConfiguration extends CachingConfigurerSupport {

    @Value("${cache.expireTime:10000}")
    private int expireTime;
//    @Bean
//    public KeyGenerator keyGenerator(){
//        return new CacheKeyGenerator();
//    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        // false);
        // do your customizations here...
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
				.allowIfSubType("com.example.demo").allowIfSubType("java.util.ArrayList").build();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(ISO_DATE_TIME));
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(ISO_DATE_TIME));
        // Enable Jackson to handle the unquoted field name.
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);

        objectMapper.registerModule(javaTimeModule);
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);


        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(expireTime))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware().build();
        // Number of seconds before expiration. Defaults to unlimited (0)
        //cacheManager.setDefaultExpiration(expireTime);// Sets the default expire time (in seconds)
        return cacheManager;
    }
}