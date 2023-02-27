package shoppingMall.gupang.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.web.controller.item.dto.ItemSearchDto;
import shoppingMall.gupang.domain.Category;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Seller;
import shoppingMall.gupang.elasticsearch.itemSearch.ItemSearchRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
public class ElasticsearchTest {

    @Autowired
    private ItemSearchRepository itemSearchRepository;

    @Autowired
    private EntityManager em;

    private List<Item> items = new ArrayList<>();


    @BeforeEach
    void before(){
        Seller seller1 = new Seller("010-1111-1111", "managerName1");
        Seller seller2 = new Seller("010-2222-2222", "managerName2");
        Seller seller3 = new Seller("010-1111-1111", "managerName3");
        Seller seller4 = new Seller("010-2222-2222", "managerName4");
        Seller seller5 = new Seller("010-1111-1111", "managerName5");
        Seller seller6 = new Seller("010-2222-2222", "managerName6");
        Seller seller7 = new Seller("010-1111-1111", "managerName7");
        Seller seller8 = new Seller("010-2222-2222", "managerName8");
        Seller seller9 = new Seller("010-1111-1111", "managerName9");
        Seller seller10 = new Seller("010-2222-2222", "managerName10");
        Seller seller11 = new Seller("010-1111-1111", "managerName11");
        Seller seller12 = new Seller("010-2222-2222", "managerName12");
        Seller seller13 = new Seller("010-1111-1111", "managerName13");
        Seller seller14 = new Seller("010-2222-2222", "managerName14");
        Seller seller15 = new Seller("010-1111-1111", "managerName15");
        em.persist(seller1);
        em.persist(seller2);
        em.persist(seller3);
        em.persist(seller4);
        em.persist(seller5);
        em.persist(seller6);
        em.persist(seller7);
        em.persist(seller8);
        em.persist(seller9);
        em.persist(seller10);
        em.persist(seller11);
        em.persist(seller12);
        em.persist(seller13);
        em.persist(seller14);
        em.persist(seller15);

        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        Category category3 = new Category("category3");
        Category category4 = new Category("category4");
        Category category5 = new Category("category5");
        Category category6 = new Category("category6");
        Category category7 = new Category("category7");
        Category category8 = new Category("category8");
        Category category9 = new Category("category9");
        Category category10 = new Category("category10");
        Category category11 = new Category("category11");
        Category category12 = new Category("category12");
        Category category13 = new Category("category13");
        Category category14 = new Category("category14");
        Category category15 = new Category("category15");
        em.persist(category1);
        em.persist(category2);
        em.persist(category3);
        em.persist(category4);
        em.persist(category5);
        em.persist(category6);
        em.persist(category7);
        em.persist(category8);
        em.persist(category9);
        em.persist(category10);
        em.persist(category11);
        em.persist(category12);
        em.persist(category13);
        em.persist(category14);
        em.persist(category15);

        Item item1 = new Item("item Name7", 10000, 100, seller1, category1);
        Item item2 = new Item("itemName8", 10000, 100, seller2, category2);
        Item item3 = new Item("아이템 입니다", 10000, 100, seller3, category3);
        Item item4 = new Item("아이템 입니", 10000, 100, seller4, category4);
        Item item5 = new Item("이게 아이템이다.", 10000, 100, seller5, category5);
        Item item6 = new Item("이게 아이템다.", 10000, 100, seller6, category6);
        Item item7 = new Item("item Name7", 10000, 100, seller7, category7);
        Item item8 = new Item("itemName8", 10000, 100, seller8, category8);
        Item item9 = new Item("item Name9", 10000, 100, seller9, category9);
        Item item10 = new Item("itemN ame10", 10000, 100, seller10, category10);
        Item item11 = new Item("item Name11", 10000, 100, seller11, category11);
        Item item12 = new Item("아이 템", 10000, 100, seller12, category12);
        Item item13 = new Item("아이템입니다", 10000, 100, seller13, category13);
        Item item14 = new Item("아이템 이다", 10000, 100, seller14, category14);
        Item item15 = new Item("아 이 템", 10000, 100, seller15, category15);
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.persist(item5);
        em.persist(item6);
        em.persist(item7);
        em.persist(item8);
        em.persist(item9);
        em.persist(item10);
        em.persist(item11);
        em.persist(item12);
        em.persist(item13);
        em.persist(item14);
        em.persist(item15);

        ItemSearchDto dto1 = new ItemSearchDto(item1.getId(), item1.getName(), item1.getItemPrice(),
                item1.getCategory().getName());
        ItemSearchDto dto2 = new ItemSearchDto(item2.getId(), item2.getName(), item2.getItemPrice(),
                item2.getCategory().getName());
        ItemSearchDto dto3 = new ItemSearchDto(item3.getId(), item3.getName(), item3.getItemPrice(),
                item3.getCategory().getName());
        ItemSearchDto dto4 = new ItemSearchDto(item4.getId(), item4.getName(), item4.getItemPrice(),
                item4.getCategory().getName());
        ItemSearchDto dto5 = new ItemSearchDto(item5.getId(), item5.getName(), item5.getItemPrice(),
                item5.getCategory().getName());

        itemSearchRepository.save(new ItemSearchDto(item7.getId(), item7.getName(), item7.getItemPrice(), item7.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item8.getId(), item8.getName(), item8.getItemPrice(), item8.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item9.getId(), item9.getName(), item9.getItemPrice(), item9.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item10.getId(), item10.getName(), item10.getItemPrice(), item10.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item11.getId(), item11.getName(), item11.getItemPrice(), item11.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item12.getId(), item12.getName(), item12.getItemPrice(), item12.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item13.getId(), item13.getName(), item13.getItemPrice(), item13.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item14.getId(), item14.getName(), item14.getItemPrice(), item14.getCategory().getName()));
        itemSearchRepository.save(new ItemSearchDto(item15.getId(), item15.getName(), item15.getItemPrice(), item15.getCategory().getName()));
        itemSearchRepository.save(dto1);
        itemSearchRepository.save(dto2);
        itemSearchRepository.save(dto3);
        itemSearchRepository.save(dto4);
        itemSearchRepository.save(dto5);

//        searchDtos.add(dto1);
//        searchDtos.add(dto2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
    }

    @AfterEach
    void after(){
        itemSearchRepository.deleteAll();
    }

    @Test
    void searchTest(){
        List<ItemSearchDto> items = itemSearchRepository.findByItemname("아이템");
        for (ItemSearchDto dto : items) {
            log.info(dto.getItemname());
        }
    }
}
