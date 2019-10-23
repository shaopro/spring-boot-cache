package com.github.cache.service.impl;

import com.github.cache.pojo.doo.UserDetailDO;
import com.github.cache.repository.IUserDetailRepository;
import com.github.cache.service.IUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 创建时间为 18:52 2019-07-07
 * 项目名称 spring-boot-cache
 * </p>
 *
 * @author 石少东
 * @version 0.0.1
 * @since 0.0.1
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "redis_cache")
public class UserDetailServiceImpl implements IUserDetailService {

    @Resource
    private IUserDetailRepository repository;

    /**
     * 指定了 cacheManager = "JsonCacheManager", 所以会用在 Redis 中生成 JSON 格式的字符串
     *
     * @param name 请求的用户名
     * @return UserDetailDO
     */
    @Cacheable(
            keyGenerator = "DefaultGenerator",
            condition = "#name !='name10'",
            unless = "#name.length() <= 4",
            cacheManager = "JsonCacheManager"
    )
    @Override
    public UserDetailDO findByName(String name) {
        log.info("---------- UserDetailService|findByName|{}", name);
        return repository.findByNameEquals(name);
    }

    @Override
    @Cacheable(keyGenerator = "DefaultGenerator")
    public UserDetailDO findByPhone(String phone) {
        log.info("---------- UserDetailService|findByPhone|{}", phone);
        return repository.findUserCacheDOByPhoneEquals(phone);
    }

    @Cacheable(keyGenerator = "DefaultGenerator")
    @Override
    public Page<UserDetailDO> findAll(@NotNull Pageable pageable) {
        log.info("---------- UserDetailService|findByPhone|{}", pageable.toString());
        return repository.findAll(pageable);
    }

    @Caching(
            cacheable = {
                    @Cacheable(key = "'[' + #a0.name + ']' "),
            },
            put = {
                    @CachePut(key = "'[' + #result.name + ']'"),
                    @CachePut(key = "'[' + #result.phone + ']'")
            }
    )
    @Override
    public UserDetailDO create(@NotNull UserDetailDO userCacheDTO) {
        log.info("UserDetailService|create|{}", userCacheDTO.toString());
        return repository.save(userCacheDTO);
    }

    @Override
    @CachePut(key = "'[' + #result.name + ']'")
    public UserDetailDO update(@NotNull UserDetailDO userCacheDTO) {
        log.info("---------- UserDetailService|update|{}", userCacheDTO.toString());
        return repository.save(userCacheDTO);
    }

    @Override
    @CacheEvict(key = "'[' + #a0 + ']'")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> delete(String name) {
        log.info("---------- UserDetailService|delete|{}", name);
        repository.deleteUserCacheDOByNameIs(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @CacheEvict(allEntries = true)
    public ResponseEntity<Void> deleteAll() {
        log.info("---------- UserDetailService|deleteAll");
        repository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
