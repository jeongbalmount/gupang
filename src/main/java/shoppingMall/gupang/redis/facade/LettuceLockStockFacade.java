package shoppingMall.gupang.redis.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.exception.item.DecreaseException;
import shoppingMall.gupang.exception.item.IncreaseException;
import shoppingMall.gupang.redis.repository.RedisLockRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.service.item.ItemService;

import java.util.Optional;

@Component
@Slf4j
public class LettuceLockStockFacade {

    private RedisLockRepository redisLockRepository;
    private ItemService itemService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, ItemService itemService) {
        this.redisLockRepository = redisLockRepository;
        this.itemService = itemService;
    }

    public void decrease(Long key, int quantity) throws InterruptedException {
        while(!redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            itemService.decreaseQuantity(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }

    public void increase(Long key, int quantity) throws InterruptedException {
        while(!redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            itemService.decreaseQuantity(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}
