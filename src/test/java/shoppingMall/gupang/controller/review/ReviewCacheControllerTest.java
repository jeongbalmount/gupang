package shoppingMall.gupang.controller.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingMall.gupang.repository.review.ReviewDtoRepository;
import shoppingMall.gupang.repository.review.ReviewRepository;

import javax.persistence.EntityManager;

/*
    <캐시 주로 사용하는 테스트>

    - 리뷰 좋아요 버튼 누르기
        - 좋아요 누른 리뷰 좋아요 1만큼 제대로 늘어 났는지 확인
        - 좋아요 누른 리뷰가 캐싱 되었다면 캐싱된 리뷰도 1만큼 늘어났는지 확인
        - 좋아요가 1만큼 늘어난 리뷰의 좋아요가 캐시의 리뷰중 가장 좋아요 수가 적은 리뷰의 좋아요보다 커진다면
          캐시에서 가장 좋아요가 적은 리뷰를 지우고 1만큼 늘어난 리뷰가 캐싱 되어야 한다.
    - 상품에 적힌 리뷰들 가져오기
        - 페이지 맞게 리뷰들 가져오기
            - 1페이지 불러올 때 캐시에서 가져오는지 테스트
            - 2페이지부터 캐시가 아닌 db에서 가져오는지 테스트
        - 캐시에 5개의 리뷰보다 적은 리뷰를 갖고 있을 때 db의 리뷰 중 가장 좋아요가 많은 리뷰 캐싱하기

 */

@SpringBootTest
@Transactional
@Slf4j
@AutoConfigureMockMvc
public class ReviewCacheControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewDtoRepository reviewDtoRepository;



}
