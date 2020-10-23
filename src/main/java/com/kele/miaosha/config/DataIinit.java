package com.kele.miaosha.config;

import com.kele.miaosha.constant.MiaoShaConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
public class DataIinit implements InitializingBean {


    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化数据....");
        redisTemplate.opsForHash().put(MiaoShaConstant.MIAOSHA_PREFIX + 1, "stock", 100L);
    }


}
