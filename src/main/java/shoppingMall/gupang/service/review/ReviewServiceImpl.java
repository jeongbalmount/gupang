package shoppingMall.gupang.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Review;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.review.NoEditedContentException;
import shoppingMall.gupang.exception.review.NoReviewException;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Override
    @CachePut(key = "#reviewDto.itemId", value = "reviewDto")
    public void addReview(ReviewDto reviewDto) {
        Optional<Member> optionalMember = memberRepository.findById(reviewDto.getMemberId());
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 멤버가 없습니다.");
        }

        Optional<Item> optionalItem = itemRepository.findById(reviewDto.getItemId());
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        Review review = new Review(member, item, reviewDto.getTitle(), reviewDto.getContent());
        reviewRepository.save(review);
    }

    @Override
    public List<Review> getMemberReviews(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 멤버가 없습니다.");
        }
        return reviewRepository.findByMember(member);
    }

    @Override
    @Cacheable(key = "#itemId", value = "review")
    public List<Review> getItemReviews(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        return reviewRepository.findByItem(item);
    }

    @Override
    public void removeReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElse(null);
        if (review == null) {
            throw new NoReviewException("해당 리뷰가 없습니다.");
        }
        reviewRepository.delete(review);
    }

    @Override
    public void addLike(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElse(null);
        if (review == null) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        review.addGoodBtnCount();
    }

    @Override
    public void editReview(ReviewEditDto dto) {
        Optional<Review> optionalReview = reviewRepository.findById(dto.getReviewId());
        Review review = optionalReview.orElse(null);
        if (review == null) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        if (dto.getNewTitle().isBlank() || dto.getNewContent().isBlank()) {
            throw new NoEditedContentException("수정된 제목과 내용은 내용이 있어야 합니다.");
        }
        review.changeTitle(dto.getNewTitle());
        review.changeContents(dto.getNewContent());
    }
}
