# 6. Collecting data with streams

1. Collectors ins a nutshell
2. Reducing and summarizing
3. Grouping
4. Partitioning
5. The Collector interface
6. Developing your own collector for better performance
7. Summary

> ### This chapter covers
> - `Collector` class로 collector 생성, 사용
> - stream data를 single vlaue로 reducing
> - Summarization
> - grouping, partitioning
> - custom collector 향상된 성능으로 개발


---

#### `collect()`와 collectors로 할 수 있는 것

- grouping : return `Map<Currency, Integer>`
- partitioning : return `Map<Boolean, List<Transaction>>`
- multilevel grouping : return `Map<String, Map<Boolean, List<Transaction>>>`

#### Grouping 예시

````
// Java 8 이전
Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>(); 

for(Transaction transaction : transactions){
    Currnecy currency = transaction.getCurrency();
    List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
    
    if(transactionsForCurrency == null){
        transactionsForCurrency = new ArrayList<>();
        transactionsByCurrencies.put(currency, transactionsForCurrency);
    }
    
    transactionsForCurrency.add(transaction);
}

// Java 8
Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream()
                                                                        .collect(groupingBy(Transaction::getCurrency));
````

- 구현 복잡
- 가독성 안좋음

## 1. Collectors in a nutshell

## 2. Reducing and summarizing

## 3. Grouping

## 4. Partitioning

## 5. The Collector interface

## 6. Developing your own collector for better performance

## 7. Summary
