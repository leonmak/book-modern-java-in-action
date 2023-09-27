# 19. Functional programming techniques

1. Functions everywhere
2. Persistent data structures
3. Lazy evaluation with streams
4. Pattern matching
5. Miscellany
6. Summary

> ### This chapter covers
>
> - First-class citizens, higher-order functions, currying, partial application
> - Persistent data structures
> - Java Stream : Lazy evaluation, lazy list
> - Java에서 Pattern matching 구현
> - 참조 투명성, caching

---

## 1. Functions everywhere

- Function을 value처럼 사용 가능해야함
    - Function을 parameter로 전달 가능해야함
- e.g. `::` method reference, `(int x) -> x + 1` lambda
  expression, `Function<String, Integer> strToInt = Integer::parseInt;`

### 1.1 Higher-order functions

<img src="img.png"  width="80%"/>

````
Comparator<Idol> idolComparator = Comparator.comparing(Idol::getName);

// pipeline
Function<Idol, String> getFirstName = makeIdol.addThen(Idol::getName)
      .addThen(Idol::getFirstName);
````

- `Comparator.comparing`은 function을 받아 새로운 function을 return
- _higher-order functions_
    - 1개 이상의 function을 parameter로 받거나,
    - function을 return하는 function

````
Function<Function<Double,Double>, Function<Double,Double>> ...
````

- `Function<Double,Double>`을 parameter로 받고, `Function<Double,Double>`을
  return하는 function

> ### Higher-order functions의 side effect
>
> - Higher-order functions는 side effect를 가질 수 있음
> - side effect를 어떻게 제어할 것인지 API 문서화 (주석)하는 것이 좋음
> - e.g. 파라미터로 ?타입을 전달하지 않을 경우 side effect가 발생할 수 있음

### 1.2 Currying

````
// Celsius to Fahrenheit

/**
 * @param x convert 할 온도, f conversion factor, b baseline
 * @return temperature in Fahrenheit
 */
static double converter(double x, double f, double b) {
    return x * f + b;
}

````

````
static DoubleUnaryOperator curriedConverter(double f, double b){
    return (double x) -> x * f + b;
}

DoubleUnaryOperator convertCtoF = curriedConverter(9.0/5, 32);
DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);

...

doulble gbp = convertUSDtoGBP.applyAsDouble(1000);
````

> ### Formal definition of currying
>
> - `f(x,y)` : 2개의 parameter를 받는 function
> - `(g(x))(y)` : 1개의 파라미터를 받음 -> 1개의 function return -> 1개의 파라미터를 받음
> - `f` -> `g` : currying

## 2. Persistent data structures

## 3. Lazy evaluation with streams

## 4. Pattern matching

## 5. Miscellany

## 6. Summary
