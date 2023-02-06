package shoppingMall.gupang.redis.facade;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import shoppingMall.gupang.redis.error.LockFailException;
import shoppingMall.gupang.service.review.ReviewService;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonLockLikeFacade {

    private final RedissonClient redissonClient;

    private final ReviewService reviewService;

    public RedissonLockLikeFacade(RedissonClient redissonClient, ReviewService reviewService) {
        this.redissonClient = redissonClient;
        this.reviewService = reviewService;
    }

    public void addLike(Long key) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            boolean available = lock.tryLock(35, 1, TimeUnit.SECONDS);
            if (!available) {
                log.warn("lock 획득 실패");
                return;
            }
            reviewService.addLike(key);
        } catch(InterruptedException e) {
            throw new LockFailException("Lock 획득 실패");
        }
    }
}
