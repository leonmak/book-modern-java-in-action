package org.example.part2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Main5 {

    public static void main(String[] args) {

        List<String> members = Arrays.asList("KARINA", "WINTER", "GISELLE", "NINGNING", "KARINA");

        // members.stream().distinct().forEach(member -> System.out.println("member = " + member));


        List<Member> memberList = Arrays.asList(new Member("karina", Member.Team.AESPA, true, 23), new Member("winter", Member.Team.AESPA, true, 23), new Member("gisele", Member.Team.AESPA, false, 20), new Member("ningning", Member.Team.AESPA, false, 18), new Member("irene", Member.Team.RED_VELVET, true, 28), new Member("seulgi", Member.Team.RED_VELVET, true, 27), new Member("wendy", Member.Team.RED_VELVET, true, 27), new Member("joy", Member.Team.RED_VELVET, true, 25), new Member("yeri", Member.Team.RED_VELVET, true, 22), new Member("hani", Member.Team.NEW_JEANS, false, 20), new Member("hyerin", Member.Team.NEW_JEANS, false, 18), new Member("minzi", Member.Team.NEW_JEANS, false, 20));

        List<Member> aespa1 = memberList.stream().filter(member -> {
            System.out.println("filter: " + member.getName());
            return member.getTeam() == Member.Team.AESPA;
        }).toList();

        System.out.println("aespa1 = " + aespa1 + "\n\n\n");

        List<Member> aespa2 = memberList.stream().takeWhile(member -> {
            System.out.println("takeWhile: " + member.getName());
            return member.getTeam() == Member.Team.AESPA;
        }).toList();

        System.out.println("aespa2 = " + aespa2 + "\n\n\n");

        List<Member> notAespa = memberList.stream().dropWhile(member -> {
            System.out.println("dropWhile: " + member.getName());
            return member.getTeam() == Member.Team.AESPA;
        }).toList();

        System.out.println("notAespa = " + notAespa + "\n\n\n");

        // truncate

        // limit

        List<Member> aespaTwoMember1 = memberList.stream()
                .filter(member -> member.getTeam() == Member.Team.AESPA)
                .limit(2)
                .toList();
        System.out.println("aespaTwoMember1 = " + aespaTwoMember1 + "\n\n\n");

        List<Member> aespaTwoMember2 = memberList.stream()
                .filter(member -> member.getTeam() == Member.Team.AESPA)
                .skip(2)
                .toList();
        System.out.println("aespaTwoMember2 = " + aespaTwoMember2 + "\n\n\n");

        // Mapping

        // map

        List<String> memberNamesAespa = memberList.stream()
                .filter(member -> member.getTeam() == Member.Team.AESPA)
                .map(Member::getName)
                .toList();

        System.out.println("memberNamesAespa = " + memberNamesAespa + "\n\n\n");

        List<Integer> memberNameLengthAespa = memberList.stream()
                .filter(member -> member.getTeam() == Member.Team.AESPA)
                .map(Member::getName)
                .map(String::length)
                .toList();

        System.out.println("memberNameLengthAespa = " + memberNameLengthAespa + "\n\n\n");


        // quiz 5.2 Mapping
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> result = numbers.stream().map(n -> (int) Math.pow(n, 2)).toList();
        System.out.println("result = " + result);

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        List<int[]> pairs = numbers1.stream()
                .flatMap(i ->
                        numbers2.stream()
                                .filter(j -> (i + j) % 3 == 0)
                                .map(j -> new int[]{i, j}))
                .collect(toList());

        List<int[]> pairs2 = new ArrayList<>();
        for (int i : numbers1) {
            for (int j : numbers2) {
                pairs2.add(new int[]{i, j});
            }
        }

        // Finding and matching

        if (memberList.stream().anyMatch(Member::getIsDebut)) {
            System.out.println("there is a debut member");
        }

        if (memberList.stream().allMatch(Member::checkIsNotChild)) {
            System.out.println("there is no child member");
        }

        // NONE MATCH
        if (memberList.stream().noneMatch(Member::unknownTeam)) {
            System.out.println("there is no unknown team member");
        }

        // findAny

        memberList.stream()
                .filter(member -> member.getTeam() == Member.Team.AESPA)
                .filter(Member::checkIsAdult)
                .findAny()
                .ifPresent(member -> System.out.println("member = " + member));

        // findFirst
        memberList.stream()
                .filter(member -> member.getTeam() == Member.Team.NEW_JEANS)
                .findFirst()
                .ifPresent(member -> System.out.println("NEW_JEANS leader is" + member));

        // quiz 5.3 Reducing
        Integer sum = memberList.stream().map(m -> 1).reduce(0, Integer::sum);

        // doSolution();

        Stream<int[]> pythagoreanTriples1 =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a ->
                                IntStream.rangeClosed(a, 100)
                                        .filter((b) -> {
                                            double returnVal = Math.sqrt(a * a + b * b);
                                            System.out.println("filter : " + a + " || " + b + " || " + returnVal);
                                            return returnVal % 1 == 0;
                                        })
                                        .mapToObj((b) -> {
                                            System.out.println("map : " + a + " || " + b);
                                            return new int[]{a, b, (int) Math.sqrt(a * a + b * b)};
                                        })
                        );

        Stream<double[]> pythagoreanTriples2 =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a ->
                                IntStream.rangeClosed(a, 100)
                                        .mapToObj(
                                                b -> {
                                                    System.out.println("map : " + a + " || " + b);
                                                    return new double[]{a, b, Math.sqrt(a * a + b * b)};
                                                })
                                        .filter(t -> {
                                            System.out.println("filter : " + t[0] + " || " + t[1] + " || " + t[2]);
                                            return t[2] % 1 == 0;
                                        }));

//        pythagoreanTriples1.limit(5).count();
        pythagoreanTriples2.limit(5).count();
    }

    /*6.1 practice*/
    private static void doSolution() {

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950));

        // 1. Find all transactions in the year 2011 and sort them by value (small to high).
        List<Transaction> sol1 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println("sol1 = " + sol1);

        // 2. What are all the unique cities where the traders work?
        List<String> sol2 = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(toList());
        System.out.println("sol2 = " + sol2);

        // 3. Find all traders from Cambridge and sort them by name.
        List<Trader> sol3 = transactions.stream()
                .map(t -> t.getTrader())
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(toList());
        System.out.println("sol3 = " + sol3);

        // 4. Return a string of all traders’ names sorted alphabetically.
        String sol4 = transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (n1, n2) -> n1 + " " + n2);

        String sol4Better = transactions.stream().map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(joining());

        System.out.println("sol4 = " + sol4);

        // 5. Are any traders based in Milan?
        Boolean sol5 = transactions.stream()
                .anyMatch(t -> t.getTrader().getCity().equals("Milan"));

        System.out.println("sol5 = " + sol5);

        // 6. Print the values of all transactions from the traders living in Cambridge.
        transactions.stream()
                .forEach(t -> {
                    if (t.getTrader().getCity().equals("Cambridge"))
                        System.out.println(t.getValue());
                });
        transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        // 7. What’s the highest value of all the transactions?
        Optional<Integer> sol7 = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);
        System.out.println("sol7 = " + sol7.get());


        // 8. Find the transaction with the smallest value.
        Optional<Transaction> sol8 = transactions.stream()
                .reduce((t1, t2) ->
                        t1.getValue() < t2.getValue() ? t1 : t2
                );
        Optional<Transaction> sol8Better = transactions.stream()
                .min(Comparator.comparing(Transaction::getValue));
        System.out.println("sol8 = " + sol8.get());


    }

}
