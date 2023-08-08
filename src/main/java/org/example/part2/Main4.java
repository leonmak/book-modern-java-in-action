package org.example.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main4 {

    public static void main(String[] args) {
        List<Member> memberList = Arrays.asList(new Member("karina", Member.Team.AESPA, true, 23), new Member("winter", Member.Team.AESPA, true, 23), new Member("gisele", Member.Team.AESPA, false, 20), new Member("ningning", Member.Team.AESPA, false, 18), new Member("irene", Member.Team.RED_VELVET, true, 28), new Member("seulgi", Member.Team.RED_VELVET, true, 27), new Member("wendy", Member.Team.RED_VELVET, true, 27), new Member("joy", Member.Team.RED_VELVET, true, 25), new Member("yeri", Member.Team.RED_VELVET, true, 22), new Member("hani", Member.Team.NEW_JEANS, false, 20), new Member("hyerin", Member.Team.NEW_JEANS, false, 18), new Member("minzi", Member.Team.NEW_JEANS, false, 20));

        System.out.println("memberList = " + memberList);

        List<String> memberNameAdult = memberList.stream().filter(member -> member.getAge() >= 20).map(Member::getName).limit(3).collect(Collectors.toList());

        System.out.println("memberNameAdult = " + memberNameAdult);


        // external iteration
        List<String> aespaMemberName = new ArrayList<>();
        Iterator<Member> iterator = memberList.iterator();
        while (iterator.hasNext()) {
            Member member = iterator.next();
            if (member.getTeam() == Member.Team.AESPA) {
                aespaMemberName.add(member.getName());
            }
        }

        // internal iteration
        List<String> aespaMemberName2 = memberList.stream().filter(member -> member.getTeam() == Member.Team.AESPA).map(Member::getName).collect(Collectors.toList());

        System.out.println("=====================================\n\n\n");

        // Intermediate operation

        List<String> aespaMemberName3 = memberList.stream().filter(member -> {
            System.out.println("filter: " + member.getName());
            return member.getTeam() == Member.Team.AESPA;
        }).map(member -> {
            System.out.println("map: " + member.getName());
            return member.getName();
        }).limit(2).collect(Collectors.toList());

        System.out.println("aespaMemberName3 = " + aespaMemberName3);
        System.out.println("=====================================\n\n\n");

        // Terminal operation

        aespaMemberName3.stream().forEach(memberName -> System.out.println("memberName = " + memberName));
    }
}
