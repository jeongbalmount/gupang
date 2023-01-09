package shoppingMall.gupang.elasticsearch.coupon.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shoppingMall.gupang.elasticsearch.coupon.FixCoupon;

public interface FixCouponRepository extends ElasticsearchRepository<FixCoupon, String> {
}
