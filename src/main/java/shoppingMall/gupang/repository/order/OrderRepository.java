package shoppingMall.gupang.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    Page<Order> findByMember(Member member, Pageable pageable);

    List<Order> findByMemberId(Long memberId);
}
