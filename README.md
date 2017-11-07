# Playground
Android file explorer with clean architecture

### Tech stack
```
1. Min sdk API 16, Target API 26
2. RxAndroid/Java 2.x.x : minsdk API 9 (2.3)
3. RxBinding 2.x.x : minsdk API 14 (4.0)
4. Project lombok
5. Butterknife
6. Light-Weight-Stream
7. Storage
8. RxPermission
```

### Focus
MVP vs MVVM vs Flux

```  
1. MVVM : 잦은 Model, View 갱신에 장점이 있음. 하지만 안드로이드에서의 MVVM은 대부분 Databinding 모듈을 끌고 다녀야 한다는 것이 ..  
2. MVP : 플랫폼 의존적 코드와 로직을 분리할 수 있음. Test 를 분리하는데에 장점이 있으나, 작은 프로젝트 특성 상 적절하지 못하다 생각.  
3. Flux : 잦은 Model, View 갱신에 장점이 있음. 하지만 안드로이드 진영에서는 어째서인지 웹에서처럼은 흥하지 못하고 있다. 적절한 Store Library가 미존재  
```

### Conclusion
본질은 간단한 역량 확인용 프로젝트로 생각되기 때문에, 적절한 아키텍쳐로 보이지는 않지만  
Mobx(Reference 참조) 를 본딴 간단한 상태관리 스토어를 만들고 이를 이용하여 프로젝트를 구성함.  

### To do
```
1. 데이터 표현 포메팅
2. 파일 트리 탐색
3. 썸네일, mimetype 에 따라 다른 아이콘을 표시하도록
4. 파일 선택 혹은 롱-클릭 시 상세 정보를 표현해주도록
```

### Reference
1. Mobx : https://mobx.js.org/intro/concepts.html
2. Convention : https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md
