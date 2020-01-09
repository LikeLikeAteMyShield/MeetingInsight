/*******************************************************************************
 * Copyright (C) 2019, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 ******************************************************************************/
package com.hackaton.meetinginsight.redis;

import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@AllArgsConstructor
public class RedisConnectionPool {

    private JedisPool jedisPool;

    public Jedis getJedis() {
        for (var retries = 0; retries < 2; retries++) {
            try {
                return jedisPool.getResource();
            } catch (JedisConnectionException exception) {
                exception.printStackTrace();
            }
        }

        throw new RuntimeException();
    }

    public static JedisPool setupJedisConnectionPool() {
        var jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1);
        jedisPoolConfig.setMaxIdle(0);
        return new JedisPool(jedisPoolConfig, "localhost", 6379, 10000);
    }
}
