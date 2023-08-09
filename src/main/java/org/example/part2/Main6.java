package org.example.part2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main6 {

    public static void main(String[] args) {
        List<Member> memberList = Arrays.asList(new Member("karina", Member.Team.AESPA, true, 23)
                , new Member("winter", Member.Team.AESPA, true, 23)
                , new Member("gisele", Member.Team.AESPA, false, 20), new Member("ningning", Member.Team.AESPA, false, 18), new Member("irene", Member.Team.RED_VELVET, true, 28), new Member("seulgi", Member.Team.RED_VELVET, true, 27), new Member("wendy", Member.Team.RED_VELVET, true, 27), new Member("joy", Member.Team.RED_VELVET, true, 25), new Member("yeri", Member.Team.RED_VELVET, true, 22), new Member("hani", Member.Team.NEW_JEANS, false, 20), new Member("hyerin", Member.Team.NEW_JEANS, false, 18), new Member("minzi", Member.Team.NEW_JEANS, false, 20));

        // counting

        long howManyMembers1 = memberList.stream().count();
        long howManyMembers2 = memberList.stream().collect(Collectors.counting());

        System.out.println("howManyMembers1 = " + howManyMembers1);
        System.out.println("howManyMembers2 = " + howManyMembers2);

        // max / min
        Comparator<Member> memberAgeComparator = Comparator.comparingInt(Member::getAge);
        Optional<Member> oldestMember = memberList.stream().collect(Collectors.maxBy(memberAgeComparator));
        System.out.println("oldestMember = " + oldestMember);

        // Summarization

        int totalAge = memberList.stream().collect(Collectors.summingInt(Member::getAge));
        double avgAge = memberList.stream().collect(Collectors.averagingDouble(Member::getAge));

        System.out.println("totalAge = " + totalAge);
        System.out.println("avgAge = " + avgAge);

        IntSummaryStatistics memberStatics = memberList.stream().collect(Collectors.summarizingInt(Member::getAge));
        System.out.println("memberStatics = " + memberStatics);

        // Joining Strings

        String allMemberName = memberList.stream()
                .map(Member::getName)
                .collect(Collectors.joining());

        System.out.println("allMemberName = " + allMemberName);

        String allMemberName2 = memberList.stream()
                .map(Member::getName)
                .collect(Collectors.joining(", "));

        System.out.println("allMemberName2 = " + allMemberName2);

        int totalAges = memberList.stream().collect(Collectors.reducing(0, Member::getAge, (i, j) -> i + j));
        System.out.println("totalAges = " + totalAges);

        int maxAge = memberList.stream().collect(Collectors.reducing(0, Member::getAge, (i, j) -> i > j ? i : j));
        System.out.println("maxAge = " + maxAge);

        // Quiz 6.1: Joining strings with reducing
        String shortName = memberList.stream().map(Member::getName).collect(Collectors.joining());
        // krinawinter...

        String shortName1 = memberList.stream().map(Member::getName).collect(Collectors.reducing((s1, s2)-> s1 + s2)).get();


    }
}
