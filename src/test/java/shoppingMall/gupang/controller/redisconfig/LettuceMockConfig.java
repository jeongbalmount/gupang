package shoppingMall.gupang.controller.redisconfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisServer;

import java.io.IOException;

@TestConfiguration
public class LettuceMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        return new RedisServer("localhost", 6379);
    }
}
