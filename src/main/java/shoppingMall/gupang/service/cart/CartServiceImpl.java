package shoppingMall.gupang.service.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.NoCartItemException;
import shoppingMall.gupang.exception.NoItemException;
import shoppingMall.gupang.exception.NoMemberException;
import shoppingMall.gupang.repository.cartItem.CartRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CartItem> getAllCartItems(Long memberId) {
        return cartRepository.findCartItemsByMember(memberId);
    }

    @Override
    public void updateItemCount(Long cartItemId, int count) {
        Optional<CartItem> optionalCartItem = cartRepository.findById(cartItemId);
        CartItem cartItem = optionalCartItem.orElse(null);
        if (cartItem == null) {
            throw new NoCartItemException("맞는 카트 상품이 없습니다.");
        }

        cartItem.updateItemCount(count);
    }

    @Override
    public void addCartItem(Long memberId, Long itemId, int count) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당하는 상품이 없습니다.");
        }

        CartItem cartItem = new CartItem(member, item, count, item.getItemPrice());
        cartRepository.save(cartItem);
    }

    @Override
    public void removeCartItems(List<Long> cartItemIds) {
        for (Long id : cartItemIds) {
            Optional<CartItem> optionalCartItem = cartRepository.findById(id);
            optionalCartItem.ifPresent(cartRepository::delete);
        }
    }
}
