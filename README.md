# 🚀Gupang

## 개발 동기 및 앱 설명
Spring 프레임워크를 이용하여 만든 쇼핑몰 백엔드 프로젝트입니다.

쇼핑몰의 기능에 초점을 맞춰 구현하였으며 비효율적인 부분을 최적화 하였습니다.

<br />

## 사용한 기술 및 클라우드
- 언어 및 프레임워크: **Java17, Springboot 2.7.2**
<br />

- 클라우드 서비스: **AWS EC2, ELB, RDS,Elasticache**
  - AWS EC2 사용 , ELB를 도입하여 일정 트래픽 이상 발생하면 오토 스케일링
  - DB는 RDS, 캐시는 Elasticache 사용
<br />

- 데이터베이스 및 orm: **mysql 8, spring data jpa2.7.2, Quarydsl**
  - DB는 mysql, ORM은 JPA 사용
  - Spring data JPA와 Quarydsl을 사용하여 코드를 줄이고 직관성 있는 쿼리를 이용하여 개발
<br />

- 캐싱: **Redis**
  - Redis를 이용하여 hit rate가 높을 만한 데이터 캐싱
  - 동시성 문제 캐싱 이용하여 해결
<br />

- 로깅: **Spring AOP, logback**
  - Spring AOP를 도입해 로깅과 같이 다수의 모듈에 필요한 횡단 관심사를 충족
  - logback을 사용하여 프로그램 사용시 발생할 수 있는 WARN 사항들을 파일화하고 백업
<br />

- 검색: **Elasticsearch**
  - Elasticsearch의 역색인화를 이용해 빠르고 정확하게 검색 결과를 도출
  
<br />
  
 ## 데이터베이스 
 
<img width="596" alt="스크린샷 2022-11-01 오후 9 39 35" src="https://user-images.githubusercontent.com/52123195/199235086-74093c53-1d9b-41f4-8ce7-f0d76114ae94.png">

  ### 개요
  - member를 통해 회원 가입, 로그인, 회원 정보 관리를 하고 상품은 item으로 관리합니다.
  - 회원은 item을 주문 및 검색할 수 있고, 주문 시 연결 테이블인 orderItem 테이블과 배송을 위한 delivery 테이블을 같이 생성합니다.
  - 또한 주문시 회원이 갖고 있는 상품에 맞는 쿠폰을 적용해 할인된 가격으로 상품을 구매할 수 있습니다.
  - item은 category로 정리 되어 있고, seller가 파는 item들만 따로 확인할 수 있습니다.
  - 회원이 카트에 담아 놓은 상품과 상품의 수량은 cartItem을 통해 볼 수 있습니다.
  - 리뷰는 상품과 회원으로 묶일 수 있습니다.
  
  ### DB
  - order와 item은 다대다 관계입니다. sql 방식으로 다대다 관계를 표현하기 어렵기 때문에 order_item 테이블을 만들어 1대다, 다대1 방식으로 풀어 설계했습니다.
  - coupon 검증시 coupon은 member 쿠폰 정보와 item 쿠폰 정보를 참고하고, review는 member가 작성한 review들, item에 작성된 review들을 불러오기 위해 member와 item 정보를 이용합니다.
  - member는 cartItem을 이용해 cart에 담겨있는 item, 그리고 item의 수량을 참조합니다.
  - member의 email과 name에 인덱스를 걸어 member 검색시에 속도를 빠르게 합니다.

<br />

## API 구조
<details>

  ### Member
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/member|중복되는 아이디 확인|
  |POST|/member|회원가입|
  
  ### Login
  |Method|URL|Desc.|
  |------|---|---|
  |POST|/login|로그인|
  |POST|/logout|로그아웃|
  
  ### Cart
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/cart/{memberId}|회원의 카트 상품 목록 불러오기|
  |POST|/cart|카트에 새로운 물품 등록|
  |PATCH|/cart|카트에 물품 개수 업데이트|
  |DELETE|/cart|카트에 물품 삭제|
  
  ### Category
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/category|카테고리 해당 상품 불러오기|
  
  ### Coupon
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/coupon/{memberId}|회원이 갖고 있는 쿠폰 불러오기|
  |POST|/coupon/add|새로운 쿠폰 등록|
  
  ### Item
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/item/{itemName}|상품 이름에 맞는 검색 결과 가져오기|
  |POST|/item/add|새로운 상품 등록|
  
  ### Order
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/order/{memberId}|회원의 주문 목록 불러오기|
  |POST|/order/add|새로운 주문 생성|
  |POST|/order/coupon|쿠폰을 사용하는 주문 생성|
  |DELETE|/order/{orderId}|해당 주문 삭제|
  
  ### Review
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/review/item/{itemId}|상품에 달린 리뷰 불러오기|
  |GET|/review/member/{itemId}|회원이 적은 리뷰 불러오기|
  |POST|/review|새로운 리뷰 생성|
  |POST|/review/like/{reviewId}|리뷰 좋아요 카운드 1늘리기|
  |PATCH|/review/{reviewId}|리뷰 내용 수정하기|
  |DELETE|/review/{reviewId}|리뷰 삭제|
  
  ### Seller
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/seller/{sellerId}|판매자가 판매하는 상품 불러오기|
  |POST|/seller/add|새로운 판매자 생성|
  |PATCH|/seller|판매자 정보 수정|
</details>

<br />

## AWS 구조
<img src="https://user-images.githubusercontent.com/52123195/216921277-86be996a-bce7-441e-918d-dda5a6f11009.png"  width="300" height="250"/>

- Elb를 사용하여 오토 스케일링을 구현했다. 그렇기 때문에 서버 사용량이 많아져도 수동으로 새로운 서버를 할당할 필요 없이 서버가 할당된다.
- 캐시서버는 Aws Elasticache를 사용하였고 Elasticache 2개를 띄워 캐시 서버와 세션 서버를 분리했다.
- Aws RDS를 사용해 데이터베이스 서버를 구축하였다. RDS도 레디스 서버들과 같이 Read 기능만을 담당하는 Slave 서버와 나머지 Update 기능을 맡는 Master 서버로 분리했다.

<br />

## 문제발생점과 해결방법

**캐싱**

- 상품 리뷰와 같은 **hit rate**가 높을 만한 데이터를 고객이 부를때마다 DB 쿼리하는 것은 비효율적
    - **Redis**(repository) 도입
    - 리뷰 중 추천수 높은 순으로 5개의 리뷰를 캐싱하고 캐시에 정해 놓은 저장 리뷰 수 보다 적거나 더 많은 리뷰를 요청하면 그때 DB 쿼리하도록 설계
    - **Eviction** 정책은 LFU로 설정
<br />

**세션**
- **Redis**를 사용해서 세션관리
- 오토 스케일링시 이점과 변하지 않는 세션 데이터 특성을 고려하여 Redis 사용

<br />
<br />

<img src="https://user-images.githubusercontent.com/52123195/221391396-7dc6b9fe-4ae9-49eb-bbb1-7493e41024ca.png"  width="300" height="250"/>

**다형성**

- 쿠폰이 퍼센트와 금액 방식으로 나뉘어 있는데 나중에 추가 될 새로운 형식의 쿠폰까지 대비해 **다형성** 도입
    - 쿠폰 도메인을 추상 클래스로 설계하여 쿠폰 유효 검사와 같이 모든 쿠폰에 쓰이는 로직은 구현해 놓고 쿠폰 마다 다르게 구현할 할인 방법 및 할인율, 할인가격 적용 메서드 등은 **추상 메서드**로써 상속 받는 클래스에서 구현하도록 구성

<br />
<br />

<img src="https://user-images.githubusercontent.com/52123195/221391453-ce6f354e-3fd4-4a52-aca9-45e5f62bde9f.png"  width="500" height="70"/>
<img src="https://user-images.githubusercontent.com/52123195/221391457-35b667af-067b-436f-a8cf-ea9d5ffd6f8c.png"  width="500" height="70"/>

**동시성 문제**

- 주문 발생시 재고가 줄어 드는데 여러 주문 쓰레드가 동시에 재고에 접근시 동시성 문제가 발생하는 것을 인지
    - 처음 계획은 mysql **비관적 락** 혹은 **낙관적** 락 고려
    - 이미 Redis 캐시를 사용하고 있기 때문에 pub/sub 방식의 **redisson 분산 락** 사용
    
<br />

**데이터베이스**

- sql 서버를 master와 slave로 나눠 **slave가 read작업**을 도맡도록 하고 **update는 master**가 맡도록 설정
- N+1문제 fetch join을 이용해 추가 쿼리 문제 해결
- 1대다 관계에서 fetch join시 페이징할 수 없는 문제 `default_batch_fetch_size` 설정을 통해 페이징 가능하게 수정하였습니다.
- 한개의 칼럼만 가지고 인덱스 운영
    - 인덱스 칼럼을 더 추가하고 Cardinality가 높은 순으로 정렬하여 더 효율적인 검색이 가능
    
<br />
<br />

<img src="https://user-images.githubusercontent.com/52123195/221391530-f555a61b-985c-4117-bbb3-84f530ccfe32.png"  width="300" height="500"/>

**검색**

- 처음 검색 기능은 sql의 like를 사용하여 간단하게 구현 했지만 이름이 정확하게 매칭되지 않으면 검색이 불가능한 문제 발생
    - 이후 ElasticSearch의 **역색인**으로 검색기능 문제 해결
    - **n-gram**을 사용하여 띄어쓰기나 순서가 바뀌어도 검색대응 가능
