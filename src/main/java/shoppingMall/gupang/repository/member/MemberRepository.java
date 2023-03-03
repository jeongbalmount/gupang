package shoppingMall.gupang.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Review;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
