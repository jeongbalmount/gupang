package shoppingMall.gupang.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.controller.review.dto.ReviewDtoRepository;
import shoppingMall.gupang.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.controller.review.dto.ReviewItemDto;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    private final ReviewDtoRepository reviewDtoRepository;

    @Override
    public ReviewItemDto addReview(ReviewItemDto reviewItemDto) {

        Optional<Item> optionalItem = itemRepository.findById(reviewItemDto.getItemId());
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }

        Review review = new Review(item, reviewItemDto.getTitle(), reviewItemDto.getContent());
        Review savedReview = reviewRepository.save(review);
        reviewItemDto.setReviewId(savedReview.getId());
        reviewDtoRepository.save(reviewItemDto);

        return reviewItemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewItemDto> getItemReviews(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        return reviewDtoRepository.findReviewItemDtoByItemId(itemId);
    }

    @Override
    public void removeReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElse(null);
        if (review == null) {
            throw new NoReviewException("해당 리뷰가 없습니다.");
        }

        reviewRepository.delete(review);
        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        if (reviewItemDto == null) {
            throw new NoReviewException("해당 리뷰가 없습니다.(cache 오류)");
        }
        reviewDtoRepository.delete(reviewItemDto);
    }

    @Override
    public void addLike(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review review = optionalReview.orElse(null);
        if (review == null) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        review.addGoodBtnCount();

        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        if (reviewItemDto == null) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.(cache 문제)");
        }
        int reviewLike = reviewItemDto.getLike();
        reviewItemDto.setLike(reviewLike+1);
        reviewDtoRepository.save(reviewItemDto);
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

        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(dto.getReviewId());
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        if (reviewItemDto == null) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.(cache 문제)");
        }
        reviewItemDto.setContent(dto.getNewContent());
        reviewItemDto.setTitle(dto.getNewTitle());

        reviewDtoRepository.save(reviewItemDto);
    }
}
