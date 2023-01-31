package shoppingMall.gupang.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.repository.seller.SellerRepository;
import shoppingMall.gupang.service.seller.SellerService;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class SellerServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SellerService sellerService;

    private Seller seller;

    @BeforeEach
    void beforeEach() {
        Seller seller = new Seller("010-1111-2222", "managerName");
        Category category = new Category("categoryName");
        Item item1 = new Item("itemName1", 10000, 1000, seller, category);
        Item item2 = new Item("itemName2", 10000, 1000, seller, category);
        Item item3 = new Item("itemName3", 10000, 1000, seller, category);
        em.persist(seller);
        em.persist(category);
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);

        this.seller = seller;
    }

//    @Test
//    void testSeller() {
//        sellerService.registerSeller(seller.getId());
//        List<Item> sellerItems = sellerService.getSellerItems(seller.getId());
//        for (Item sellerItem : sellerItems) {
//            log.info(sellerItem.getName());
//        }
//
//        log.info(seller.getManagerName());
//        sellerService.updateManagerName(seller.getId(), "newManagerName");
//        log.info(seller.getManagerName());
//    }

}
