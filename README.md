# Playground
Android file explorer with clean architecture

### Focus
MVP vs MVVM vs Flux

`MVVM` : 잦은 Model, View 갱신에 장점이 있음. 하지만 안드로이드에서의 MVVM은 대부분 Databinding 모듈을 끌고 다녀야 한다는 것이 ..  
`MVP` : 플랫폼 의존적 코드와 로직을 분리할 수 있음. Test 를 분리하는데에 장점이 있으나, 작은 프로젝트 특성 상 적절하지 못하다 생각.  
`Flux` : 잦은 Model, View 갱신에 장점이 있음. 하지만 안드로이드 진영에서는 어째서인지 웹에서처럼은 흥하지 못하고 있다. 적절한 Store Library가 미존재

### Conclusion
본질은 간단한 역량 확인용 프로젝트로 생각되기 때문에, 적절한 아키텍쳐로 보이지는 않지만,
전역 Store/Action/Dispatch 를 통하지 않는 간단한 데이터 통신을 구조를 이용할 예정.
