# 13. Default methods

1. Evolving APIs
2. Default methods in a nutshell
3. Usage patterns for default methods
4. Resolution rules
5. Summary

> ### This chapter covers
>
> - default method의 개념
> - API를 호환되는 방식으로 진화
> - default method의 사용 패턴
> - Resolution 규칙

---

<img src="img.png"  width="80%"/>

- Interface를 업데이트하면, 기존 구현체는 새로운 메소드를 구현해야 함
- Java 8부터 interface에 **_static method_** 와 **_default method_** 를 추가할 수 있음
    - 기존 구현체들은 명시적으로 구현하지 않아도 추가된 메소드를 사용할 수 있음
    - e.g. `java.util.Collection.stream()`, `java.util.List.sort()`

<details>
<summary>java.util.Collection.stream(), java.util.List.sort()</summary>

```java
package java.util;

public interface Collection<E> extends Iterable<E> {
    // ...
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false); // spliterator()는 Collection의 default method
    }
    // ...
}
//..

public interface List<E> extends Collection<E> {
    // ...
    default void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<E> i = this.listIterator();
        for (Object e : a) {
            i.next();
            i.set((E) e);
        }
    }
    // ...
}
```

</details>

### Static method and instances

- Interface에 static method를 추가할 수 있게 되면서,
- Utility class의 역할을 하는 interface를 만들 수 있게 됨

## 1. Evolving APIs

## 2. Default methods in a nutshell

## 3. Usage patterns for default methods

## 4. Resolution rules

## 5. Summary
