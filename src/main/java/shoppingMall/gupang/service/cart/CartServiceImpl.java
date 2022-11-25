package shoppingMall.gupang.service.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.cart.dto.CartItemDto;
import shoppingMall.gupang.controller.cart.dto.CartItemsDto;
import shoppingMall.gupang.controller.cart.dto.CartItemsMemberDto;
import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.cart.LackOfCountException;
import shoppingMall.gupang.exception.cart.NoCartItemException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.repository.cartItem.CartRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 회원이 없습니다.");
        }

        return cartRepository.findCartItemsByMemberId(memberId);
    }

    @Override
    public void updateItemCount(Long cartItemId, int count) {

        if (count < 1) {
            throw new LackOfCountException("최소 1이상 입력하세요");
        }

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
    public List<CartItem> removeCartItems(CartItemsMemberDto cartItemsMemberDto) {

        for (CartItemDto dto : cartItemsMemberDto.getCartItemIds()) {
            Optional<CartItem> optionalCartItem = cartRepository.findById(dto.getCartItemId());
            CartItem cartItem = optionalCartItem.orElse(null);
            if (cartItem == null) {
                throw new NoCartItemException("해당하는 카트 상품이 없습니다.");
            }
            cartRepository.delete(cartItem);
        }
        Long memberId = cartItemsMemberDto.getMemberId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        return cartRepository.findCartItemsByMemberId(memberId);
    }

    @Override
    public Page<CartItemDto> getAllCartItemsNoFetch(Long memberId, Pageable pageable) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 회원이 없습니다.");
        }
        Page<CartItem> page = cartRepository.findByMember(member, pageable);

        return page.map(CartItemDto::new);
    }
}
