package com.kk.clients.fallbackfactory;

import com.kk.clients.UserClient;
import com.kk.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        return new UserClient() {
            @Override
            public User findById(Long id) {
                log.error("查询用户失败", throwable);
                return new User();
            }
        };
    }
}
