package org.example.part2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Main6 {

    public static void main(String[] args) {
        List<Member> memberList = Arrays.asList(new Member("karina", Member.Team.AESPA, true, 23)
                , new Member("winter", Member.Team.AESPA, true, 23)
                , new Member("gisele", Member.Team.AESPA, false, 20)
                , new Member("ningning", Member.Team.AESPA, false, 18)
                , new Member("irene", Member.Team.RED_VELVET, true, 28)
                , new Member("seulgi", Member.Team.RED_VELVET, true, 27)
                , new Member("wendy", Member.Team.RED_VELVET, true, 27)
                , new Member("joy", Member.Team.RED_VELVET, true, 25)
                , new Member("yeri", Member.Team.RED_VELVET, true, 22)
                , new Member("hani", Member.Team.NEW_JEANS, false, 20)
                , new Member("hyerin", Member.Team.NEW_JEANS, false, 18)
                , new Member("minzi", Member.Team.NEW_JEANS, false, 20));

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

        String shortName1 = memberList.stream().map(Member::getName).collect(Collectors.reducing((s1, s2) -> s1 + s2)).get();

        // Grouping
        Map<Member.Team, List<Member>> memberByTeam = memberList.stream().collect(groupingBy(Member::getTeam));
        System.out.println("memberByTeam = " + memberByTeam);

        Map<Member.AgeLevel, List<Member>> memberByAgeLevel = memberList.stream()
                .collect(groupingBy(member -> {
                    if (member.getAge() < 20) {
                        return Member.AgeLevel.CHILD;
                    } else if (member.getAge() < 40) {
                        return Member.AgeLevel.ADULT;
                    } else {
                        return Member.AgeLevel.SENIOR;
                    }
                }));
        System.out.println("memberByAgeLevel = " + memberByAgeLevel);

        // Manipulating grouped elements
        Map<Member.Team, List<Member>> member20ByTeam1 = memberList.stream()
                .filter(member -> member.getAge() == 20)
                .collect(groupingBy(Member::getTeam));

        Map<Member.Team, List<Member>> member20ByTeam2 = memberList.stream()
                .collect(groupingBy(Member::getTeam, filtering(member -> member.getAge() == 20, Collectors.toList())));

        Map<Member.Team, List<String>> memberByTeam3 = memberList.stream()
                .collect(groupingBy(Member::getTeam, mapping(Member::getName, toList())));

        System.out.println("member20ByTeam1 = " + member20ByTeam1);
        System.out.println("member20ByTeam2 = " + member20ByTeam2);
        System.out.println("memberByTeam3 = " + memberByTeam3);


        Map<Member.Team, List<String>> teamTags = new HashMap<>();
        teamTags.put(Member.Team.AESPA, Arrays.asList("4인조", "SM", "여자", "블랙맘바"));
        teamTags.put(Member.Team.NEW_JEANS, Arrays.asList("5인조", "신인", "여자"));
        teamTags.put(Member.Team.IVE, Arrays.asList("6인조", "여자", "다국적 그룹"));
        teamTags.put(Member.Team.RED_VELVET, Arrays.asList("5인조", "SM", "여자", "꽃가루를 날려"));

        Map<Member.Team, Set<String>> teamWithTag = memberList.stream()
                .collect(groupingBy(Member::getTeam, flatMapping(member -> teamTags.get(member.getTeam()).stream(), toSet())));
        System.out.println("teamWithTag = " + teamWithTag);

        // Multi-level grouping
        Map<Member.Team, Map<Member.AgeLevel, List<Member>>> memberByTeamAndAgeLevel
                = memberList.stream().collect(
                groupingBy(Member::getTeam,
                        groupingBy(member -> {
                            if (member.getAge() <= 20) {
                                return Member.AgeLevel.CHILD;
                            } else if (member.getAge() < 40) {
                                return Member.AgeLevel.ADULT;
                            } else {
                                return Member.AgeLevel.SENIOR;
                            }
                        })
                )
        );
        System.out.println("memberByTeamAndAgeLevel = " + memberByTeamAndAgeLevel);

        // collecting data in subgroups
        Map<Member.Team, Long> memberCountByTeam = memberList.stream()
                .collect(groupingBy(Member::getTeam, counting()));

        System.out.println("memberCountByTeam = " + memberCountByTeam);

        Map<Member.Team, Optional<Member>> memberOldestByTeam = memberList.stream()
                .collect(groupingBy(Member::getTeam
                        , maxBy(Comparator.comparingInt(Member::getAge))));

        System.out.println("memberOldestByTeam = " + memberOldestByTeam);

        Map<Member.Team, Member> memberOldestByTeam2 = memberList.stream()
                .collect(groupingBy(Member::getTeam // classification function
                        , collectingAndThen(maxBy(Comparator.comparingInt(Member::getAge)) // wrapping collector
                                , Optional::get))); // transformation function

        System.out.println("memberOldestByTeam2 = " + memberOldestByTeam2);

        Map<Member.Team, Integer> totalAgeByTeam = memberList.stream()
                .collect(groupingBy(Member::getTeam
                        , summingInt(Member::getAge)));
        System.out.println("totalAgeByTeam = " + totalAgeByTeam);

        Map<Member.Team, Set<Member.AgeLevel>> ageLevelByTeam = memberList.stream()
                .collect(
                        groupingBy(Member::getTeam, mapping(member -> {
                                    if (member.getAge() < 20) {
                                        return Member.AgeLevel.CHILD;
                                    } else if (member.getAge() < 40) {
                                        return Member.AgeLevel.ADULT;
                                    } else {
                                        return Member.AgeLevel.SENIOR;
                                    }
                                }, toSet())
                        )
                );

        System.out.println("ageLevelByTeam = " + ageLevelByTeam);

        Map<Member.Team, Set<Member.AgeLevel>> ageLevelBYTeamCollection = memberList.stream()
                .collect(
                        groupingBy(Member::getTeam, mapping(member -> {
                                    if (member.getAge() < 20) {
                                        return Member.AgeLevel.CHILD;
                                    } else if (member.getAge() < 40) {
                                        return Member.AgeLevel.ADULT;
                                    } else {
                                        return Member.AgeLevel.SENIOR;
                                    }
                                }, toCollection(HashSet::new))
                        )
                );

        System.out.println("ageLevelBYTeamCollection = " + ageLevelBYTeamCollection);
    }
}
