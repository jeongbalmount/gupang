package shoppingMall.gupang.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.repository.item.ItemRepository;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
public class ItemRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void itemRepositoryTest() {
        Category category1 = new Category("name");
        Category category2 = new Category("name");
        Seller seller1 = new Seller("010-2222-3333", "manager");
        Seller seller2 = new Seller("010-2222-3333", "manager");
        em.persist(category1);
        em.persist(category2);
        em.persist(seller1);
        em.persist(seller2);

        Item item = new Item(1000, 10, 100, seller1, category1);
        Item item2 = new Item(2000, 10, 100, seller1, category1);
        Item item3 = new Item(3000, 10, 100, seller2, category2);
        Item item4 = new Item(4000, 10, 100, seller2, category2);

        em.persist(item);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);

        List<Item> items1 = itemRepository.findByCategory(category1);
        List<Item> items2 = itemRepository.findBySeller(seller1);

        List<Item> items3 = itemRepository.findByCategory(category2);
        List<Item> items4 = itemRepository.findBySeller(seller2);

        for (Item i : items1) {
            log.info(String.valueOf(i.getItemPrice()));
        }

        for (Item i : items2) {
            log.info(String.valueOf(i.getItemPrice()));
        }

        for (Item i : items3) {
            log.info(String.valueOf(i.getItemPrice()));
        }

        for (Item i : items4) {
            log.info(String.valueOf(i.getItemPrice()));
        }

    }
}
