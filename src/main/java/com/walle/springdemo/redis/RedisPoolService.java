package com.walle.springdemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolService {
    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getMaxTotal());
        config.setMaxIdle(redisConfig.getMaxIdle());
        config.setMaxWaitMillis(redisConfig.getMaxWait() * 1000);
        JedisPool jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout() * 100, redisConfig.getPassword());
        return jedisPool;
    }

}
