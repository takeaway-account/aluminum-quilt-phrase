package com.example.aluminumquiltphrase.config;

import com.example.aluminumquiltphrase.entity.UserInfo;
import com.example.aluminumquiltphrase.service.UserService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Value("${cache.max-size}")
    private int cacheMaxSize;

    @Value("${cache.expire-after}")
    private int cacheExpireAfter;

    @Bean
    public LoadingCache<String, UserInfo> loadingCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheExpireAfter, TimeUnit.MILLISECONDS)
                .build(
                    new CacheLoader<String, UserInfo>() {
                        public UserInfo load(String key) throws Exception {
                            logger.info("Loading user info for " + key + " from Github.");
                            return userService.getUserInfoByUsername(key);
                        }
                    }
                );
    }
}
