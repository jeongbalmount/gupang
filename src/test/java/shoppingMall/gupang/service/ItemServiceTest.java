package shoppingMall.gupang.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.service.item.ItemService;

import javax.persistence.EntityManager;

@SpringBootTest
@Slf4j
@Transactional
public class ItemServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ItemService itemService;

    private Item item;
    private Seller seller;
    private Category category;

    @BeforeEach
    void beforeEach() {
        Seller seller = new Seller("010-0000-0000", "name");
        Category category = new Category("category");
//        Item item = new Item("name", 100, 10, 10, seller, category);

        em.persist(seller);
        em.persist(category);
//        em.persist(item);

        this.seller = seller;
        this.category = category;
    }

//    @Test
//    void saveItemTest(){
//        ItemDto dto = new ItemDto("name", 10000, 100, 1000, seller.getId(),
//                category.getId());
//        itemService.saveItem(dto);
//
//        Item itemByName = itemService.findItemByName(dto.getName());
//        log.info(itemByName.getName());
//    }

}
