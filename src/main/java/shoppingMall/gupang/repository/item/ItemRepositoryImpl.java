package shoppingMall.gupang.repository.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.QCategory;
import shoppingMall.gupang.domain.QItem;
import shoppingMall.gupang.domain.QSeller;

import javax.persistence.EntityManager;
import java.util.List;

import static shoppingMall.gupang.domain.QCategory.category;
import static shoppingMall.gupang.domain.QItem.item;
import static shoppingMall.gupang.domain.QOrder.order;
import static shoppingMall.gupang.domain.QOrderItem.orderItem;
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
