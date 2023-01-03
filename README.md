# Authorization System
- 2022.12.22 ~ 
- 12.25일까지 1차로 프론트 없이 서버만 진행중입니다. 이후 refreshToken와, redis사용하며 token validate 구현하고 프론트 만들 예정입니다. 

### 개요
12.25까지 1차로 구현한 내용은 다음과 같습니다.
- 사용자 가입. 가입하면서 비밀번호는 단방향 암호화.
- 사용자 로그인. 로그인 성공하면 쿠키에 토큰 저장. 토큰에는 권한만 저장.
- 인터셉터를 구현하여 컨트롤러 호출 전 로그인 상태 확인(인터셉터 1), 권한 확인(인터셉터 2).
- 관리자 권한 쿠키를 가지고 있어야 가입한 유저 정보 리스트 볼 수 있음.

### 기술스택
- Java 11
- Spring Boot 2.7.7
- Jpa
- spring-boot-starter-web
- lombok
- jaxb-api
- jjwt
- h2(콘솔은 되는데 spring에서 에러로 급하게 h2 사용했습니다. 연결만 mysql로 변경하면 됩니다!)

### 코드 중 확인받고 싶은 부분
1. low couping을 유지하자 생각하며 현재 컨트롤러와 서비스가 3개로 나누었는데 좋아보이지는 않습니다. 그냥 UserController와 HomeController를 합치고 Service도 하나로 다 합치는게 맞을지 궁금합니다. 
2. 기존에는 security에서 hasRole로 간단하게 권한을 확인했는데 사용하지 않게 되면서 권한 및 로그인 확인하는 반복작업을 줄이기 위해 첫번째 인터셉터에서 로그인 확인, 두번째 인터셉터에서 쿠키에 저장된 토큰으로 각 권한을 확인하고 있는데 이게 옳으면서 효율적인 일인지 궁금합니다.

### 개발관련 과정에서 궁금했던 부분
1. token 또한 탈취 위협이 있기 때문에 위험한 정보는 최소화 해야할 것입니다. 그래서 지금은 토큰에 권한정보만 저장하고 다른 사용자 정보는 세션이나 쿠키에 직접 저장하려고 합니다. 하지만 msa일 경우 세션 유지도 힘들고 모놀리식이더라도 굳이 세션에 나누어 저장할 필요가 있을지 궁금합니다.
2. 만약 refreshToken을 구현한다면 안의 데이터는 인증에는 사용할 수 없는 다시 access를 받을 수 있도록 하는 데이터만 필요할 것입니다. 단순하게 0,1 데이터로 재발급 가능 여부를 판단해도 될지 아니면 refreshToken안에도 지금 사용자와 대조하여 재발급 해주어야 할지 궁금합니다.
3. 비밀번호를 단방향 암호화 처리했는데 이럴경우 다시 복호화 하기는 힘들 것 입니다. 이런 상황에서 옵션으로 제시하신 비밀번호 찾기는 이메일 인증으로 확인 후 비밀번호 초기화가 정답이 될지 궁금합니다.
4. 제가 코드를 작성하는 과정이 해야할 화면(컨트롤러)을 정하고 그것에 따라 필요한 entity들을 정의하여 만들고 필요한 서비스를 만들면서 레포지토리도 만들고 그 후에 필터나 util들을 추가하고 있습니다. 짧은 시간동안 만들긴 했지만 생산성이 떨어진다고 생각했는데 능력 문제지만 나아질 방법이 있다면 말씀해 주시면 숙지하겠습니다!
5. 렌더링을 SSR할지 CSR할지 둘 다 해보다가 프론트를 만들지 못했는데 서버 사이드에서 렌더링 할 경우 security를 사용하지 않고 토큰과 상태를 관리하기는 어려웠습니다. 어떻게 해야 security를 사용하지 않고 관리할 수 있을지 궁금합니다.

## 강의 내용
공통된게 많ㅇ으면 User에서, 특별한 내용이 있는 것만 Admin으로 떼서 관리


도메인 로직 분리
코드를 개발할 때 사용자의 관점에서 요구사항을 먼저 조사한다.
회사에서는 요구사항을 바탕으로 기획서가 나온다.
이 두가지를 가지고 DB설계 후 API설계를 진행한다.
하나의 명사에 대해 명확히 나누어서 생각한다. ex. 사용자는 구매자 판매자 관리자로 나뉜다.
이 하나하나가 서브도메인이 되고 DB테이블이 되고, 개발할 때 class의 단위가 될 수 있다.
도메인은 명확히 정의되어 팀원도 동일하게 인지되어야 한다.
기본적으로 소프트웨어 아키텍처는 한번 한다고 끝이 아니고 계속해서 업그레이드 된다.
행위가 아닌 명사에 대해 클래스를 만드는 것이 일반적이다.

아키텍처의 핵심 - responsibility, coupling, cohesion, 
- SRP
  - 각 클래스, 메소드, API 등은 명확한 하나의 책임역할을 가지고 있어야 한다.
- low coupling, high cohesion
  - 각 클래스는 꼭 필요한, 서로 협력하는 필드와 메소드로 구성되어야 함.

- code smell
  - 무엇이 나쁜지 알아야 고칠 수 있으니까 공부해야한다.
  - 팀 내에 리뷰에서도 이것을 기준으로 할 수 있다.
- dependency management
  - 객체간 의존을 시각화하여 확인할 수 있다.
  - 개발이 끝나고 의도한대로 잘 나왔는지 확인 할 수 있다.
- MSA 구조에서는 DB를 나누는게 Best -> low coupling
- api gateway에서 공톤 인증 처리를 한다. 여기에 redis를 붙여서 간단한 체크만 api 접근 가능한지만 한다. 서비스에서 사용하는 DB에 접근할 정도로 복잡한 작업은 피해야한다. 서버가? 다운될 수 있기 때문.
