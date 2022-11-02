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
    쇼핑몰의 기본 기능들을 구현해보았다. 
    member를 통해 회원 가입, 로그인, 회원 정보 관리를 하고 상품은 item으로 관리한다.
    회원은 item을 주문 및 검색할 수 있고, 주문시 다대다를 위한 연결 테이블인 orderItem 테이블과 배송을 위한 delivery 테이블을 같이 생성한다.
    또한 주문시 회원이 갖고 있는 상품에 맞는 쿠폰을 적용해 할인된 가격으로 상품을 구매할 수 있다.
    item은 category로 정리 되어 있고, seller가 파는 item들만 따로 확인할 수 있다.
    
    
    
  
  ### DB
  - order와 item은 다대다 관계이다. sql 방식으로 다대다 관계를 표현하기 어렵기 때문에 order_item 테이블을 만들어 1대다, 다대1 방식으로 풀어 설계했다.
  - coupon 검증시 coupon은 member 쿠폰 정보와 item 쿠폰 정보를 참고하고, review는 member가 작성한 review들, item에 작성된 review들을 불러오기 위해 member와 item 정보를 이용한다.
  - member는 cartItem을 이용해 cart에 어떤 item, 그리고 item의 수량을 참조한다.
  - member의 name에 인덱스를 걸어 member 검색시에 속도를 빠르게 한다.
  
  ### 그외
  - jpa 및 spring data jpa를 사용해 객체와 sql 간에 차이를 극복하여 데이터베이스를 더 빠르고 직관적으로 설계할 수 있었다.
  - Querydsl을 사용해 긴 쿼리 문장도 런타임 에러 없이 작성하였다. 또한 쿼리 코드를 이해하기가 훨씬 쉬워졌다.

## API 구조

  ### Member
  - 

## AWS 구조



## 고민했던 부분



  
  
  
  
  
