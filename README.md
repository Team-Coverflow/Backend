<p align="center">
<!--   <a href="https://coverflow.co.kr" target="_blank" >사이트 바로가기</a> -->
</p>

## 🚀 기술 스택
![image](https://github.com/fakerdeft/CoverFlow-BE/assets/98208452/cc5dffb8-d84e-48c6-869a-8adc5c2c5baf)

## 🚀 프로젝트 아키텍처
![image](https://github.com/COFLLL/.github/assets/98208452/bdd1c678-3eef-4af7-a75e-661069930261)

## ✈ 기본

- 클래스 선언부와 필드 사이에 공백 하나 추가한다.

- 어노테이션은 클래스 혹은 메서드와 가장 관련된 것을 선언부와 가깝게 한다.

- 객체 필드, 메서드 파라미터, 변수 등 재할당이 불가능한 값이면 `final`을 붙인다.

- 패키지명은 단수로 한다.

- `DTO`는 매개변수가 3개 이상일 경우 생성한다.

- 생성자 선언 순서

```
1. 기본 생성자
2. 모든 파라미터를 받는 생성자
3. 이후 파라미터가 많은 생성자가 상단에 오도록 선언
```

## ✈ Class

- 기능들의 의존성을 낮추기 위해 유지/보수를 위해 도메인 단위로 패키지를 나눈다.

- ```application```은 service, ```domain```은 entity/repository, ```presentation```은 controller 객체들을 포함한다.

- ```dto```와 ```exception```은 특정 도메인 하위에서 구현한다.

```
src
  └ main
    └ java
      └ com
        └ coverflow
          └ config
          └ member
            └ application
              └ MemberService.java
            └ domain
              └ Member.java
              └ MemberRepository.java
            └ dto
              └ request
              └ response
            └ exception
              └ InvalidMemberException.java
            └ presentation
              └ MemberController.java
          └ board
            └ application
            └ ...
          └ ...
```

- 객체 네이밍은 다음을 따른다.

```
Entity(도메인) + Controller/Service/Repository/...
```

## ✈ Method

### Presentation Layer

- ```CUD```는 save, update, delete로 통일한다.

- 단일건에 대한 ```Read```의 경우에는 파라미터에 따라 정한다. ```findById(final long Id)```

- 복수건에 대한 ```Read```의 경우 메서드 네임에 도메인 복수명과 파라미터에 대해서 작성한다. ```findByMemberId(final long memberId)```

- ```Read```를 할 때 URL이 /me로 끝나는 경우 My로 시작하는 메서드 네이밍으로
  작성한다. ```url : /api/category/me -> findMyCategories(final long MemberId)```

### Service Layer

- ```CUD```는 컨트롤러 메서드와 네이밍을 통일한다.

- ```Read```의 경우에는 파라미터에 따라 네이밍을 정한다. ```findByIdAndMemberId(final long Id, final long memberId)```

- 검증 로직을 ```Repository```에서 처리한다. ```existCategory(final long Id)```

- 불가피하게 검증 로직이 필요한 경우 ```validate```로 시작하고 검증하려는 로직에 대해서 적는다. ```validateExistCategory(final long Id)```

### Repository Layer

- ```Spring Data JPA```가 지원하는 쿼리 메서드를 작성하는 네이밍 방식과 통일한다.
  [[Spring Data JPA 쿼리 메서드]](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)

- 조회에 관한 메서드는 ```find```으로 시작한다.

## ✈ DTO

- ```DTO(Data Transfer Object)```는 ```Request```와 ```Response```로 나누어 제작한다. ```SignUpRequest, SignUpResponse```

## ✈ Test Code

- 테스트 메서드명은 영어로, ```@DisplayName```을 한글로 작성한다.

- 테스트가 어려운 외부 서비스는 Test Double을 적용한다.

- Test Fixture는 ```Inner Class```를 활용한 메서드 체이닝을 적극 활용한다.

- 테스트 클래스의 ```bean```주입은 필드 주입을 사용한다.

- ```given, when, then``` 주석을 명시적으로 붙인다. 생략하지 않는다.
    - ```given, when, then``` 절을 나누기 곤란한 경우 ```given, when & then```처럼 ```&``` 으로 합쳐서 작성한다.
      ``` 
      // given & when
      // when & then
      // given & when & then
      ```
- 예외 케이스에 대한 테스트 메서드 네이밍은 ```~ 하면 예외가 발생한다.```로 통일한다.

```
 @Test
 @DisplayName("없는 이미지를 조회하면 예외가 발생한다.")
 void failIfNotExistsImage() {
     // given
     // when & then
 }
```

- 작은 기능 단위로 ```@Nested```를 사용해 클래스로 그룹화한다.

```
 @Nested
 @DisplayName("카테고리 생성 시")
 class createCategory{

     @Test
     @DisplayName("카테고리 생성에 성공한다.")
      void success() {
          // given
          // when
          // then
      }
 }
```
