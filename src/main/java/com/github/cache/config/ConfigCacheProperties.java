package com.github.cache.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * <p>
 * 创建时间为 下午1:23 2019/10/23
 * 项目名称 spring-boot-cache
 * </p>
 *
 * @author 石少东
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class ConfigCacheProperties {

    /**
     * 必须要
     *
     * @return CacheProperties
     */
    @Bean
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }

    /**
     * 必须要
     *
     * @param customizers ObjectProvider
     * @return CacheManagerCustomizers
     */
    @Bean
    public CacheManagerCustomizers cacheManagerCustomizers(@NotNull ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
    }


}
