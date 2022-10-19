package shoppingMall.gupang.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.review.dto.ReviewDto;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.domain.Review;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.review.NoEditedContentException;
import shoppingMall.gupang.exception.review.NoReviewException;
import shoppingMall.gupang.redis.ReviewDtoRepository;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
//    private final ReviewDtoRepository reviewDtoRepository;

    @Override
    @Cacheable(value = "reviewItemDto", key = "#reviewItemDto.id")
    public ReviewItemDto addReview(ReviewItemDto reviewItemDto) {

        Optional<Item> optionalItem = itemRepository.findById(reviewItemDto.getItemId());
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }

        Review review = new Review(item, reviewItemDto.getTitle(), reviewItemDto.getContent());
        reviewRepository.save(review);

        return reviewItemDto;
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
    @Cacheable(value = "reviewItemDto", unless = "#result==null || #result.empty")
    public List<ReviewItemDto> getItemReviews(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }

        return reviewRepository.findByItem(item).stream()
                .map(r -> new ReviewItemDto(r.getId(), item.getId(), r.getTitle(), r.getContents()))
                .collect(Collectors.toList());
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
