package ru.nsu.configuration.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ru.nsu.model.GDS.Race;
import ru.nsu.model.GDS.RaceFullInfo;
import ru.nsu.payload.response.AccountTripsResponse;

import java.time.Duration;
import java.util.List;


@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(4)); // Значение по умолчанию для времени жизни

        RedisCacheConfiguration regionsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(24));

        RedisCacheConfiguration depotsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(24)); //раз в 1 день

        RedisCacheConfiguration dispatchPointsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(48)); //раз в 2 дня arrivalPointsCache

        RedisCacheConfiguration arrivalPointsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(48)); //раз в 2 дня


        JavaType javaTypeForRacesByPointAndDays = objectMapper.getTypeFactory().constructParametricType(List.class, Race.class);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializerForRacesByPointAndDays = new Jackson2JsonRedisSerializer<>(javaTypeForRacesByPointAndDays);
        jackson2JsonRedisSerializerForRacesByPointAndDays.setObjectMapper(objectMapper);

        RedisCacheConfiguration racesByPointsAndDaysCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializerForRacesByPointAndDays))
                .entryTtl(Duration.ofMinutes(30)); //раз в 30 минут

        Jackson2JsonRedisSerializer<RaceFullInfo> raceFullInfoSerializer = new Jackson2JsonRedisSerializer<>(RaceFullInfo.class);
        raceFullInfoSerializer.setObjectMapper(objectMapper);

        RedisCacheConfiguration raceFullInfo = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(raceFullInfoSerializer))
                .entryTtl(Duration.ofMinutes(30)); //раз в 30 минут

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, AccountTripsResponse.class);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializerForUserTrips = new Jackson2JsonRedisSerializer<>(javaType);
        jackson2JsonRedisSerializerForUserTrips.setObjectMapper(objectMapper);

        RedisCacheConfiguration userTripsConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializerForUserTrips))
                .entryTtl(Duration.ofMinutes(30)); // Установка времени жизни кэша

        // Возвращаем кэш-менеджера с кастомными настройками
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("allRegionsInCountry", regionsCacheConfig)
                .withCacheConfiguration("depotInfo", depotsCacheConfig)
                .withCacheConfiguration("allDispatchPoint", dispatchPointsCacheConfig)
                .withCacheConfiguration("allArrivalPoint", arrivalPointsCacheConfig)
                .withCacheConfiguration("allRacesInADayFromPointToPoint", racesByPointsAndDaysCacheConfig)
                .withCacheConfiguration("raceInfo", raceFullInfo)
                .withCacheConfiguration("allAccountTrips", userTripsConfig)
                .build();
    }
}