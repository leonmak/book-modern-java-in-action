# 18. Think functionally

1. Implementing and maintaining systems
2. What's functional programming?
3. Recursion vs iteration
4. Summary

> ### This chapter covers
>
> - functional programming의 필요성
> - functional programming의 정의
> - 선언적 프로그래밍과 참조 투명성
> - Guidelines for writing functional-style Java
> - Iteration vs recursion



---

## 1. Implementing and maintaining systems

- _coupling_, _cohesion_, 동시성 등 보다 사소한 debugging 요소가 유지보수성에 영향이 더 큼
    - _coupling_: 독립, 한 부분의 변경이 다른 부분에 영향을 미치는 정도
    - _cohesion_: 응집도, 한 부분이 얼마나 관련된 기능을 수행하는지
- 사소한 debugging 요소 : 기대하지 않은 value로 인한 충돌 등

### 1.1 Shared mutable data

<img src="img.png"  width="60%"/>

- 1개 이상의 method에서 mutable 자료구조를 수정하는 것은 좋지 않음

> #### 특정 list에 몇개의 class가 참조할 때
>
> - list의 주인은 누구인가?
> - 어떤 class가 list를 수정한다면 어떻게 되는가?
> - 다른 class는 list가 불변이라고 가정하는가?
> - 다른 class는 list의 변화를 어떻게 감지하는가?
> - list의 변화를 알아야 하는가? 아니면, 방어적으로 깊은 복사를 해야 하는가?

#### _side effect free function_

- 자신을 둘러산 class의 상태나 다른 객체의 상태를 수정하지 않고 `return`하는 function
- side effect
    - 생성자가 아닌 곳에서 field를 수정하는 것 e.g. setterl
    - 예외 throws
    - I/O 수행 e.g. writing to a file

#### _immutable object_

- initailize 이후에는 수정할 수 없는 object
- side effect를 방지하는 방법
- thread-safe
- locking 없이 multi-core에서 안전하게 사용

### 1.2 Declarative programming

- programming 2가지 방법
- **how**에 집중, _imperative programming_
    - e.g. do this -> update that -> return this
    - classic OOP
- **what**에 집중, _internal iteration_, _declarative programming_
    - how는 library에게 위임

````
// imperative
Transaction findAespa = transactions.get(0);

if(findAespa == null)
    throw new IllegalStateException("No transactions found");
  
for(Transaction t: transactions.subList(1, transactions.size())){
    if(t.getTeamName.equals("aespa")){
        findAespa = t;
        break;
    }
}

// declarative
Optional<Transaction> findAespa = transactions.stream()
    .filter(t -> t.getTeamName().equals("aespa"))
    .findFirst();
````

### 1.3 Why functional programming?

- declarative programming을 사용할 수 있음
- expression을 사용해 what에 집중
- side-effect free 연산
- e.g. Java의 stream을 사용하면 복잡한 query를 what에만 집중하여 작성 가능

## 2. What's functional programming?

## 3. Recursion vs iteration

## 4. Summary

