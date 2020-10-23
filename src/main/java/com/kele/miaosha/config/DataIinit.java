package com.kele.miaosha.config;

import com.kele.miaosha.constant.MiaoShaConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataIinit implements CommandLineRunner {

    @Autowired
    RedisTemplate<String, Long> redisTemplate;


    @Override
    public void run(String... args) throws Exception {
        log.info("初始化数据....");
        redisTemplate.opsForValue().set(MiaoShaConstant.MIAOSHA_PREFIX + 1, 100L);
    }
}
