package shoppingMall.gupang.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.redis.facade.RedissonLockStockFacade;
import shoppingMall.gupang.repository.category.CategoryRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.seller.SellerRepository;
import shoppingMall.gupang.service.item.ItemService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
public class StockQuantityTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    private Item theItem;

    @BeforeEach
    public void before() {
        Seller seller = new Seller("010-111-111", "name");
        Category category = new Category("category");
        sellerRepository.save(seller);
        categoryRepository.save(category);

        Item item = new Item("testItem", 100, 100, seller, category);
        itemRepository.save(item);
        theItem = item;
    }

    @AfterEach
    public void after() {
        itemRepository.delete(theItem);
    }

    @Test
    @DisplayName("재고 감소 테스트")
    public void stock_decrease() {
        Item item = theItem;

        item.removeStock(1);
        item = itemRepository.findById(item.getId()).orElseThrow();
        Assertions.assertEquals(99, item.getItemQuantity());
    }

    @Test
    void 동시에_100개의_요청_캐시사용_X() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemService.decreaseQuantity(theItem.getId(), 1);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Item item2 = itemRepository.findById(theItem.getId()).orElseThrow();
        assertEquals(0, item2.getItemQuantity());
    }

    @Test
    void 동시에_100개의_요청_캐시사용() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(theItem.getId(), 1);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Item item2 = itemRepository.findById(theItem.getId()).orElseThrow();
        assertEquals(0, item2.getItemQuantity());
    }
}
