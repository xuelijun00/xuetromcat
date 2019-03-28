package com.yks.bigdata.config;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by liuxing on 2017/7/28.*/


@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class RedisConfigurationTest {

    @Autowired
    ValueOperations<String, Object> valueOperations;
    @Autowired
    RedisTemplate redisTemplate;


    @Test
    public void valueOperations() throws Exception {
        String value = "redis string templater";
        valueOperations.set("test_redis",value,1000);
        valueOperations.get("test_redis");
    }

    @Autowired
    ListOperations<String, Object> listOperations;

    @Test
    public void testRedis() throws Exception {
        String key = "testkey";
        for (int i = 0;i<10;i++){
            listOperations.leftPush(key,i);
        }
        Long size = listOperations.size(key);
        System.out.println(size);
        for (int i = 0;i<size;i++){
            System.out.println(listOperations.rightPop(key));
        }

        Set<String> keys = listOperations.getOperations().keys("*");
        System.out.println(keys.size());
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }

    @Test
    public void testRedisTemplate() throws Exception {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set("2017-08-01".getBytes(),"1".getBytes());
                redisConnection.save();
                return null;
            }
        });
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStringRedisTemplate() throws Exception {
        stringRedisTemplate.opsForValue().set("1","1");
        redisTemplate.opsForValue().set("2","2");
    }


}