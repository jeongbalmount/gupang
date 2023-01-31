package shoppingMall.gupang.redis.facade;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import shoppingMall.gupang.redis.error.LockFailException;
import shoppingMall.gupang.service.item.ItemService;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;

    private final ItemService itemService;

    public RedissonLockStockFacade(RedissonClient redissonClient, ItemService itemService) {
        this.redissonClient = redissonClient;
        this.itemService = itemService;
    }

    public void decrease(Long key, int quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            boolean available = lock.tryLock(35, 1, TimeUnit.SECONDS);
            if (!available) {
                log.warn("lock 획득 실패");
                return;
            }
            itemService.decreaseQuantity(key, quantity);
        } catch(InterruptedException e) {
            throw new LockFailException("Lock 획득 실패");
        }
    }
    public void increase(Long key, int quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            boolean available = lock.tryLock(35, 1, TimeUnit.SECONDS);
            if (!available) {
                log.warn("lock 획득 실패");
                return;
            }
            itemService.increaseQuantity(key, quantity);
        } catch(InterruptedException e) {
            throw new LockFailException("Lock 획득 실패");
        }
    }
}
