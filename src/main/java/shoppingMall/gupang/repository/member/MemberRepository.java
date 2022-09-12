package shoppingMall.gupang.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.gupang.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
