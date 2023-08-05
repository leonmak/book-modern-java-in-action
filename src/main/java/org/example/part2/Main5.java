package org.example.part2;

import java.util.Arrays;
import java.util.List;

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

    }
}
