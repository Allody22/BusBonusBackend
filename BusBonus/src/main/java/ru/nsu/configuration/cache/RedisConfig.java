package ru.etraffic.busbonus.configuration.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ru.etraffic.busbonus.configuration.cache.deserializer.RefreshTokenDeserializer;
import ru.etraffic.busbonus.configuration.cache.serializer.RefreshTokenSerializer;
import ru.etraffic.busbonus.model.GDS.Race;
import ru.etraffic.busbonus.model.GDS.RaceFullInfo;
import ru.etraffic.busbonus.model.RefreshToken;
import ru.etraffic.busbonus.payload.response.AccountOrdersByStatusesResponse;

import java.time.Duration;
import java.util.List;


@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        // Основной ObjectMapper для всех кеширований, кроме refreshToken
        ObjectMapper generalObjectMapper = new ObjectMapper();
        generalObjectMapper.registerModule(new JavaTimeModule());

        // ObjectMapper для refreshToken с кастомными сериализатором и десериализатором
        ObjectMapper refreshTokenObjectMapper = new ObjectMapper();
        refreshTokenObjectMapper.registerModule(new JavaTimeModule());
        refreshTokenObjectMapper.registerModule(new SimpleModule()
                .addSerializer(RefreshToken.class, new RefreshTokenSerializer())
                .addDeserializer(RefreshToken.class, new RefreshTokenDeserializer()));

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(1)); // Значение по умолчанию для времени жизни

        RedisCacheConfiguration regionsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(125)); //раз в 5 дней для регионов

        RedisCacheConfiguration depotsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(24)); //раз в 1 день для автовокзалов

        RedisCacheConfiguration dispatchPointsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(48)); //раз в 2 дня пункты отбытия

        RedisCacheConfiguration arrivalPointsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofHours(48)); //раз в 2 дня arrivalPointsCache


        JavaType javaTypeForRacesByPointAndDays = generalObjectMapper.getTypeFactory().constructParametricType(List.class, Race.class);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializerForRacesByPointAndDays = new Jackson2JsonRedisSerializer<>(javaTypeForRacesByPointAndDays);
        jackson2JsonRedisSerializerForRacesByPointAndDays.setObjectMapper(generalObjectMapper);

        RedisCacheConfiguration racesByPointsAndDaysCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializerForRacesByPointAndDays))
                .entryTtl(Duration.ofMinutes(30)); //раз в 30 минут

        Jackson2JsonRedisSerializer<RaceFullInfo> raceFullInfoSerializer = new Jackson2JsonRedisSerializer<>(RaceFullInfo.class);
        raceFullInfoSerializer.setObjectMapper(generalObjectMapper);

        RedisCacheConfiguration raceFullInfo = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(raceFullInfoSerializer))
                .entryTtl(Duration.ofMinutes(30)); //раз в 30 минут

        Jackson2JsonRedisSerializer<AccountOrdersByStatusesResponse> jackson2JsonRedisSerializerForUserTrips = new Jackson2JsonRedisSerializer<>(AccountOrdersByStatusesResponse.class);
        jackson2JsonRedisSerializerForUserTrips.setObjectMapper(generalObjectMapper);

        RedisCacheConfiguration userTripsConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializerForUserTrips))
                .entryTtl(Duration.ofMinutes(30)); // раз в 30 минут для поездок пользователя

        Jackson2JsonRedisSerializer<RefreshToken> refreshTokenJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(RefreshToken.class);
        refreshTokenJackson2JsonRedisSerializer.setObjectMapper(refreshTokenObjectMapper);

        RedisCacheConfiguration refreshTokenCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(refreshTokenJackson2JsonRedisSerializer)) // Используйте refreshTokenJackson2JsonRedisSerializer
                .entryTtl(Duration.ofHours(144)); // раз в 6 дней для refreshToken


        // Возвращаем кэш-менеджера с кастомными настройками
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("regionsCache", regionsCacheConfig)
                .withCacheConfiguration("refreshTCache", refreshTokenCacheConfig)
                .withCacheConfiguration("depotCache", depotsCacheConfig)
                .withCacheConfiguration("dispatchPointsCache", dispatchPointsCacheConfig)
                .withCacheConfiguration("arrivalPointsCache", arrivalPointsCacheConfig)
                .withCacheConfiguration("racesByPointsAndDaysCache", racesByPointsAndDaysCacheConfig)
                .withCacheConfiguration("raceFullInfo", raceFullInfo)
                .withCacheConfiguration("userTrips", userTripsConfig)
                .build();
    }
}