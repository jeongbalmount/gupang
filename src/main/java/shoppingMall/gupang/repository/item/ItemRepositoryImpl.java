package shoppingMall.gupang.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import shoppingMall.gupang.domain.Item;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QCategory.category;
import static shoppingMall.gupang.domain.QItem.item;
import static shoppingMall.gupang.domain.QSeller.seller;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Item> findItemsBySeller(Long seller_id) {
        return queryFactory
                .selectFrom(item)
                .join(item.category, category).fetchJoin()
                .join(item.seller, seller).fetchJoin()
                .where(item.seller.id.eq(seller_id))
                .fetch();
    }

    @Override
    public List<Item> findItemsByCategory(Long category_id) {
        return queryFactory
                .selectFrom(item)
                .join(item.category, category).fetchJoin()
                .join(item.seller, seller).fetchJoin()
                .where(item.category.id.eq(category_id))
                .fetch();
    }

    @Override
    public List<Item> findItemByString(String subString) {
        return queryFactory
                .selectFrom(item)
                .where(item.name.contains(subString))
                .fetch();
    }
}
