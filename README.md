# 🚀Gupang

## 개발 동기 및 앱 설명

  쇼핑몰api 구현 프로젝트
  
## 사용한 기술 및 클라우드

  언어: `JAVA`
  
  프레임워크: `Spring`
  
  데이터베이스 및 orm: `mysql`, `JPA`, `Spring data JPA`, `Quarydsl`
  
  캐싱 및 세션 관리: `redis-cache`, `redis-session`
  
  클라우드: `AWS`, `ec2`, `elb`, `rds`, `elasticache`
  
  API: `Swagger`
  
  Test: `JUnit5`
  
 ## 데이터베이스 
 
<img width="596" alt="스크린샷 2022-11-01 오후 9 39 35" src="https://user-images.githubusercontent.com/52123195/199235086-74093c53-1d9b-41f4-8ce7-f0d76114ae94.png">

  ### 개요
  - 쇼핑몰의 기본 기능들을 구현해보았다. 
  - member를 통해 회원 가입, 로그인, 회원 정보 관리를 하고 상품은 item으로 관리한다.
  - 회원은 item을 주문 및 검색할 수 있고, 주문 시 연결 테이블인 orderItem 테이블과 배송을 위한 delivery 테이블을 같이 생성한다.
  - 또한 주문시 회원이 갖고 있는 상품에 맞는 쿠폰을 적용해 할인된 가격으로 상품을 구매할 수 있다.
  - item은 category로 정리 되어 있고, seller가 파는 item들만 따로 확인할 수 있다.
  - 회원이 카트에 담아 놓은 상품과 상품의 수량은 cartItem을 통해 볼 수 있다.
  
  ### DB
  - order와 item은 다대다 관계이다. sql 방식으로 다대다 관계를 표현하기 어렵기 때문에 order_item 테이블을 만들어 1대다, 다대1 방식으로 풀어 설계했다.
  - coupon 검증시 coupon은 member 쿠폰 정보와 item 쿠폰 정보를 참고하고, review는 member가 작성한 review들, item에 작성된 review들을 불러오기 위해 member와 item 정보를 이용한다.
  - member는 cartItem을 이용해 cart에 담겨있는 item, 그리고 item의 수량을 참조한다.
  - member의 name에 인덱스를 걸어 member 검색시에 속도를 빠르게 한다.
  
  ### 그외
  - jpa 및 spring data jpa를 사용해 객체와 sql 간에 차이를 극복하여 데이터베이스를 더 빠르고 직관적으로 설계할 수 있었다.
  - Querydsl을 사용해 긴 쿼리 문장도 런타임 에러 없이 작성하였다. 또한 쿼리 코드를 이해하기가 훨씬 쉬워졌다.

## API 구조

  ### Member
  |Method|URL|Desc.|
  |------|---|---|
  |POST|/member/checkId|중복되는 아이디 확인|
  |POST|/member/signup|회원가입|
  
  ### Login
  |Method|URL|Desc.|
  |------|---|---|
  |POST|/login|로그인|
  |POST|/logout|로그아웃|
  
  ### Cart
  |Method|URL|Desc.|
  |------|---|---|
  |GET|/cart/{memberId}|회원의 카트 상품 목록 불러오기|
  |POST|/cart/add|카트에 새로운 물품 등록|
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
  

## AWS 구조

<img width="360" alt="스크린샷 2022-11-01 오후 7 41 30" src="https://user-images.githubusercontent.com/52123195/199496831-2211e41d-c233-4580-b740-ff9b6e4305ba.png">

- Elb를 사용하여 오토 스케일링을 구현했다. 그렇기 때문에 서버 사용량이 많아져도 수동으로 새로운 서버를 할당할 필요 없이 서버가 할당된다.
- 캐시서버는 Aws Elasticache를 사용하였고 Elasticache 2개를 띄워 캐시 서버와 세션 서버를 분리했다.
- Aws RDS를 사용해 데이터베이스 서버를 구축하였다. RDS도 레디스 서버들과 같이 Read 기능만을 담당하는 Slave 서버와 나머지 Update 기능을 맡는 Master 서버로 분리했다.


## 고민했던 부분

### AWS

- 사람이 많이 몰릴 때마다 수동으로 서버 증설을 하기가 번거롭고 사실상 불가능하다.
  - AWS Elb를 도입하여 사람이 많이 몰려 일정 하드웨어 능력치를 초과하면 자동으로 스케일링 되도록 한다.

### 캐싱

- 상품 리뷰와 같이 많은 양의 데이터를 고객이 부를 때마다 쿼리하는것은 비효율적이다.
  - Redis 캐시를 도입하여 쿼리를 할 필요 없이 메모리에서 빠르게 데이터를 검색하여 고객에게 보여 줄수 있다.

### 예외처리

- 예외 처리를 보기 좋고 쉽게 처리할 수 있는 방법을 고민하였다.
  - @ExceptionHandler 애노테이션을 사용하고, @RestControllerAdvice를 이용해 각 컨트롤러가 갖는 예외를 쉽게 정의하고 정리할 수 있었다.
 
- 필터나 인터셉터에서 발생하는 예외도 잡고 싶었다.
  - global하게 @RestControllerAdvice를 정의하여 각 컨트롤러 뿐만아니라 필터와 인터셉터에서도 예외를 잡을 수 있게 설정했다.

### 데이터베이스
 
- sql의 대부분이 Read 작업인데 crud 모든 작업을 한 db 서버가 맡는 것은 비효율적이다.
  - sql 서버를 master와 slave로 나눠 slave가 read작업을 도맡도록 하고 나머지 update 작업을 master가 맡고 slave와 동기화 할 수 있도록 설정하였다.

- jpa는 1대다 혹은 다대1 관계의 엔티티를 지연로딩으로 나중에 가져온다. 현재 필요하지 않은 엔티티를 가져오지 않는 효율성이 있지만 나중에 가져올때 추가 쿼리가 나가는 N+1문제가 발생할 수 있다.
  - fetch join을 이용해 1대다 혹은 다대1인 엔티티 정보까지 한번에 가져와 추가 쿼리 가능성을 없앤다.

  
  
  
  
  
