package shoppingMall.gupang.service.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.CartItem;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.cart.CartItemNotMatchWithMemberException;
import shoppingMall.gupang.exception.cart.LackOfCountException;
import shoppingMall.gupang.exception.cart.NoCartItemException;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.repository.cartItem.CartRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.web.controller.cart.dto.CartItemIdsDto;

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
    public List<CartItem> getAllCartItems(String memberEmail) {

        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 회원이 없습니다.");
        }

        return cartRepository.findCartItemsByMemberId(member.getId());
    }

    @Override
    public List<CartItem> updateItemCount(String memberEmail, Long cartItemId, int count) {

        if (count < 1) {
            throw new LackOfCountException("최소 1이상 입력하세요");
        }

        Optional<CartItem> optionalCartItem = cartRepository.findById(cartItemId);
        CartItem cartItem = optionalCartItem.orElse(null);
        if (cartItem == null) {
            throw new NoCartItemException("맞는 카트 상품이 없습니다.");
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(cartItem.getMember().getEmail());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("회원이 존재하지 않습니다.");
        }

        if (!memberEmail.equals(cartItem.getMember().getEmail())) {
            throw new CartItemNotMatchWithMemberException("카트 상품의 소유자와 맞지 않습니다.");
        }

        cartItem.updateItemCount(count);
        return cartRepository.findCartItemsByMemberId(member.getId());
    }

    @Override
    public void addCartItem(String memberEmail, Long itemId, int count) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
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
    public List<CartItem> removeCartItems(String memberEmail, CartItemIdsDto cartItemIdsDto) {

        /*
            - cartItems 돌면서 memberEmail과 cartItem의 memberEmail과 맞지 않는 케이스가 있다면
            - throw exception한다.
         */
        for (Long cartItemId : cartItemIdsDto.getCartItemIds()) {
            Optional<CartItem> optionalCartItem = cartRepository.findById(cartItemId);
            CartItem cartItem = optionalCartItem.orElse(null);
            if (cartItem == null) {
                throw new NoCartItemException("해당하는 카트 상품이 없습니다.");
            }

            // 세션의 이메일과 cartItem의 멤버의 이메일 비교
            String cartItemMemberEmail = cartItem.getMember().getEmail();
            if (!memberEmail.equals(cartItemMemberEmail)) {
                throw new CartItemNotMatchWithMemberException("카트 상품의 소유자와 맞지 않습니다.");
            }
            cartRepository.delete(cartItem);
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당하는 멤버가 없습니다.");
        }

        return cartRepository.findCartItemsByMemberId(member.getId());
    }

}
