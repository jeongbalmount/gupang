package shoppingMall.gupang.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.domain.Member;
import shoppingMall.gupang.exception.member.NoMemberException;
import shoppingMall.gupang.exception.review.NoMatchEmailException;
import shoppingMall.gupang.repository.member.MemberRepository;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.web.SessionConst;
import shoppingMall.gupang.web.controller.review.dto.ReviewDto;
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
import java.util.ArrayList;
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
    public Long addReview(ReviewDto reviewDto, String memberEmail) {

        Optional<Item> optionalItem = itemRepository.findById(reviewDto.getItemId());
        Item item = optionalItem.orElse(null);
        if (item == null) {
            throw new NoItemException("해당 상품이 없습니다.");
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        Member member = optionalMember.orElse(null);
        if (member == null) {
            throw new NoMemberException("해당 사용자가 없습니다.");
        }

        Review review = new Review(member, item, reviewDto.getTitle(), reviewDto.getContent());
        Review savedReview = reviewRepository.save(review);

        ReviewItemDto reviewItemDto = new ReviewItemDto(review.getId(), reviewDto.getItemId(), memberEmail,
                reviewDto.getTitle(), reviewDto.getContent(), 0);
        reviewItemDto.setReviewId(savedReview.getId());
        List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(reviewDto.getItemId());
        if (reviewDtos.size() < 5) {
            reviewDtoRepository.save(reviewItemDto);
        }

        return review.getId();
    }

    /*
        - 상품에 맞는 리뷰들을 Page에 맞춰 가져온다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewReturnDto> getItemReviews(Long itemId, String memberEmail, Pageable pageable) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElse(null);
        if (item == null) {
            log.warn("no item");
            throw new NoItemException("해당 상품이 없습니다.");
        }
        // 첫 페이지의 like 가장 많은 5개 리뷰들은 캐시에서 꺼내서 보여준다. 만약 캐시에 5개 미만의 리뷰만
        // 저장 되어 있다면 캐시에 빈 리뷰들 채워 넣고 리뷰들 리턴
        List<ReviewReturnDto> reviewReturnDtos = new ArrayList<>();
        if (pageable.getPageNumber() == 0) {
            reviewReturnDtos = getFirstPageReviewReturnDtos(memberEmail, item, pageable);
        } else {
            reviewReturnDtos = getPageReviewReturnDtos(memberEmail, item, pageable);
        }

        return reviewReturnDtos;
    }

    /*
        - 첫번째 페이지의 5개 리뷰는 캐시에서 리턴한다.
     */
    private List<ReviewReturnDto> getFirstPageReviewReturnDtos(String memberEmail, Item item,
                                                               Pageable pageable) {
        // reviewDtoRepository => redis repository
        List<ReviewItemDto> reviewDtos = reviewDtoRepository.findByItemIdOrderByLikeDesc(item.getId());
        // 캐시에 저장된 리뷰 개수가 5개 미만이면 비어 있는 리뷰 수 만큼 db에서 꺼내와 캐시에 저장
        int dbReviewDtoCount = 5 - reviewDtos.size();
        int leastLike = 1000000;
        if (dbReviewDtoCount > 0) {
            // 캐시에 들어간 리뷰들 중 가장 like가 적은 리뷰의 like 찾기
            for (ReviewItemDto dto : reviewDtos) {
                if (dto.getLike() < leastLike) {
                    leastLike = dto.getLike();
                }
            }
            List<Review> leftReviews = reviewRepository.
                    findReviewsWithLikeLessThanWithMember(item, dbReviewDtoCount, pageable);
            for (Review r : leftReviews) {
                ReviewItemDto newDto = new ReviewItemDto(r.getId(),
                        r.getItem().getId(), r.getMember().getEmail(), r.getTitle(), r.getContent(), r.getLike());
                reviewDtoRepository.save(newDto);
                reviewDtos.add(newDto);
            }
        }
        // 리턴 dto에 flag를 집어 넣어서 만약 리뷰 중 현재 로그인한 회원의 이메일과 일치한다면
        // true flag를 넣어 프론트에서 수정/삭제 버튼이 보이도록 설계
        return reviewDtos.stream()
                .map(r -> {
                    boolean flag = r.getEmail().equals(memberEmail);
                    return new ReviewReturnDto(r.getReviewId(), r.getTitle(), r.getContent(), flag, r.getLike());
                })
                .collect(Collectors.toList());
    }

    /*
        - 1페이지 이후 페이지들은 pagable을 이용해 review들을 리턴한다.
     */
    private List<ReviewReturnDto> getPageReviewReturnDtos(String memberEmail, Item item, Pageable pageable) {
        List<Review> reviews = reviewRepository.findReviewWithMemberWithPage(item, pageable);
        // 리턴 dto에 flag를 집어 넣어서 만약 리뷰 중 현재 로그인한 회원의 이메일과 일치한다면
        // true flag를 넣어 프론트에서 수정/삭제 버튼이 보이도록 설계
        return reviews.stream()
                .map(r -> {
                    boolean flag = r.getMember().getEmail().equals(memberEmail);
                    return new ReviewReturnDto(r.getId(), r.getTitle(), r.getContent(), flag, r.getLike());
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeReview(Long reviewId, String memberEmail) {
        List<Review> reviews = reviewRepository.findReviewWithMember(reviewId);
        if (reviews.size() == 0) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        Review review = reviews.get(0);
        if (!review.getMember().getEmail().equals(memberEmail)) {
            throw new NoMatchEmailException("리뷰 작성자가 아닙니다.");
        }
        reviewRepository.delete(review);

        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(reviewId);
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        if (reviewItemDto != null) {
            reviewDtoRepository.delete(reviewItemDto);
        }
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
    public void editReview(ReviewEditDto dto, String memberEmail) {
        List<Review> reviews = reviewRepository.findReviewWithMember(dto.getReviewId());
        if (reviews.size() == 0) {
            throw new NoReviewException("해당하는 리뷰가 없습니다.");
        }
        Review review = reviews.get(0);
        if (!review.getMember().getEmail().equals(memberEmail)) {
            throw new NoMatchEmailException("리뷰 작성자가 아닙니다.");
        }
        if (dto.getNewTitle().isBlank() || dto.getNewContent().isBlank()) {
            throw new NoEditedContentException("수정된 제목과 내용은 내용이 있어야 합니다.");
        }
        review.changeTitle(dto.getNewTitle());
        review.changeContents(dto.getNewContent());

        /*
            - 내용을 업데이트 하려는 리뷰가 캐시 안에 있을때 수정하는 코드
         */
        Optional<ReviewItemDto> optionalReviewItemDto = reviewDtoRepository.findById(dto.getReviewId());
        ReviewItemDto reviewItemDto = optionalReviewItemDto.orElse(null);
        if (reviewItemDto != null) {
            reviewItemDto.setContent(dto.getNewContent());
            reviewItemDto.setTitle(dto.getNewTitle());

            reviewDtoRepository.save(reviewItemDto);
        }

    }
}
