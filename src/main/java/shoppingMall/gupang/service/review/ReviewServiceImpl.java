package shoppingMall.gupang.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.review.NoMatchEmailException;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.review.dto.ReviewEditDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewItemDto;
import shoppingMall.gupang.web.controller.review.dto.ReviewReturnDto;
import shoppingMall.gupang.domain.Item;
import shoppingMall.gupang.domain.Review;
import shoppingMall.gupang.exception.item.NoItemException;
import shoppingMall.gupang.exception.review.LikeLimitException;
import shoppingMall.gupang.exception.review.NoEditedContentException;
import shoppingMall.gupang.exception.review.NoReviewException;
import shoppingMall.gupang.repository.item.ItemRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final ReviewDtoRepository reviewDtoRepository;
    private final MemberRepository memberRepository;

    @Override
    public ReviewReturnDto addReview(ReviewItemDto reviewItemDto, HttpServletRequest request) {

        Optional<Item> optionalItem = itemRepository.findById(reviewItemDto.getItemId());
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }
        HttpSession session = request.getSession();
        String memberEmail = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 사용자가 없습니다.");
        }

        Review review = new Review(member, item, reviewItemDto.getTitle(), reviewItemDto.getContent());
        Review savedReview = reviewRepository.save(review);

        reviewItemDto.setReviewId(savedReview.getId());
        List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(reviewItemDto.getItemId());
        if (reviewDtos.size() < 5) {
            reviewDtoRepository.save(reviewItemDto);
        }

        return new ReviewReturnDto(review.getId(), review.getTitle(), review.getContent(), review.getLike());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewReturnDto> getItemReviews(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            log.warn("no item");
            throw new NoItemException("해당 상품이 없습니다.");
        }

        List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(item.getId());
        int dbReviewDtoCount = 5 - reviewDtos.size();
        int lessNum = 1000000;
        if (dbReviewDtoCount > 0) {
            for (ReviewItemDto dto : reviewDtos) {
                if (dto.getLike() < lessNum) {
                    lessNum = dto.getLike();
                }
            }
            List<Review> leftReviews = reviewRepository.findByItemAndLikeLessThan(item, lessNum);
            String email = "email";
            for (Review r : leftReviews) {
                ReviewItemDto newDto = new ReviewItemDto(r.getId(),
                        r.getItem().getId(), email, r.getTitle(), r.getContent(), r.getLike());
                reviewDtoRepository.save(newDto);
                reviewDtos.add(newDto);
            }
        }
        return reviewDtos.stream()
                .map(r -> new ReviewReturnDto(r.getReviewId(), r.getTitle(), r.getContent(), r.getLike()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeReview(Long reviewId, HttpServletRequest request) {
        List<Review> reviews = reviewRepository.findReviewWithMember(reviewId);
        if (reviews.size() == 0) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        Review review = reviews.get(0);
        // 세션 저장소에 저장되어 있는 멤버 이메일 값과 review 작성자의 이메일 값이 같아야 삭제 가능하다.
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (review.getMember().getEmail().equals(email)) {
            throw new NoMatchEmailException("리뷰 작성자가 아닙니다.");
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

        List<Review> reviews = reviewRepository.findReviewsWithItem(reviewId);
        if (reviews.size() == 0) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        Review review = reviews.get(0);
        int like = review.getLike();
        if (like+1 > 999999) {
            throw new LikeLimitException("좋아요 개수는 999999이하만 가능합니다.");
        }
        review.addLike();

        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        if (reviewItemDto != null) {
            int reviewLike = reviewItemDto.getLike();
            reviewItemDto.setLike(reviewLike+1);
            reviewDtoRepository.save(reviewItemDto);
        } else {
            // 원래 캐싱 되지 않았다면 캐싱된 top like 5개 중 like가 더 작은 review는 꺼내고 새로 like가 늘어난 review를 캐싱한다.
            List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(review.getItem().getId());
            for (ReviewItemDto reviewDto : reviewDtos) {
                if (reviewDto.getLike() < like) {
                    String email = "email";
                    reviewDtoRepository.delete(reviewDto);
                    reviewDtoRepository.save(new ReviewItemDto(review.getId(), review.getItem().getId(),email,
                            review.getTitle(), review.getContent(), review.getLike()));
                    break;
                }
            }
        }
    }

    @Override
    public void editReview(ReviewEditDto dto, HttpServletRequest request) {
        List<Review> reviews = reviewRepository.findReviewWithMember(dto.getReviewId());
        if (reviews.size() == 0) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        Review review = reviews.get(0);
        // 세션 저장소에 저장되어 있는 멤버 이메일 값과 review 작성자의 이메일 값이 같아야 삭제 가능하다.
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (review.getMember().getEmail().equals(email)) {
            throw new NoMatchEmailException("리뷰 작성자가 아닙니다.");
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
