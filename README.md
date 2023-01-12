# 🚀Gupang

## 개발 동기 및 앱 설명

  쇼핑몰api 구현 프로젝트
  
## 사용한 기술 및 클라우드

  - 언어 및 프레임워크: `JAVA`, `Spring`, `Springboot`
    - JAVA 11, Springframeword boot 2.7.2 사용
    
- 클라우드 서비스: `AWS EC2`, `AWS ELB`, `AWS RDS`, `AWS Elasticache`
    - AWS EC2 클라우드 사용
    - DB는 AWS RDS, 캐시는 AWS Elasticache 클라우드 환경 사용
    - AWS ELB를 도입하여 일정 트래픽 이상 발생하면 새로운 EC2를 생성하여 트래픽 분산
    
- 데이터베이스 및 orm: `mysql`, `JPA`, `Spring data JPA`, `Quarydsl`
    - DB는 mysql을 사용
    - JPA를 사용하여 객체지향 언어와 sql 간의 구조적 다름을 개선하고 sql을 객체 입장에서 프로그래밍할 수 있었습니다.
    - Spring data JPA를 도입하여 불필요한 crud 코드를 줄였습니다.
    - Quarydsl을 사용하여 컴파일시 쿼리 에러를 잡아내어 런타임시 발생할 수 있는 쿼리 에러를 줄였습니다.
    
- 캐싱 및 세션 관리: `redisRepository`, `redisSession`
    - redis를 이용하여 많은 쿼리가 예상되는 데이터를 캐싱
    - 수정되지 않고 계속해서 데이터 요청이 들어오는 session또한 캐싱하여 효율성 증대

- 로깅: `Spring AOP`, `logback`
    - Spring AOP를 도입해 로깅과 같이 다수의 모듈에 필요한 횡단 관심사를 충족 시켰습니다.
    - logback을 사용하여 프로그램 사용시 발생할 수 있는 WARN 사항들을 파일화하고 백업하였습니다.
    
- API: `swagger`
    - Swagger를 통해 api를 시각화하고 정리하였습니다.
    
- 검색: `Elasticsearch`
    - Elasticsearch의 역색인화를 이용해 빠르고 정확하게 검색 결과를 도출하였습니다.
  
 ## 데이터베이스 
 
<img width="596" alt="스크린샷 2022-11-01 오후 9 39 35" src="https://user-images.githubusercontent.com/52123195/199235086-74093c53-1d9b-41f4-8ce7-f0d76114ae94.png">

  ### 개요
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


## 문제발생점과 해결방법

**캐싱 & 세션**

- 상품 리뷰와 같이 많은 양의 데이터를 고객이 부를때마다 DB 쿼리하는 것은 비효율적
    - 그렇기 때문에 Redis repository를 도입하여 쿼리 하지 않고 in-memory 방식으로 빠르게 정보 쿼리 가능하게 구현하였습니다.
    - 캐시는 성능이 높아 빠르게 요청 값을 반환하는 장점이 있지만 비용이 비싸다는 단점이 있습니다. 그렇기 때문에 리뷰 중 추천수 높은 순으로 5개의 리뷰를 캐싱하고 캐시에 정해 놓은 저장 리뷰 수 보다 적거나 더 많은 리뷰를 요청하면 그때 DB 쿼리하도록 설계하였습니다.

**다형성과 NoSql**

- 쿠폰이 퍼센트와 금액 방식으로 나뉘어 있는데 나중에 추가 될 새로운 형식의 쿠폰까지 대비해 쿠폰 도메인을 구성할 필요가 있었습니다.
    - 쿠폰 도메인을 추상 클래스로 설계하여 쿠폰 유효 검사와 같이 모든 쿠폰에 쓰이는 로직은 구현해 놓고 쿠폰 마다 다르게 구현할 할인 방법 및 할인율, 할인가격 적용 메서드 등은 추상 메서드로써 상속 받는 클래스에서 구현하도록 구성하였습니다.
- 다형성을 이용해 쿠폰을 구현하고 나니 수 많은 종류의 쿠폰 타입이 관계형 DB가 담기엔 정형화 되어 있지 않고, 월드컵 쿠폰과 같이 예상치 못한 쿠폰까지 대응이 힘들다는 걸 깨달았습니다.
    - 비 정형 데이터 대응에 적합한 Nosql을 이용하여 갑자기 도입된 쿠폰 데이터를 저장하도록 설계하였습니다.**(도입 예정)**

**동시성 문제**

- 주문 발생시 재고가 줄어 드는데 여러 주문 쓰레드가 동시에 재고에 접근시 동시성 문제가 발생하는 것을 인지
    - Redis 캐시를 이용해 재고에 접근하는 쓰레드가 key를 저장하여 다른 쓰레드가 재고에 접근하려고 캐시에 같은 key를 조회 했을 때 해당 key 존재한다면 대기하도록 설계
    - 비용 절감을 위해 Redis를 이용하지 않고 직접 어플리케이션에서 DB Lock을 걸어두는 방법도 염두

**데이터베이스**

- sql의 대부분이 Read 작업인데 crud 모든 작업을 한 db 서버가 맡는 것은 비효율적
    - sql 서버를 master와 slave로 나눠 slave가 read작업을 도맡도록 하고 나머지 작업을 master가 맡고 slave와 동기화 할 수 있도록 설정하였습니다.
- jpa는 1대다 혹은 다대1 관계의 엔티티를 지연로딩으로 설정하면 지금 필요한 정보만 쿼리한다는 장점이 있지만 지연로딩한 데이터를 나중에 쿼리할 때 추가 쿼리가 나가는 N+1문제가 발생
    - fetch join을 이용해 1대다 혹은 다대1인 엔티티 정보까지 한번에 가져와 추가 쿼리가 없게 리팩토링 하였습니다.
- 1대다 관계에서 fetch join시 페이징할 수 없는 문제 발생
    - `default_batch_fetch_size` 설정을 통해 batch_size로 1대다에서 1쪽 데이터가 다쪽 데이터 만큼 늘어 나지 않아 페이징 가능하게 수정하였습니다.
- 한개의 칼럼만 가지고 인덱스 운영
    - 인덱스 칼럼을 더 추가하고 Cardinality가 높은 순으로 정렬하여 더 효율적인 검색이 가능해졌다.

**검색**

- 처음 검색 기능은 sql의 like를 사용하여 간단하게 구현
    - 하지만 정확한 이름이 정확하게 매칭하지 않으면 검색이 불가능한 문제 발생
    - 이후 ElasticSearch 도입으로 검색기능 문제 해결
